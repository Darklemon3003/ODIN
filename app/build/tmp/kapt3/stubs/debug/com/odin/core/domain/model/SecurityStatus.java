package com.odin.core.domain.model;

/**
 * SecurityStatus representa el estado actual de seguridad del sistema.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\tH\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u000bH\u00c6\u0003J;\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u00c6\u0001J\u0013\u0010\u001c\u001a\u00020\u00032\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001e\u001a\u00020\u0007H\u00d6\u0001J\t\u0010\u001f\u001a\u00020 H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0011R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006!"}, d2 = {"Lcom/odin/core/domain/model/SecurityStatus;", "", "isSecure", "", "threatLevel", "Lcom/odin/core/domain/model/ThreatLevel;", "activeThreats", "", "lastUpdateTime", "", "components", "Lcom/odin/core/domain/model/ComponentStatus;", "(ZLcom/odin/core/domain/model/ThreatLevel;IJLcom/odin/core/domain/model/ComponentStatus;)V", "getActiveThreats", "()I", "getComponents", "()Lcom/odin/core/domain/model/ComponentStatus;", "()Z", "getLastUpdateTime", "()J", "getThreatLevel", "()Lcom/odin/core/domain/model/ThreatLevel;", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "toString", "", "app_debug"})
public final class SecurityStatus {
    private final boolean isSecure = false;
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.domain.model.ThreatLevel threatLevel = null;
    private final int activeThreats = 0;
    private final long lastUpdateTime = 0L;
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.domain.model.ComponentStatus components = null;
    
    public SecurityStatus(boolean isSecure, @org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.ThreatLevel threatLevel, int activeThreats, long lastUpdateTime, @org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.ComponentStatus components) {
        super();
    }
    
    public final boolean isSecure() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.domain.model.ThreatLevel getThreatLevel() {
        return null;
    }
    
    public final int getActiveThreats() {
        return 0;
    }
    
    public final long getLastUpdateTime() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.domain.model.ComponentStatus getComponents() {
        return null;
    }
    
    public final boolean component1() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.domain.model.ThreatLevel component2() {
        return null;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final long component4() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.domain.model.ComponentStatus component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.domain.model.SecurityStatus copy(boolean isSecure, @org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.ThreatLevel threatLevel, int activeThreats, long lastUpdateTime, @org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.ComponentStatus components) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}