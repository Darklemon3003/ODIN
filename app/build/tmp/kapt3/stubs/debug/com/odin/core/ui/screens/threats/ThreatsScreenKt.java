package com.odin.core.ui.screens.threats;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000<\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0007\u0010\b\u001a\u001e\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\rH\u0007\u001a6\u0010\u000e\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0007\u001a\u001a\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0007\u001a\u0012\u0010\u0015\u001a\u00020\u00012\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0007\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0018"}, d2 = {"StatItem", "", "label", "", "value", "color", "Landroidx/compose/ui/graphics/Color;", "StatItem-mxwnekA", "(Ljava/lang/String;Ljava/lang/String;J)V", "ThreatDetailDialog", "threat", "Lcom/odin/core/network/detection/DetectionResult;", "onDismiss", "Lkotlin/Function0;", "ThreatItem", "onClick", "modifier", "Landroidx/compose/ui/Modifier;", "ThreatStatisticsCard", "statistics", "Lcom/odin/core/domain/model/ThreatStatistics;", "ThreatsScreen", "viewModel", "Lcom/odin/core/ui/viewmodel/ThreatsViewModel;", "app_debug"})
public final class ThreatsScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ThreatsScreen(@org.jetbrains.annotations.NotNull()
    com.odin.core.ui.viewmodel.ThreatsViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ThreatStatisticsCard(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.ThreatStatistics statistics, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ThreatItem(@org.jetbrains.annotations.NotNull()
    com.odin.core.network.detection.DetectionResult threat, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ThreatDetailDialog(@org.jetbrains.annotations.NotNull()
    com.odin.core.network.detection.DetectionResult threat, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
}