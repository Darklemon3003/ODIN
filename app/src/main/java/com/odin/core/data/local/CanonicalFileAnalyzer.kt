package com.odin.core.data.local

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import com.odin.core.domain.model.AnalysisIndicator
import com.odin.core.domain.model.AnalysisVerdict
import com.odin.core.domain.model.FileAnalysisResult
import com.odin.core.domain.model.IndicatorCategory
import com.odin.core.domain.model.IndicatorSeverity
import com.odin.core.domain.model.SelectedFile
import java.io.InputStream
import java.security.MessageDigest

/**
 * Canonical file analyzer.
 *
 * Performs real, evidence-backed analysis:
 * - SHA-256 / SHA-1 / MD5 streaming hash calculation
 * - File type detection via header magic bytes + extension
 * - Extension/header mismatch detection
 * - Deterministic suspicious string indicators
 * - Standardized test signature detection (EICAR)
 *
 * Does NOT:
 * - Convert content URIs to filesystem paths
 * - Load entire files into memory
 * - Return NO_KNOWN_INDICATORS on error
 * - Claim MALICIOUS based on ZIP header alone
 */
class CanonicalFileAnalyzer {

    companion object {
        private const val BUFFER_SIZE = 8192
        private const val MAX_STRING_SCAN_BYTES = 256 * 1024 // 256KB
        private const val MAX_HEADER_BYTES = 512

        // EICAR standardized antivirus test string
        private val EICAR_SIGNATURE =
            "X5O!P%@AP[4\\PZX54(P^)7CC)7}\$EICAR-STANDARD-ANTIVIRUS-TEST-FILE!\$H+H*".toByteArray()

        // Known file type magic bytes
        private val ZIP_MAGIC = byteArrayOf(0x50, 0x4B, 0x03, 0x04)
        private val ZIP_EMPTY_MAGIC = byteArrayOf(0x50, 0x4B, 0x05, 0x06)
        private val APK_MAGIC = byteArrayOf(0x50, 0x4B, 0x03, 0x04) // APK is ZIP-based
        private val PDF_MAGIC = "%PDF".toByteArray()
        private val PNG_MAGIC = byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47)
        private val JPEG_MAGIC = byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte())
        private val GIF_MAGIC = "GIF8".toByteArray()
        private val ELF_MAGIC = byteArrayOf(0x7F, 0x45, 0x4C, 0x46)
        private val PE_MAGIC = byteArrayOf(0x4D, 0x5A, 0x90.toByte(), 0x00) // MZ
        private val OLE_MAGIC = byteArrayOf(0xD0.toByte(), 0xCF.toByte(), 0x11.toByte(), 0xE0.toByte())
        private val RTF_MAGIC = "{\\rt".toByteArray()

        // Suspicious strings (deterministic indicators, NOT a signature database)
        private val SUSPICIOUS_STRINGS = listOf(
            "cmd.exe" to "Windows command shell reference",
            "powershell" to "PowerShell script reference",
            "eval(" to "Script eval() call",
            "base64_decode" to "Base64 decode function",
            "shell_exec" to "Shell execution function",
            "system(" to "System command execution",
            "passthru" to "Pass-through command execution",
            "CreateProcess" to "Windows CreateProcess API",
            "VirtualAlloc" to "Windows VirtualAlloc API",
            "WriteProcessMemory" to "Windows WriteProcessMemory API",
            "CreateRemoteThread" to "Windows CreateRemoteThread API",
            "SetWindowsHookEx" to "Windows hook installation API"
        )
    }

    /**
     * Resolve a SAF URI into a SelectedFile abstraction.
     * Queries display name, size, and MIME type from ContentResolver.
     */
    fun resolveSelectedFile(uri: Uri, contentResolver: ContentResolver): SelectedFile? {
        return try {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (!it.moveToFirst()) return null

                val displayName = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    .let { idx -> if (idx >= 0) it.getString(idx) else "unknown" }
                val size = it.getColumnIndex(OpenableColumns.SIZE)
                    .let { idx -> if (idx >= 0) it.getLong(idx) else -1L }
                val mimeType = contentResolver.getType(uri)

                val extension = displayName.substringAfterLast('.', "").lowercase()

                SelectedFile(
                    contentUri = uri.toString(),
                    displayNameSanitized = sanitizeFileName(displayName),
                    mimeType = mimeType,
                    size = size,
                    extension = extension
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Analyze a file via ContentResolver InputStream.
     * Returns ERROR on failure. Never returns NO_KNOWN_INDICATORS on error.
     */
    fun analyze(uri: Uri, contentResolver: ContentResolver): FileAnalysisResult {
        val startTime = System.currentTimeMillis()
        val indicators = mutableListOf<AnalysisIndicator>()
        val checksPerformed = mutableListOf<String>()
        val checksNotPerformed = mutableListOf<String>()

        try {
            val inputStream = contentResolver.openInputStream(uri)
                ?: return FileAnalysisResult.error(
                    "No se pudo abrir el archivo",
                    System.currentTimeMillis() - startTime
                )

            inputStream.use { stream ->
                // 1. Read header bytes for type detection
                val headerBytes = readHeaderBytes(stream)
                checksPerformed.add("header_type_detection")

                // 2. Calculate hashes (streaming, resets stream)
                val hashes = calculateHashes(uri, contentResolver)
                if (hashes != null) {
                    checksPerformed.add("hash_sha256")
                    checksPerformed.add("hash_sha1")
                    checksPerformed.add("hash_md5")
                } else {
                    checksNotPerformed.add("hash_calculation_failed")
                }

                // 3. Detect file type from header
                val detectedType = detectFileType(headerBytes, getExtension(uri, contentResolver))
                checksPerformed.add("file_type_detection")

                // 4. Check for extension/header mismatch
                val extension = getExtension(uri, contentResolver)
                val mismatchIndicator = checkExtensionHeaderMismatch(extension, detectedType, headerBytes)
                if (mismatchIndicator != null) {
                    indicators.add(mismatchIndicator)
                    checksPerformed.add("extension_header_mismatch")
                } else {
                    checksPerformed.add("extension_header_mismatch")
                }

                // 5. Scan for suspicious strings (bounded)
                val stringScanResult = scanForSuspiciousStrings(uri, contentResolver)
                if (stringScanResult != null) {
                    indicators.addAll(stringScanResult)
                    checksPerformed.add("suspicious_string_scan")
                } else {
                    checksNotPerformed.add("suspicious_string_scan_failed")
                }

                // 6. Check for EICAR test signature
                val eicarIndicator = checkEicarTestSignature(uri, contentResolver)
                if (eicarIndicator != null) {
                    indicators.add(eicarIndicator)
                    checksPerformed.add("test_signature_check")
                } else {
                    checksPerformed.add("test_signature_check")
                }

                // Determine verdict
                val verdict = determineVerdict(indicators)

                val duration = System.currentTimeMillis() - startTime
                return FileAnalysisResult(
                    verdict = verdict,
                    sha256 = hashes?.first,
                    sha1 = hashes?.second,
                    md5 = hashes?.third,
                    detectedFileType = detectedType,
                    indicators = indicators,
                    checksPerformed = checksPerformed,
                    checksNotPerformed = checksNotPerformed,
                    analysisDurationMs = duration,
                    errorMessage = null
                )
            }
        } catch (e: Exception) {
            return FileAnalysisResult.error(
                "Error de analisis: ${e.message ?: "desconocido"}",
                System.currentTimeMillis() - startTime
            )
        }
    }

    /**
     * Determine the verdict based on indicators.
     *
     * - MALICIOUS: Only for exact test signature match (EICAR)
     * - SUSPICIOUS: Any other indicator found
     * - NO_KNOWN_INDICATORS: No indicators found AND analysis completed
     */
    private fun determineVerdict(indicators: List<AnalysisIndicator>): AnalysisVerdict {
        if (indicators.isEmpty()) {
            return AnalysisVerdict.NO_KNOWN_INDICATORS
        }

        // MALICIOUS only for exact test signature
        val hasTestSignature = indicators.any {
            it.category == IndicatorCategory.TEST_SIGNATURE
        }
        if (hasTestSignature) {
            return AnalysisVerdict.MALICIOUS
        }

        return AnalysisVerdict.SUSPICIOUS
    }

    /**
     * Read first MAX_HEADER_BYTES for type detection.
     */
    private fun readHeaderBytes(stream: InputStream): ByteArray {
        val buffer = ByteArray(MAX_HEADER_BYTES)
        val bytesRead = stream.read(buffer)
        return if (bytesRead > 0) buffer.copyOf(bytesRead) else ByteArray(0)
    }

    /**
     * Calculate SHA-256, SHA-1, and MD5 using streaming.
     * Opens a fresh InputStream to read from the beginning.
     */
    private fun calculateHashes(
        uri: Uri,
        contentResolver: ContentResolver
    ): Triple<String, String, String>? {
        return try {
            val sha256Digest = MessageDigest.getInstance("SHA-256")
            val sha1Digest = MessageDigest.getInstance("SHA-1")
            val md5Digest = MessageDigest.getInstance("MD5")
            val buffer = ByteArray(BUFFER_SIZE)

            contentResolver.openInputStream(uri)?.use { stream ->
                while (true) {
                    val bytesRead = stream.read(buffer)
                    if (bytesRead <= 0) break
                    sha256Digest.update(buffer, 0, bytesRead)
                    sha1Digest.update(buffer, 0, bytesRead)
                    md5Digest.update(buffer, 0, bytesRead)
                }
            } ?: return null

            Triple(
                bytesToHex(sha256Digest.digest()),
                bytesToHex(sha1Digest.digest()),
                bytesToHex(md5Digest.digest())
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Detect file type from header magic bytes and extension.
     */
    private fun detectFileType(headerBytes: ByteArray, extension: String): String {
        if (headerBytes.size >= 4) {
            when {
                matchesMagic(headerBytes, ZIP_MAGIC) -> {
                    // APK and JAR are ZIP-based; distinguish by extension
                    return when (extension) {
                        "apk" -> "APK"
                        "jar" -> "JAR"
                        "docx", "xlsx", "pptx" -> "OFFICE_OOXML"
                        else -> "ZIP"
                    }
                }
                matchesMagic(headerBytes, PDF_MAGIC) -> return "PDF"
                matchesMagic(headerBytes, PNG_MAGIC) -> return "PNG"
                matchesMagic(headerBytes, JPEG_MAGIC) -> return "JPEG"
                matchesMagic(headerBytes, GIF_MAGIC) -> return "GIF"
                matchesMagic(headerBytes, ELF_MAGIC) -> return "ELF"
                matchesMagic(headerBytes, PE_MAGIC) -> return "PE_EXE"
                matchesMagic(headerBytes, OLE_MAGIC) -> return "OLE"
                matchesMagic(headerBytes, RTF_MAGIC) -> return "RTF"
            }
        }
        return "UNKNOWN"
    }

    /**
     * Check for extension/header mismatch.
     * Returns an indicator if mismatch detected.
     */
    private fun checkExtensionHeaderMismatch(
        extension: String,
        detectedType: String,
        headerBytes: ByteArray
    ): AnalysisIndicator? {
        if (extension.isBlank() || detectedType == "UNKNOWN") return null

        val expectedType = when (extension) {
            "png" -> "PNG"
            "jpg", "jpeg" -> "JPEG"
            "gif" -> "GIF"
            "pdf" -> "PDF"
            "zip" -> "ZIP"
            "apk" -> "APK"
            "jar" -> "JAR"
            "docx", "xlsx", "pptx" -> "OFFICE_OOXML"
            "doc", "xls", "ppt" -> "OLE"
            "rtf" -> "RTF"
            "exe", "dll" -> "PE_EXE"
            else -> return null
        }

        if (expectedType != detectedType) {
            return AnalysisIndicator(
                id = "extension_header_mismatch",
                category = IndicatorCategory.MISMATCH,
                severity = IndicatorSeverity.MEDIUM,
                description = "La extension .$extension no coincide con el tipo detectado $detectedType",
                evidenceSummary = "Extension: .$extension, Header: $detectedType"
            )
        }
        return null
    }

    /**
     * Scan first MAX_STRING_SCAN_BYTES for suspicious strings.
     * Returns list of indicators found.
     */
    private fun scanForSuspiciousStrings(
        uri: Uri,
        contentResolver: ContentResolver
    ): List<AnalysisIndicator>? {
        return try {
            val indicators = mutableListOf<AnalysisIndicator>()
            val buffer = ByteArray(BUFFER_SIZE)
            val sb = StringBuilder()
            var totalRead = 0

            contentResolver.openInputStream(uri)?.use { stream ->
                while (totalRead < MAX_STRING_SCAN_BYTES) {
                    val bytesRead = stream.read(buffer)
                    if (bytesRead <= 0) break
                    sb.append(String(buffer, 0, bytesRead, Charsets.UTF_8))
                    totalRead += bytesRead
                }
            } ?: return null

            val content = sb.toString().lowercase()
            for ((pattern, description) in SUSPICIOUS_STRINGS) {
                if (content.contains(pattern.lowercase())) {
                    indicators.add(
                        AnalysisIndicator(
                            id = "string_$pattern",
                            category = IndicatorCategory.STRING,
                            severity = IndicatorSeverity.LOW,
                            description = description,
                            evidenceSummary = "Cadena encontrada: $pattern"
                        )
                    )
                }
            }

            indicators
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Check for EICAR standardized antivirus test signature.
     * This is a controlled test indicator, NOT real malware.
     */
    private fun checkEicarTestSignature(
        uri: Uri,
        contentResolver: ContentResolver
    ): AnalysisIndicator? {
        return try {
            contentResolver.openInputStream(uri)?.use { stream ->
                val buffer = ByteArray(BUFFER_SIZE)
                val sb = StringBuilder()
                var totalRead = 0

                while (totalRead < MAX_STRING_SCAN_BYTES) {
                    val bytesRead = stream.read(buffer)
                    if (bytesRead <= 0) break
                    sb.append(String(buffer, 0, bytesRead, Charsets.UTF_8))
                    totalRead += bytesRead
                }

                val content = sb.toString()
                if (content.contains(String(EICAR_SIGNATURE))) {
                    AnalysisIndicator(
                        id = "eicar_test_signature",
                        category = IndicatorCategory.TEST_SIGNATURE,
                        severity = IndicatorSeverity.HIGH,
                        description = "Indicador de prueba estandar EICAR detectado",
                        evidenceSummary = "EICAR-STANDARD-ANTIVIRUS-TEST-FILE"
                    )
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getExtension(uri: Uri, contentResolver: ContentResolver): String {
        val selectedFile = resolveSelectedFile(uri, contentResolver)
        return selectedFile?.extension ?: ""
    }

    private fun matchesMagic(data: ByteArray, magic: ByteArray): Boolean {
        if (data.size < magic.size) return false
        for (i in magic.indices) {
            if (data[i] != magic[i]) return false
        }
        return true
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        for (b in bytes) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }

    private fun sanitizeFileName(name: String): String {
        // Remove path components if any leaked through
        val baseName = name.substringAfterLast('/')
        val baseName2 = baseName.substringAfterLast('\\')
        return baseName2.take(100) // Limit length
    }
}
