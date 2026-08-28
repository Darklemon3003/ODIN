package com.odin.core.ui.screens.dashboard;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u0086\u0001\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u001a\u0016\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0003\u001a\u0010\u0010\u0005\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0007H\u0003\u001ab\u0010\b\u001a\u00020\u00012\b\b\u0002\u0010\t\u001a\u00020\n2\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\u000e\b\u0002\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\fH\u0007\u001a\"\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\b\b\u0002\u0010\u0015\u001a\u00020\u0016H\u0003\u001a$\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\u00192\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u00010\u001bH\u0003\u001a*\u0010\u001d\u001a\u00020\u00012\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00190\u00032\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u00010\u001bH\u0003\u001a\u0010\u0010\u001f\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0007H\u0003\u001a*\u0010 \u001a\u00020\u00012\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\"0\u00032\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u00010\u001bH\u0003\u001a,\u0010#\u001a\u00020\u00012\f\u0010$\u001a\b\u0012\u0004\u0012\u00020%0\u00032\u0006\u0010&\u001a\u00020\'2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\fH\u0003\u001a\u0010\u0010(\u001a\u00020\u00012\u0006\u0010)\u001a\u00020%H\u0003\u001a\u0017\u0010*\u001a\u00020\u00132\b\u0010+\u001a\u0004\u0018\u00010,H\u0002\u00a2\u0006\u0002\u0010-\u001aV\u0010.\u001a\u00020\u00012\u0006\u0010/\u001a\u00020\u001c2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\fH\u0002\u001a\f\u00100\u001a\u00020\u0013*\u000201H\u0002\u001a\f\u00102\u001a\u000203*\u00020\u0019H\u0002\u001a\f\u00102\u001a\u000203*\u00020\"H\u0002\u001a\u0011\u00104\u001a\u000205*\u000206H\u0002\u00a2\u0006\u0002\u00107\u001a\u0011\u00104\u001a\u000205*\u000201H\u0002\u00a2\u0006\u0002\u00108\u001a\f\u0010\u0012\u001a\u00020\u0013*\u000201H\u0002\u001a\f\u00109\u001a\u00020:*\u000206H\u0002\u001a\f\u00109\u001a\u00020:*\u000201H\u0002\u00a8\u0006;"}, d2 = {"BlockedFeaturesSection", "", "blockedFeatures", "", "Lcom/odin/core/ui/model/DashboardBlockedFeature;", "DashboardHeader", "uiState", "Lcom/odin/core/ui/model/DashboardUiState;", "DashboardScreen", "viewModel", "Lcom/odin/core/ui/viewmodel/DashboardViewModel;", "onOpenLogs", "Lkotlin/Function0;", "onOpenSettings", "onOpenFirewall", "onOpenSentry", "onOpenPermissions", "HonestMetric", "label", "", "value", "modifier", "Landroidx/compose/ui/Modifier;", "ModuleCard", "module", "Lcom/odin/core/ui/model/DashboardModuleState;", "onAction", "Lkotlin/Function1;", "Lcom/odin/core/ui/model/DashboardAction;", "ModuleSection", "modules", "PostureCard", "QuickActionsCard", "actions", "Lcom/odin/core/ui/model/DashboardQuickAction;", "RecentActivityCard", "events", "Lcom/odin/core/domain/model/LogEvent;", "recentAlertsCount", "", "RecentEventRow", "event", "formatTime", "timestamp", "", "(Ljava/lang/Long;)Ljava/lang/String;", "handleDashboardAction", "action", "description", "Lcom/odin/core/ui/model/OverallStatus;", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "iconColor", "error/NonExistentClass", "Lcom/odin/core/ui/model/ModuleStatus;", "(Lcom/odin/core/ui/model/ModuleStatus;)Lerror/NonExistentClass;", "(Lcom/odin/core/ui/model/OverallStatus;)Lerror/NonExistentClass;", "tone", "Lcom/odin/core/ui/components/OdinStatusTone;", "app_debug"})
public final class DashboardScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void DashboardScreen(@org.jetbrains.annotations.NotNull()
    com.odin.core.ui.viewmodel.DashboardViewModel viewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenLogs, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenFirewall, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSentry, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenPermissions) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DashboardHeader(com.odin.core.ui.model.DashboardUiState uiState) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PostureCard(com.odin.core.ui.model.DashboardUiState uiState) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void HonestMetric(java.lang.String label, java.lang.String value, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ModuleSection(java.util.List<com.odin.core.ui.model.DashboardModuleState> modules, kotlin.jvm.functions.Function1<? super com.odin.core.ui.model.DashboardAction, kotlin.Unit> onAction) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ModuleCard(com.odin.core.ui.model.DashboardModuleState module, kotlin.jvm.functions.Function1<? super com.odin.core.ui.model.DashboardAction, kotlin.Unit> onAction) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void RecentActivityCard(java.util.List<com.odin.core.domain.model.LogEvent> events, int recentAlertsCount, kotlin.jvm.functions.Function0<kotlin.Unit> onOpenLogs) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void RecentEventRow(com.odin.core.domain.model.LogEvent event) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void QuickActionsCard(java.util.List<com.odin.core.ui.model.DashboardQuickAction> actions, kotlin.jvm.functions.Function1<? super com.odin.core.ui.model.DashboardAction, kotlin.Unit> onAction) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void BlockedFeaturesSection(java.util.List<com.odin.core.ui.model.DashboardBlockedFeature> blockedFeatures) {
    }
    
    private static final void handleDashboardAction(com.odin.core.ui.model.DashboardAction action, kotlin.jvm.functions.Function0<kotlin.Unit> onOpenLogs, kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings, kotlin.jvm.functions.Function0<kotlin.Unit> onOpenFirewall, kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSentry, kotlin.jvm.functions.Function0<kotlin.Unit> onOpenPermissions) {
    }
    
    private static final java.lang.String label(com.odin.core.ui.model.OverallStatus $this$label) {
        return null;
    }
    
    private static final java.lang.String description(com.odin.core.ui.model.OverallStatus $this$description) {
        return null;
    }
    
    private static final com.odin.core.ui.components.OdinStatusTone tone(com.odin.core.ui.model.OverallStatus $this$tone) {
        return null;
    }
    
    private static final error.NonExistentClass iconColor(com.odin.core.ui.model.OverallStatus $this$iconColor) {
        return null;
    }
    
    private static final com.odin.core.ui.components.OdinStatusTone tone(com.odin.core.ui.model.ModuleStatus $this$tone) {
        return null;
    }
    
    private static final error.NonExistentClass iconColor(com.odin.core.ui.model.ModuleStatus $this$iconColor) {
        return null;
    }
    
    private static final androidx.compose.ui.graphics.vector.ImageVector icon(com.odin.core.ui.model.DashboardModuleState $this$icon) {
        return null;
    }
    
    private static final androidx.compose.ui.graphics.vector.ImageVector icon(com.odin.core.ui.model.DashboardQuickAction $this$icon) {
        return null;
    }
    
    private static final java.lang.String formatTime(java.lang.Long timestamp) {
        return null;
    }
}