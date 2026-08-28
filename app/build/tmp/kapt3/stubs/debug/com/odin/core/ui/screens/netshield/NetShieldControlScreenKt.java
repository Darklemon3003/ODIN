package com.odin.core.ui.screens.netshield;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000B\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0003\u001a\u0018\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0003\u001a\u0018\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0003\u001a\u0010\u0010\b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001a\u0010\u0010\t\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001a\u0012\u0010\n\u001a\u00020\u00012\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a\u001e\u0010\u000b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\rH\u0003\u001a\u0010\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001a0\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u0017H\u0003\u001a\u001e\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00010\rH\u0003\u001a\u0017\u0010\u001a\u001a\u00020\u00132\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0002\u00a2\u0006\u0002\u0010\u001d\u001a\f\u0010\u001e\u001a\u00020\u0013*\u00020\u001fH\u0002\u001a\f\u0010\u0016\u001a\u00020\u0017*\u00020\u001fH\u0002\u00a8\u0006 "}, d2 = {"ConnectivityRiskCard", "", "uiState", "Lcom/odin/core/ui/model/NetShieldUiState;", "viewModel", "Lcom/odin/core/ui/viewmodel/NetShieldViewModel;", "ConsentCard", "ControlsCard", "HeaderCard", "MetricsCard", "NetShieldControlScreen", "NotificationCard", "onRequestPermission", "Lkotlin/Function0;", "RuntimeCard", "StatusRow", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "title", "", "message", "status", "tone", "Lcom/odin/core/ui/components/OdinStatusTone;", "VpnAuthorizationCard", "onAuthorize", "formatTime", "timestamp", "", "(Ljava/lang/Long;)Ljava/lang/String;", "label", "Lcom/odin/core/ui/model/NetShieldStatus;", "app_debug"})
public final class NetShieldControlScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void NetShieldControlScreen(@org.jetbrains.annotations.NotNull()
    com.odin.core.ui.viewmodel.NetShieldViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void HeaderCard(com.odin.core.ui.model.NetShieldUiState uiState) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ConsentCard(com.odin.core.ui.model.NetShieldUiState uiState, com.odin.core.ui.viewmodel.NetShieldViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void NotificationCard(com.odin.core.ui.model.NetShieldUiState uiState, kotlin.jvm.functions.Function0<kotlin.Unit> onRequestPermission) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void VpnAuthorizationCard(com.odin.core.ui.model.NetShieldUiState uiState, kotlin.jvm.functions.Function0<kotlin.Unit> onAuthorize) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ConnectivityRiskCard(com.odin.core.ui.model.NetShieldUiState uiState, com.odin.core.ui.viewmodel.NetShieldViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ControlsCard(com.odin.core.ui.model.NetShieldUiState uiState, com.odin.core.ui.viewmodel.NetShieldViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void RuntimeCard(com.odin.core.ui.model.NetShieldUiState uiState) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MetricsCard(com.odin.core.ui.model.NetShieldUiState uiState) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void StatusRow(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String title, java.lang.String message, java.lang.String status, com.odin.core.ui.components.OdinStatusTone tone) {
    }
    
    private static final java.lang.String label(com.odin.core.ui.model.NetShieldStatus $this$label) {
        return null;
    }
    
    private static final com.odin.core.ui.components.OdinStatusTone tone(com.odin.core.ui.model.NetShieldStatus $this$tone) {
        return null;
    }
    
    private static final java.lang.String formatTime(java.lang.Long timestamp) {
        return null;
    }
}