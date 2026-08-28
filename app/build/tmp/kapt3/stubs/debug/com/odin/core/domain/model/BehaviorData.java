package com.odin.core.domain.model;

/**
 * BehaviorData representa datos de comportamiento de una aplicación.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B3\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\nH\u00c6\u0003J\t\u0010\u001c\u001a\u00020\fH\u00c6\u0003JA\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\fH\u00c6\u0001J\u0013\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010!\u001a\u00020\u0005H\u00d6\u0001J\t\u0010\"\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006#"}, d2 = {"Lcom/odin/core/domain/model/BehaviorData;", "", "packageName", "", "networkRequests", "", "suspiciousActivities", "", "Lcom/odin/core/domain/model/SuspiciousActivity;", "resourceUsage", "Lcom/odin/core/domain/model/ResourceUsage;", "lastUpdated", "", "(Ljava/lang/String;ILjava/util/List;Lcom/odin/core/domain/model/ResourceUsage;J)V", "getLastUpdated", "()J", "getNetworkRequests", "()I", "getPackageName", "()Ljava/lang/String;", "getResourceUsage", "()Lcom/odin/core/domain/model/ResourceUsage;", "getSuspiciousActivities", "()Ljava/util/List;", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class BehaviorData {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String packageName = null;
    private final int networkRequests = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.odin.core.domain.model.SuspiciousActivity> suspiciousActivities = null;
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.domain.model.ResourceUsage resourceUsage = null;
    private final long lastUpdated = 0L;
    
    public BehaviorData(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, int networkRequests, @org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.domain.model.SuspiciousActivity> suspiciousActivities, @org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.ResourceUsage resourceUsage, long lastUpdated) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPackageName() {
        return null;
    }
    
    public final int getNetworkRequests() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.odin.core.domain.model.SuspiciousActivity> getSuspiciousActivities() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.domain.model.ResourceUsage getResourceUsage() {
        return null;
    }
    
    public final long getLastUpdated() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final int component2() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.odin.core.domain.model.SuspiciousActivity> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.domain.model.ResourceUsage component4() {
        return null;
    }
    
    public final long component5() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.domain.model.BehaviorData copy(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, int networkRequests, @org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.domain.model.SuspiciousActivity> suspiciousActivities, @org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.ResourceUsage resourceUsage, long lastUpdated) {
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