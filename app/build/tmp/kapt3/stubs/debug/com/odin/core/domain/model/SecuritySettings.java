package com.odin.core.domain.model;

/**
 * SecuritySettings representa configuraciones de seguridad.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0011\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0005H\u00c6\u0003J1\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u0014\u001a\u00020\u00052\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0017H\u00d6\u0001J\t\u0010\u0018\u001a\u00020\u0019H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u001a"}, d2 = {"Lcom/odin/core/domain/model/SecuritySettings;", "", "sensitivityLevel", "Lcom/odin/core/domain/model/SensitivityLevel;", "autoResponseEnabled", "", "realTimeScanning", "heuristicAnalysis", "(Lcom/odin/core/domain/model/SensitivityLevel;ZZZ)V", "getAutoResponseEnabled", "()Z", "getHeuristicAnalysis", "getRealTimeScanning", "getSensitivityLevel", "()Lcom/odin/core/domain/model/SensitivityLevel;", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "", "toString", "", "app_debug"})
public final class SecuritySettings {
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.domain.model.SensitivityLevel sensitivityLevel = null;
    private final boolean autoResponseEnabled = false;
    private final boolean realTimeScanning = false;
    private final boolean heuristicAnalysis = false;
    
    public SecuritySettings(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.SensitivityLevel sensitivityLevel, boolean autoResponseEnabled, boolean realTimeScanning, boolean heuristicAnalysis) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.domain.model.SensitivityLevel getSensitivityLevel() {
        return null;
    }
    
    public final boolean getAutoResponseEnabled() {
        return false;
    }
    
    public final boolean getRealTimeScanning() {
        return false;
    }
    
    public final boolean getHeuristicAnalysis() {
        return false;
    }
    
    public SecuritySettings() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.domain.model.SensitivityLevel component1() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.domain.model.SecuritySettings copy(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.SensitivityLevel sensitivityLevel, boolean autoResponseEnabled, boolean realTimeScanning, boolean heuristicAnalysis) {
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