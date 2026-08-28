package com.odin.core.netshield;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\t\u001a\u00020\nJ\b\u0010\u000b\u001a\u0004\u0018\u00010\fJ\u0006\u0010\r\u001a\u00020\nJ\u0006\u0010\u000e\u001a\u00020\u000fJ\u0018\u0010\u0010\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0002J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J\u000e\u0010\u0018\u001a\u00020\n2\u0006\u0010\u0019\u001a\u00020\u000fJ\u0006\u0010\u001a\u001a\u00020\u001bJ\u0006\u0010\u001c\u001a\u00020\nJ\u000e\u0010\u001d\u001a\u00020\n2\u0006\u0010\u0019\u001a\u00020\u000fJ\u0019\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\n0\u001f\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b \u0010!J\u0010\u0010\"\u001a\u00020\u00122\u0006\u0010#\u001a\u00020$H\u0002J\u0019\u0010%\u001a\b\u0012\u0004\u0012\u00020\n0\u001f\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b&\u0010!R\u0016\u0010\u0005\u001a\n \u0006*\u0004\u0018\u00010\u00030\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \u0006*\u0004\u0018\u00010\b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\'"}, d2 = {"Lcom/odin/core/netshield/NetShieldController;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "appContext", "kotlin.jvm.PlatformType", "consentManager", "Lcom/odin/core/security/ConsentManager;", "acceptConnectivityRisk", "", "createVpnAuthorizationIntent", "Landroid/content/Intent;", "grantConsent", "hasNotificationPermission", "", "logEvent", "source", "", "message", "metricsFrom", "Lcom/odin/core/ui/model/NetShieldMetrics;", "snapshot", "Lcom/odin/core/netshield/NetShieldRuntimeState$Snapshot;", "onVpnAuthorizationResult", "granted", "readState", "Lcom/odin/core/ui/model/NetShieldUiState;", "revokeConsent", "setNotificationConsent", "startNetShield", "Lkotlin/Result;", "startNetShield-d1pmJ48", "()Ljava/lang/Object;", "statusMessage", "status", "Lcom/odin/core/ui/model/NetShieldStatus;", "stopNetShield", "stopNetShield-d1pmJ48", "app_debug"})
public final class NetShieldController {
    private final android.content.Context appContext = null;
    private final com.odin.core.security.ConsentManager consentManager = null;
    
    public NetShieldController(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.NetShieldUiState readState() {
        return null;
    }
    
    public final void grantConsent() {
    }
    
    public final void revokeConsent() {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.content.Intent createVpnAuthorizationIntent() {
        return null;
    }
    
    public final void onVpnAuthorizationResult(boolean granted) {
    }
    
    public final void setNotificationConsent(boolean granted) {
    }
    
    public final void acceptConnectivityRisk() {
    }
    
    public final boolean hasNotificationPermission() {
        return false;
    }
    
    private final com.odin.core.ui.model.NetShieldMetrics metricsFrom(com.odin.core.netshield.NetShieldRuntimeState.Snapshot snapshot) {
        return null;
    }
    
    private final java.lang.String statusMessage(com.odin.core.ui.model.NetShieldStatus status) {
        return null;
    }
    
    private final void logEvent(java.lang.String source, java.lang.String message) {
    }
}