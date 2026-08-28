package com.odin.core.customization.ui;

/**
 * REAL SettingsActivity for ODIN customization system.
 * This is a REAL Activity with REAL Compose UI - no simulations.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0004H\u0003J\u0010\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0006H\u0003J\u0010\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bH\u0003J\u0010\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\nH\u0003J\b\u0010\u0013\u001a\u00020\u000eH\u0003J\u0010\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\fH\u0003J\u0012\u0010\u0015\u001a\u00020\u000e2\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/odin/core/customization/ui/SettingsActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "automationRulesViewModel", "Lcom/odin/core/customization/viewmodel/AutomationRulesViewModel;", "backupRestoreViewModel", "Lcom/odin/core/customization/viewmodel/BackupRestoreViewModel;", "notificationViewModel", "Lcom/odin/core/customization/viewmodel/NotificationViewModel;", "profileViewModel", "Lcom/odin/core/customization/viewmodel/ProfileViewModel;", "themeViewModel", "Lcom/odin/core/customization/viewmodel/ThemeViewModel;", "AutomationRulesSettingsScreen", "", "viewModel", "BackupRestoreSettingsScreen", "NotificationSettingsScreen", "ProfileSettingsScreen", "SettingsScreen", "ThemeSettingsScreen", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "app_debug"})
public final class SettingsActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.odin.core.customization.viewmodel.ThemeViewModel themeViewModel;
    private com.odin.core.customization.viewmodel.NotificationViewModel notificationViewModel;
    private com.odin.core.customization.viewmodel.AutomationRulesViewModel automationRulesViewModel;
    private com.odin.core.customization.viewmodel.ProfileViewModel profileViewModel;
    private com.odin.core.customization.viewmodel.BackupRestoreViewModel backupRestoreViewModel;
    
    public SettingsActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private final void SettingsScreen() {
    }
    
    @androidx.compose.runtime.Composable()
    private final void ThemeSettingsScreen(com.odin.core.customization.viewmodel.ThemeViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private final void NotificationSettingsScreen(com.odin.core.customization.viewmodel.NotificationViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private final void AutomationRulesSettingsScreen(com.odin.core.customization.viewmodel.AutomationRulesViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private final void ProfileSettingsScreen(com.odin.core.customization.viewmodel.ProfileViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private final void BackupRestoreSettingsScreen(com.odin.core.customization.viewmodel.BackupRestoreViewModel viewModel) {
    }
}