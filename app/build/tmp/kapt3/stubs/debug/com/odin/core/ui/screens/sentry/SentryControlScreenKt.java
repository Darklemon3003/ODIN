package com.odin.core.ui.screens.sentry;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000B\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0003\u001a\u0018\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0003\u001a\u0018\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0003\u001a\u001e\u0010\b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\nH\u0003\u001a\u0010\u0010\u000b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001a\u0012\u0010\f\u001a\u00020\u00012\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a\u0010\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001a0\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u0016H\u0003\u001a\u0017\u0010\u0017\u001a\u00020\u00122\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0002\u00a2\u0006\u0002\u0010\u001a\u001a\f\u0010\u001b\u001a\u00020\u0012*\u00020\u001cH\u0002\u001a\f\u0010\u0015\u001a\u00020\u0016*\u00020\u001cH\u0002\u00a8\u0006\u001d"}, d2 = {"BootConsentCard", "", "uiState", "Lcom/odin/core/ui/model/SentryUiState;", "viewModel", "Lcom/odin/core/ui/viewmodel/SentryViewModel;", "ConsentCard", "ControlsCard", "NotificationCard", "onRequestPermission", "Lkotlin/Function0;", "RuntimeCard", "SentryControlScreen", "SentryHeader", "StatusRow", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "title", "", "message", "status", "tone", "Lcom/odin/core/ui/components/OdinStatusTone;", "formatHeartbeat", "timestamp", "", "(Ljava/lang/Long;)Ljava/lang/String;", "label", "Lcom/odin/core/ui/model/SentryStatus;", "app_debug"})
public final class SentryControlScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void SentryControlScreen(@org.jetbrains.annotations.NotNull()
    com.odin.core.ui.viewmodel.SentryViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SentryHeader(com.odin.core.ui.model.SentryUiState uiState) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ConsentCard(com.odin.core.ui.model.SentryUiState uiState, com.odin.core.ui.viewmodel.SentryViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void NotificationCard(com.odin.core.ui.model.SentryUiState uiState, kotlin.jvm.functions.Function0<kotlin.Unit> onRequestPermission) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void BootConsentCard(com.odin.core.ui.model.SentryUiState uiState, com.odin.core.ui.viewmodel.SentryViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ControlsCard(com.odin.core.ui.model.SentryUiState uiState, com.odin.core.ui.viewmodel.SentryViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void RuntimeCard(com.odin.core.ui.model.SentryUiState uiState) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void StatusRow(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String title, java.lang.String message, java.lang.String status, com.odin.core.ui.components.OdinStatusTone tone) {
    }
    
    private static final java.lang.String label(com.odin.core.ui.model.SentryStatus $this$label) {
        return null;
    }
    
    private static final com.odin.core.ui.components.OdinStatusTone tone(com.odin.core.ui.model.SentryStatus $this$tone) {
        return null;
    }
    
    private static final java.lang.String formatHeartbeat(java.lang.Long timestamp) {
        return null;
    }
}