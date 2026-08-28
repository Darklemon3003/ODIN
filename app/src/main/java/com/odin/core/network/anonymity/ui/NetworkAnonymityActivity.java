package com.odin.core.network.anonymity.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.odin.core.R;

/**
 * Main Activity for Network Anonymity system.
 * Enterprise-grade Network Anonymity UI for ODIN.
 */
public class NetworkAnonymityActivity extends AppCompatActivity {
    private static final String TAG = "NetworkAnonymityActivity";

    private NetworkAnonymityViewModel viewModel;
    
    private Button activateButton;
    private Button deactivateButton;
    private TextView overallStatusText;
    private TextView torStatusText;
    private TextView vpnStatusText;
    private TextView routingStatusText;
    private TextView dnsStatusText;
    private TextView ipLeakStatusText;
    private LinearLayout logsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_anonymity);
        
        initializeViewModel();
        initializeViews();
        observeViewModel();
        
        Log.i(TAG, "NetworkAnonymityActivity created");
    }

    /**
     * Initialize ViewModel
     */
    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(NetworkAnonymityViewModel.class);
        
        // In production, set child ViewModels here
        // viewModel.setChildViewModels(torViewModel, vpnViewModel, routingViewModel, dnsViewModel, ipLeakViewModel);
    }

    /**
     * Initialize views
     */
    private void initializeViews() {
        activateButton = findViewById(R.id.activate_anonymity_button);
        deactivateButton = findViewById(R.id.deactivate_anonymity_button);
        overallStatusText = findViewById(R.id.overall_status_text);
        torStatusText = findViewById(R.id.tor_status_text);
        vpnStatusText = findViewById(R.id.vpn_status_text);
        routingStatusText = findViewById(R.id.routing_status_text);
        dnsStatusText = findViewById(R.id.dns_status_text);
        ipLeakStatusText = findViewById(R.id.ip_leak_status_text);
        logsContainer = findViewById(R.id.logs_container);
        
        activateButton.setOnClickListener(v -> {
            viewModel.activateAnonymity();
            Toast.makeText(this, R.string.network_anonymity_activated_toast, Toast.LENGTH_SHORT).show();
        });
        
        deactivateButton.setOnClickListener(v -> {
            viewModel.deactivateAnonymity();
            Toast.makeText(this, R.string.network_anonymity_deactivated_toast, Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Observe ViewModel
     */
    private void observeViewModel() {
        viewModel.isAnonymityActive().observe(this, isActive -> {
            updateUIState(isActive);
        });
        
        viewModel.getTorStatus().observe(this, status -> {
            torStatusText.setText(getString(
                    R.string.tor_status_format,
                    getString(status.isConnected() ? R.string.network_status_connected : R.string.network_status_disconnected)));
        });
        
        viewModel.getVpnStatus().observe(this, status -> {
            vpnStatusText.setText(getString(
                    R.string.vpn_status_format,
                    getString(status.isConnected() ? R.string.network_status_connected_feminine : R.string.network_status_disconnected_feminine)));
        });
        
        viewModel.getRoutingStatus().observe(this, status -> {
            routingStatusText.setText(getString(
                    R.string.routing_status_format,
                    getString(status.isActive() ? R.string.network_status_active : R.string.network_status_inactive)));
        });
        
        viewModel.getDnsStatus().observe(this, status -> {
            dnsStatusText.setText(getString(
                    R.string.dns_status_format,
                    getString(status.isActive() ? R.string.network_status_encrypted : R.string.network_status_standard)));
        });
        
        viewModel.getIPLeakStatus().observe(this, status -> {
            ipLeakStatusText.setText(getString(
                    R.string.ip_leak_status_format,
                    getString(status.hasAnyLeaks() ? R.string.network_status_leaks_detected : R.string.network_status_not_validated)));
        });
        
        viewModel.getAnonymityLogs().observe(this, logs -> {
            updateLogsDisplay(logs);
        });
    }

    /**
     * Update UI state based on anonymity status
     */
    private void updateUIState(boolean isActive) {
        activateButton.setEnabled(!isActive);
        deactivateButton.setEnabled(isActive);
        overallStatusText.setText(getString(R.string.overall_status_format, viewModel.getOverallStatus()));
    }

    /**
     * Update logs display
     */
    private void updateLogsDisplay(java.util.List<com.odin.core.network.anonymity.model.AnonymityLog> logs) {
        logsContainer.removeAllViews();
        
        for (com.odin.core.network.anonymity.model.AnonymityLog log : logs) {
            TextView logView = new TextView(this);
            logView.setText("[" + log.getLevel() + "] " + log.getComponent() + ": " + log.getMessage());
            logsContainer.addView(logView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.updateAllStatuses();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "NetworkAnonymityActivity destroyed");
    }
}
