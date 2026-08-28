package com.odin.core.ui.screens.services;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000>\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001aN\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\u0010\b\u0002\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\fH\u0003\u001a<\u0010\r\u001a\u00020\u00012\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0013H\u0007\u001a\f\u0010\u0014\u001a\u00020\u0003*\u00020\u0015H\u0002\u001a\f\u0010\u0014\u001a\u00020\u0003*\u00020\u0016H\u0002\u001a\f\u0010\u0017\u001a\u00020\u0007*\u00020\u0015H\u0002\u001a\f\u0010\u0017\u001a\u00020\u0007*\u00020\u0016H\u0002\u00a8\u0006\u0018"}, d2 = {"ServiceInfoCard", "", "title", "", "description", "status", "tone", "Lcom/odin/core/ui/components/OdinStatusTone;", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "actionLabel", "onAction", "Lkotlin/Function0;", "ServicesStatusScreen", "onOpenSentry", "onOpenNetShield", "sentryViewModel", "Lcom/odin/core/ui/viewmodel/SentryViewModel;", "netShieldViewModel", "Lcom/odin/core/ui/viewmodel/NetShieldViewModel;", "serviceLabel", "Lcom/odin/core/ui/model/NetShieldStatus;", "Lcom/odin/core/ui/model/SentryStatus;", "serviceTone", "app_debug"})
public final class ServicesStatusScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ServicesStatusScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSentry, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenNetShield, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.viewmodel.SentryViewModel sentryViewModel, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.viewmodel.NetShieldViewModel netShieldViewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ServiceInfoCard(java.lang.String title, java.lang.String description, java.lang.String status, com.odin.core.ui.components.OdinStatusTone tone, androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String actionLabel, kotlin.jvm.functions.Function0<kotlin.Unit> onAction) {
    }
    
    private static final java.lang.String serviceLabel(com.odin.core.ui.model.SentryStatus $this$serviceLabel) {
        return null;
    }
    
    private static final com.odin.core.ui.components.OdinStatusTone serviceTone(com.odin.core.ui.model.SentryStatus $this$serviceTone) {
        return null;
    }
    
    private static final java.lang.String serviceLabel(com.odin.core.ui.model.NetShieldStatus $this$serviceLabel) {
        return null;
    }
    
    private static final com.odin.core.ui.components.OdinStatusTone serviceTone(com.odin.core.ui.model.NetShieldStatus $this$serviceTone) {
        return null;
    }
}