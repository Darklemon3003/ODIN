package com.odin.core.ui.screens.firewall;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000.\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a2\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00052\u0006\u0010\u0007\u001a\u00020\bH\u0007\u001a2\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00062\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001aD\u0010\f\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00062\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0007\u001a\u0012\u0010\u0012\u001a\u00020\u00012\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0007\u00a8\u0006\u0015"}, d2 = {"CreateRuleDialog", "", "onDismiss", "Lkotlin/Function0;", "onCreate", "Lkotlin/Function1;", "Lcom/odin/core/domain/model/FirewallRule;", "isLoading", "", "EditRuleDialog", "rule", "onUpdate", "FirewallRuleItem", "onToggle", "onClick", "onDelete", "modifier", "Landroidx/compose/ui/Modifier;", "FirewallScreen", "viewModel", "Lcom/odin/core/ui/viewmodel/FirewallViewModel;", "app_debug"})
public final class FirewallScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void FirewallScreen(@org.jetbrains.annotations.NotNull()
    com.odin.core.ui.viewmodel.FirewallViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void FirewallRuleItem(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.FirewallRule rule, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onToggle, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDelete, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void CreateRuleDialog(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.odin.core.domain.model.FirewallRule, kotlin.Unit> onCreate, boolean isLoading) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void EditRuleDialog(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.FirewallRule rule, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.odin.core.domain.model.FirewallRule, kotlin.Unit> onUpdate) {
    }
}