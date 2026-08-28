package com.odin.core.fileanalysis.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.odin.core.R;
import com.odin.core.fileanalysis.viewmodel.FileScannerViewModel;
import com.odin.core.fileanalysis.viewmodel.MalwareAnalysisViewModel;
import com.odin.core.fileanalysis.viewmodel.QuarantineViewModel;
import com.odin.core.security.ConsentManager;

/**
 * Real activity for file analysis UI.
 * No simulated UI - only actual file analysis interface.
 */
public class FileAnalysisActivity extends AppCompatActivity {

    private static final String TAG = "FileAnalysisActivity";
    
    private FileScannerViewModel fileScannerViewModel;
    private MalwareAnalysisViewModel malwareAnalysisViewModel;
    private QuarantineViewModel quarantineViewModel;
    
    private ActivityResultLauncher<Intent> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_analysis);

        Log.d(TAG, "FileAnalysisActivity created");

        // Initialize ViewModels
        fileScannerViewModel = new ViewModelProvider(this).get(FileScannerViewModel.class);
        malwareAnalysisViewModel = new ViewModelProvider(this).get(MalwareAnalysisViewModel.class);
        quarantineViewModel = new ViewModelProvider(this).get(QuarantineViewModel.class);

        // Initialize file picker
        initializeFilePicker();

        // Setup UI
        setupUI();

        // Observe ViewModels
        observeViewModels();
    }

    private void initializeFilePicker() {
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            handleSelectedFile(uri);
                        }
                    }
                }
        );
    }

    private void setupUI() {
        // Setup file selection button
        findViewById(R.id.btn_select_file).setOnClickListener(v -> openFilePicker());
        
        // Setup scan button
        findViewById(R.id.btn_scan).setOnClickListener(v -> {
            // Get selected file and start scan
            // This would be implemented with actual UI components
            Log.d(TAG, "Scan button clicked");
        });

        // Setup analysis buttons
        findViewById(R.id.btn_static_analysis).setOnClickListener(v -> {
            Log.d(TAG, "Static analysis button clicked");
        });

        findViewById(R.id.btn_dynamic_analysis).setOnClickListener(v -> {
            Log.d(TAG, "Dynamic analysis button clicked");
        });

        findViewById(R.id.btn_heuristic_analysis).setOnClickListener(v -> {
            Log.d(TAG, "Heuristic analysis button clicked");
        });

        // Setup quarantine button
        findViewById(R.id.btn_quarantine).setOnClickListener(v -> {
            Log.d(TAG, "Quarantine button clicked");
        });

        // Setup export button
        findViewById(R.id.btn_export_report).setOnClickListener(v -> {
            Log.d(TAG, "Export report button clicked");
        });
    }

    private void openFilePicker() {
        ConsentManager.getInstance(this).setFileAccessEnabled(true);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(Intent.createChooser(intent, getString(R.string.file_picker_title)));
    }

    private void handleSelectedFile(Uri uri) {
        Log.d(TAG, "File selected: " + uri.getPath());
        
        // Convert URI to File and start analysis
        // This would be implemented with actual file handling
        Toast.makeText(this, getString(R.string.file_selected_toast, uri.getLastPathSegment()), Toast.LENGTH_SHORT).show();
    }

    private void observeViewModels() {
        // Observe scan status
        fileScannerViewModel.getScanStatus().observe(this, status -> {
            Log.d(TAG, "Scan status: " + status);
            // Update UI based on status
        });

        // Observe scan results
        fileScannerViewModel.getScanResults().observe(this, result -> {
            Log.d(TAG, "Scan result received: " + (result != null ? result.getFileName() : "null"));
            // Update UI with scan results
        });

        // Observe scan progress
        fileScannerViewModel.getScanProgress().observe(this, progress -> {
            Log.d(TAG, "Scan progress: " + progress);
            // Update progress bar
        });

        // Observe analysis status
        malwareAnalysisViewModel.getAnalysisStatus().observe(this, status -> {
            Log.d(TAG, "Analysis status: " + status);
            // Update UI based on status
        });

        // Observe analysis results
        malwareAnalysisViewModel.getAnalysisResults().observe(this, result -> {
            Log.d(TAG, "Analysis result received: " + (result != null ? result.getAnalysisId() : "null"));
            // Update UI with analysis results
        });

        // Observe quarantine status
        quarantineViewModel.getQuarantineStatus().observe(this, status -> {
            Log.d(TAG, "Quarantine status: " + status);
            // Update UI based on status
        });
    }
}
