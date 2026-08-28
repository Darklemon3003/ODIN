package com.odin.core.sentry;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\fJ\u0018\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0002J\u0006\u0010\u0011\u001a\u00020\u0012J\u0006\u0010\u0013\u001a\u00020\nJ\u000e\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\fJ\u000e\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0017\u001a\u00020\fJ\u0019\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u0019\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001a\u0010\u001bJ\u0018\u0010\u001c\u001a\u00020\u000f2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\fH\u0002J\u0019\u0010 \u001a\b\u0012\u0004\u0012\u00020\n0\u0019\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b!\u0010\u001bR\u0016\u0010\u0005\u001a\n \u0006*\u0004\u0018\u00010\u00030\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \u0006*\u0004\u0018\u00010\b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\""}, d2 = {"Lcom/odin/core/sentry/SentryController;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "appContext", "kotlin.jvm.PlatformType", "consentManager", "Lcom/odin/core/security/ConsentManager;", "grantConsent", "", "hasNotificationPermission", "", "logEvent", "source", "", "message", "readState", "Lcom/odin/core/ui/model/SentryUiState;", "revokeConsent", "setBootConsent", "enabled", "setNotificationConsent", "granted", "startSentry", "Lkotlin/Result;", "startSentry-d1pmJ48", "()Ljava/lang/Object;", "statusMessage", "status", "Lcom/odin/core/ui/model/SentryStatus;", "bootConsentGranted", "stopSentry", "stopSentry-d1pmJ48", "app_debug"})
public final class SentryController {
    private final android.content.Context appContext = null;
    private final com.odin.core.security.ConsentManager consentManager = null;
    
    public SentryController(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.SentryUiState readState() {
        return null;
    }
    
    public final void grantConsent() {
    }
    
    public final void revokeConsent() {
    }
    
    public final void setBootConsent(boolean enabled) {
    }
    
    public final void setNotificationConsent(boolean granted) {
    }
    
    public final boolean hasNotificationPermission() {
        return false;
    }
    
    private final java.lang.String statusMessage(com.odin.core.ui.model.SentryStatus status, boolean bootConsentGranted) {
        return null;
    }
    
    private final void logEvent(java.lang.String source, java.lang.String message) {
    }
}