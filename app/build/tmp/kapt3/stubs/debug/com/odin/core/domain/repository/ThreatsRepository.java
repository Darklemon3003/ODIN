package com.odin.core.domain.repository;

/**
 * ThreatsRepository proporciona acceso a datos de amenazas.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\bH&J\u0018\u0010\u000b\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\bH&J\u001c\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b2\u0006\u0010\u000f\u001a\u00020\u0010H&\u00a8\u0006\u0011"}, d2 = {"Lcom/odin/core/domain/repository/ThreatsRepository;", "", "dismissThreat", "", "forensicsId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllThreats", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/odin/core/network/detection/DetectionResult;", "getThreatById", "getThreatStatistics", "Lcom/odin/core/domain/model/ThreatStatistics;", "getThreatsByLevel", "level", "Lcom/odin/core/network/detection/ThreatLevel;", "app_debug"})
public abstract interface ThreatsRepository {
    
    /**
     * Obtiene todas las amenazas.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.network.detection.DetectionResult>> getAllThreats();
    
    /**
     * Obtiene amenazas por nivel.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.network.detection.DetectionResult>> getThreatsByLevel(@org.jetbrains.annotations.NotNull()
    com.odin.core.network.detection.ThreatLevel level);
    
    /**
     * Obtiene estadísticas de amenazas.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.odin.core.domain.model.ThreatStatistics> getThreatStatistics();
    
    /**
     * Descarta una amenaza.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object dismissThreat(long forensicsId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Obtiene una amenaza por ID.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getThreatById(long forensicsId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.odin.core.network.detection.DetectionResult> $completion);
}