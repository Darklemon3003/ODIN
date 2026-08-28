package com.odin.core.domain.model;

/**
 * ThreatLevel representa el nivel de amenaza.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0000R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012\u00a8\u0006\u0013"}, d2 = {"Lcom/odin/core/domain/model/ThreatLevel;", "", "displayName", "", "severity", "", "(Ljava/lang/String;ILjava/lang/String;I)V", "getDisplayName", "()Ljava/lang/String;", "getSeverity", "()I", "isAtLeast", "", "level", "SECURE", "LOW", "MEDIUM", "HIGH", "CRITICAL", "app_debug"})
public enum ThreatLevel {
    /*public static final*/ SECURE /* = new SECURE(null, 0) */,
    /*public static final*/ LOW /* = new LOW(null, 0) */,
    /*public static final*/ MEDIUM /* = new MEDIUM(null, 0) */,
    /*public static final*/ HIGH /* = new HIGH(null, 0) */,
    /*public static final*/ CRITICAL /* = new CRITICAL(null, 0) */;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String displayName = null;
    private final int severity = 0;
    
    ThreatLevel(java.lang.String displayName, int severity) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDisplayName() {
        return null;
    }
    
    public final int getSeverity() {
        return 0;
    }
    
    public final boolean isAtLeast(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.ThreatLevel level) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.odin.core.domain.model.ThreatLevel> getEntries() {
        return null;
    }
}