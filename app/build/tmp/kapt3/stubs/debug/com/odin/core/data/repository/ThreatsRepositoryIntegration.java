package com.odin.core.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\nJ\u0016\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010\u0011J\u0014\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0013H\u0016J\u0018\u0010\u0014\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000f\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010\u0011J\u000e\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00070\u0013H\u0016J\u001c\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u00132\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\fH\u0002R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/odin/core/data/repository/ThreatsRepositoryIntegration;", "Lcom/odin/core/domain/repository/ThreatsRepository;", "threatDetectionEngine", "Lcom/odin/core/network/detection/ThreatDetectionEngine;", "(Lcom/odin/core/network/detection/ThreatDetectionEngine;)V", "_statistics", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/odin/core/domain/model/ThreatStatistics;", "_threats", "", "Lcom/odin/core/network/detection/DetectionResult;", "addThreat", "", "threat", "dismissThreat", "forensicsId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllThreats", "Lkotlinx/coroutines/flow/Flow;", "getThreatById", "getThreatStatistics", "getThreatsByLevel", "level", "Lcom/odin/core/network/detection/ThreatLevel;", "updateStatistics", "app_debug"})
public final class ThreatsRepositoryIntegration implements com.odin.core.domain.repository.ThreatsRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.network.detection.ThreatDetectionEngine threatDetectionEngine = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.odin.core.network.detection.DetectionResult>> _threats = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.odin.core.domain.model.ThreatStatistics> _statistics = null;
    
    @javax.inject.Inject()
    public ThreatsRepositoryIntegration(@org.jetbrains.annotations.NotNull()
    com.odin.core.network.detection.ThreatDetectionEngine threatDetectionEngine) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.network.detection.DetectionResult>> getAllThreats() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.network.detection.DetectionResult>> getThreatsByLevel(@org.jetbrains.annotations.NotNull()
    com.odin.core.network.detection.ThreatLevel level) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.odin.core.domain.model.ThreatStatistics> getThreatStatistics() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object dismissThreat(long forensicsId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getThreatById(long forensicsId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.odin.core.network.detection.DetectionResult> $completion) {
        return null;
    }
    
    public final void addThreat(@org.jetbrains.annotations.NotNull()
    com.odin.core.network.detection.DetectionResult threat) {
    }
    
    private final void updateStatistics() {
    }
}