package com.odin.core.ui.screens.quarantine;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001aR\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0007\u001a(\u0010\u000b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0007\u001a\u0012\u0010\u000f\u001a\u00020\u00012\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0007\u00a8\u0006\u0012"}, d2 = {"QuarantineAppDetailDialog", "", "app", "Lcom/odin/core/domain/model/QuarantinedApp;", "behaviorData", "Lcom/odin/core/domain/model/BehaviorData;", "onDismiss", "Lkotlin/Function0;", "onRelease", "onDelete", "onKeep", "QuarantineAppItem", "onClick", "modifier", "Landroidx/compose/ui/Modifier;", "QuarantineScreen", "viewModel", "Lcom/odin/core/ui/viewmodel/QuarantineViewModel;", "app_debug"})
public final class QuarantineScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void QuarantineScreen(@org.jetbrains.annotations.NotNull()
    com.odin.core.ui.viewmodel.QuarantineViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void QuarantineAppItem(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.QuarantinedApp app, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void QuarantineAppDetailDialog(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.QuarantinedApp app, @org.jetbrains.annotations.Nullable()
    com.odin.core.domain.model.BehaviorData behaviorData, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRelease, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDelete, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onKeep) {
    }
}