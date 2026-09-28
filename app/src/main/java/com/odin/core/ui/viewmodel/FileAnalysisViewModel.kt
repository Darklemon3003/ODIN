package com.odin.core.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odin.core.domain.model.AnalysisVerdict
import com.odin.core.domain.model.FileAnalysisResult
import com.odin.core.domain.model.QuarantineDeleteResult
import com.odin.core.domain.model.QuarantineItem
import com.odin.core.domain.model.QuarantineResult
import com.odin.core.domain.model.RestoreResult
import com.odin.core.domain.model.SelectedFile
import com.odin.core.domain.repository.FileAnalysisRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for File Analysis screen.
 *
 * Manages UI state for:
 * - File selection via SAF
 * - Analysis execution and results
 * - Quarantine list and operations
 * - Restore via SAF
 */
@HiltViewModel
class FileAnalysisViewModel @Inject constructor(
    private val repository: FileAnalysisRepository
) : ViewModel() {

    sealed class UiState {
        object Idle : UiState()
        data class FileSelected(val file: SelectedFile) : UiState()
        object Analyzing : UiState()
        data class AnalysisComplete(val result: FileAnalysisResult) : UiState()
        data class Error(val message: String) : UiState()
    }

    sealed class QuarantineState {
        object NotQuarantined : QuarantineState()
        object Quarantining : QuarantineState()
        data class QuarantineResult(val result: com.odin.core.domain.model.QuarantineResult) : QuarantineState()
    }

    sealed class RestoreState {
        object NotStarted : RestoreState()
        object Restoring : RestoreState()
        data class RestoreResult(val result: com.odin.core.domain.model.RestoreResult) : RestoreState()
    }

    sealed class DeleteState {
        object NotStarted : DeleteState()
        data class DeleteResult(val result: QuarantineDeleteResult) : DeleteState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _quarantineState = MutableStateFlow<QuarantineState>(QuarantineState.NotQuarantined)
    val quarantineState: StateFlow<QuarantineState> = _quarantineState.asStateFlow()

    private val _restoreState = MutableStateFlow<RestoreState>(RestoreState.NotStarted)
    val restoreState: StateFlow<RestoreState> = _restoreState.asStateFlow()

    private val _deleteState = MutableStateFlow<DeleteState>(DeleteState.NotStarted)
    val deleteState: StateFlow<DeleteState> = _deleteState.asStateFlow()

    private val _quarantineItems = MutableStateFlow<List<QuarantineItem>>(emptyList())
    val quarantineItems: StateFlow<List<QuarantineItem>> = _quarantineItems.asStateFlow()

    private var currentUri: Uri? = null
    private var currentResult: FileAnalysisResult? = null

    fun onFileSelected(uri: Uri) {
        currentUri = uri
        viewModelScope.launch {
            val selectedFile = repository.resolveSelectedFile(uri)
            if (selectedFile != null) {
                _uiState.value = UiState.FileSelected(selectedFile)
            } else {
                _uiState.value = UiState.Error("No se pudo resolver el archivo seleccionado")
            }
        }
    }

    fun analyzeFile() {
        val uri = currentUri ?: return
        viewModelScope.launch {
            _uiState.value = UiState.Analyzing
            _quarantineState.value = QuarantineState.NotQuarantined

            val result = repository.analyzeFile(uri)
            currentResult = result

            _uiState.value = UiState.AnalysisComplete(result)
        }
    }

    fun quarantineCurrentFile() {
        val uri = currentUri ?: return
        val result = currentResult ?: return

        viewModelScope.launch {
            _quarantineState.value = QuarantineState.Quarantining

            val qResult = repository.quarantineFile(uri, result)
            _quarantineState.value = QuarantineState.QuarantineResult(qResult)

            // Refresh quarantine list
            refreshQuarantineItems()
        }
    }

    fun restoreQuarantinedItem(quarantineId: String, destinationUri: Uri) {
        viewModelScope.launch {
            _restoreState.value = RestoreState.Restoring

            val result = repository.restoreQuarantinedItem(quarantineId, destinationUri)
            _restoreState.value = RestoreState.RestoreResult(result)
        }
    }

    fun deleteQuarantinedItem(quarantineId: String) {
        viewModelScope.launch {
            val result = repository.deleteQuarantinedItem(quarantineId)
            _deleteState.value = DeleteState.DeleteResult(result)
            refreshQuarantineItems()
        }
    }

    fun refreshQuarantineItems() {
        viewModelScope.launch {
            _quarantineItems.value = repository.listQuarantinedItems()
        }
    }

    fun clearSelectedFile() {
        currentUri = null
        currentResult = null
        _uiState.value = UiState.Idle
        _quarantineState.value = QuarantineState.NotQuarantined
        _restoreState.value = RestoreState.NotStarted
        _deleteState.value = DeleteState.NotStarted
    }

    fun resetRestoreState() {
        _restoreState.value = RestoreState.NotStarted
    }

    fun resetDeleteState() {
        _deleteState.value = DeleteState.NotStarted
    }
}
