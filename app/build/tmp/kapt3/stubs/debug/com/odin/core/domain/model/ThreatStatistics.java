package com.odin.core.domain.model;

/**
 * ThreatStatistics representa estadísticas de amenazas.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B]\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00030\t\u0012\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u0012\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\u0002\u0010\u000fJ\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003J\u0015\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00030\tH\u00c6\u0003J\u000f\u0010!\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u00c6\u0003J\u000f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u00c6\u0003Jq\u0010#\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\u0014\b\u0002\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00030\t2\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u00c6\u0001J\u0013\u0010$\u001a\u00020%2\b\u0010&\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\'\u001a\u00020\u0003H\u00d6\u0001J\t\u0010(\u001a\u00020\nH\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0011R\u001d\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00030\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0018R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0011\u00a8\u0006)"}, d2 = {"Lcom/odin/core/domain/model/ThreatStatistics;", "", "totalThreats", "", "criticalCount", "highCount", "mediumCount", "lowCount", "threatsByType", "", "", "topSourceIPs", "", "Lcom/odin/core/domain/model/IPThreatCount;", "topDestinationIPs", "(IIIIILjava/util/Map;Ljava/util/List;Ljava/util/List;)V", "getCriticalCount", "()I", "getHighCount", "getLowCount", "getMediumCount", "getThreatsByType", "()Ljava/util/Map;", "getTopDestinationIPs", "()Ljava/util/List;", "getTopSourceIPs", "getTotalThreats", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class ThreatStatistics {
    private final int totalThreats = 0;
    private final int criticalCount = 0;
    private final int highCount = 0;
    private final int mediumCount = 0;
    private final int lowCount = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, java.lang.Integer> threatsByType = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.odin.core.domain.model.IPThreatCount> topSourceIPs = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.odin.core.domain.model.IPThreatCount> topDestinationIPs = null;
    
    public ThreatStatistics(int totalThreats, int criticalCount, int highCount, int mediumCount, int lowCount, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Integer> threatsByType, @org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.domain.model.IPThreatCount> topSourceIPs, @org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.domain.model.IPThreatCount> topDestinationIPs) {
        super();
    }
    
    public final int getTotalThreats() {
        return 0;
    }
    
    public final int getCriticalCount() {
        return 0;
    }
    
    public final int getHighCount() {
        return 0;
    }
    
    public final int getMediumCount() {
        return 0;
    }
    
    public final int getLowCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.Integer> getThreatsByType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.odin.core.domain.model.IPThreatCount> getTopSourceIPs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.odin.core.domain.model.IPThreatCount> getTopDestinationIPs() {
        return null;
    }
    
    public final int component1() {
        return 0;
    }
    
    public final int component2() {
        return 0;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final int component4() {
        return 0;
    }
    
    public final int component5() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.Integer> component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.odin.core.domain.model.IPThreatCount> component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.odin.core.domain.model.IPThreatCount> component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.domain.model.ThreatStatistics copy(int totalThreats, int criticalCount, int highCount, int mediumCount, int lowCount, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Integer> threatsByType, @org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.domain.model.IPThreatCount> topSourceIPs, @org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.domain.model.IPThreatCount> topDestinationIPs) {
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