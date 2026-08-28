package com.odin.core.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u000b\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u001a\u001a\u00020\u001bJ\u0006\u0010\u001c\u001a\u00020\u001bJ\u000e\u0010\u001d\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u000bJ\u000e\u0010\u001f\u001a\u00020\u001b2\u0006\u0010 \u001a\u00020\u000fJ\b\u0010!\u001a\u00020\u001bH\u0002J\u000e\u0010\"\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u000bJ\u000e\u0010#\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u000bJ\u000e\u0010$\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u000bJ\u001c\u0010%\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u000f0\u000e2\u0006\u0010\u001e\u001a\u00020\u000bH\u0002R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\r\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u000f0\u000e0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00070\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0012R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00070\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0012R\u001d\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0012R\u0019\u0010\u0016\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0012R#\u0010\u0018\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u000f0\u000e0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0012\u00a8\u0006&"}, d2 = {"Lcom/odin/core/ui/viewmodel/FirewallViewModel;", "Landroidx/lifecycle/ViewModel;", "firewallRepository", "Lcom/odin/core/domain/repository/FirewallRepository;", "(Lcom/odin/core/domain/repository/FirewallRepository;)V", "_isCreatingRule", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_isLoading", "_rules", "", "Lcom/odin/core/domain/model/FirewallRule;", "_selectedRule", "_validationErrors", "", "", "isCreatingRule", "Lkotlinx/coroutines/flow/StateFlow;", "()Lkotlinx/coroutines/flow/StateFlow;", "isLoading", "rules", "getRules", "selectedRule", "getSelectedRule", "validationErrors", "getValidationErrors", "clearSelectedRule", "", "clearValidationErrors", "createRule", "rule", "deleteRule", "ruleId", "loadRules", "selectRule", "toggleRule", "updateRule", "validateRule", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class FirewallViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.domain.repository.FirewallRepository firewallRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.odin.core.domain.model.FirewallRule>> _rules = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.odin.core.domain.model.FirewallRule>> rules = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.odin.core.domain.model.FirewallRule> _selectedRule = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.odin.core.domain.model.FirewallRule> selectedRule = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.Map<java.lang.String, java.lang.String>> _validationErrors = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.String, java.lang.String>> validationErrors = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isLoading = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isLoading = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isCreatingRule = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isCreatingRule = null;
    
    @javax.inject.Inject()
    public FirewallViewModel(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.repository.FirewallRepository firewallRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.odin.core.domain.model.FirewallRule>> getRules() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.odin.core.domain.model.FirewallRule> getSelectedRule() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.String, java.lang.String>> getValidationErrors() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isLoading() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isCreatingRule() {
        return null;
    }
    
    private final void loadRules() {
    }
    
    public final void selectRule(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.FirewallRule rule) {
    }
    
    public final void clearSelectedRule() {
    }
    
    public final void toggleRule(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.FirewallRule rule) {
    }
    
    public final void createRule(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.FirewallRule rule) {
    }
    
    public final void updateRule(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.FirewallRule rule) {
    }
    
    public final void deleteRule(@org.jetbrains.annotations.NotNull()
    java.lang.String ruleId) {
    }
    
    private final java.util.Map<java.lang.String, java.lang.String> validateRule(com.odin.core.domain.model.FirewallRule rule) {
        return null;
    }
    
    public final void clearValidationErrors() {
    }
}