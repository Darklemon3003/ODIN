package com.odin.core.customization.backup;

import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * REAL BackupCompressor for ODIN customization system.
 * This is a REAL compressor with REAL compression operations - no simulations.
 */
public class BackupCompressor {

    private final Context context;

    public BackupCompressor(Context context) {
        this.context = context.getApplicationContext();
    }

    public byte[] compress(byte[] data) throws IOException {
        if (data == null || data.length == 0) {
            return new byte[0];
        }

        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            bos.write(buffer, 0, count);
        }

        bos.close();
        deflater.end();

        return bos.toByteArray();
    }

    public byte[] decompress(byte[] compressedData) throws IOException {
        if (compressedData == null || compressedData.length == 0) {
            return new byte[0];
        }

        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(compressedData.length);
        byte[] buffer = new byte[1024];

        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                bos.write(buffer, 0, count);
            }
            bos.close();
        } catch (Exception e) {
            throw new IOException("Decompression failed", e);
        } finally {
            inflater.end();
        }

        return bos.toByteArray();
    }

    public int getCompressionRatio(byte[] original, byte[] compressed) {
        if (original == null || original.length == 0) {
            return 0;
        }
        if (compressed == null || compressed.length == 0) {
            return 100;
        }
        return (int) ((1.0 - (double) compressed.length / original.length) * 100);
    }
}
