package com.odin.core.domain.repository;

/**
 * LogsRepository proporciona acceso a logs de seguridad.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J$\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\u0007\u001a\u00020\bH\u00a6@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u000bH&J\u001c\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u000b2\u0006\u0010\r\u001a\u00020\u000eH&J\u001c\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u000b2\u0006\u0010\u0010\u001a\u00020\u0003H&J\u001c\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u000b2\u0006\u0010\u0012\u001a\u00020\u0013H&J\u001c\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\u0015\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\u0016\u00a8\u0006\u0017"}, d2 = {"Lcom/odin/core/domain/repository/LogsRepository;", "", "exportLogs", "", "logs", "", "Lcom/odin/core/domain/model/LogEvent;", "format", "Lcom/odin/core/domain/repository/ExportFormat;", "(Ljava/util/List;Lcom/odin/core/domain/repository/ExportFormat;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllLogs", "Lkotlinx/coroutines/flow/Flow;", "getLogsBySeverity", "severity", "Lcom/odin/core/domain/model/LogEvent$Severity;", "getLogsBySource", "source", "getRecentEvents", "limit", "", "searchLogs", "query", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface LogsRepository {
    
    /**
     * Obtiene todos los logs.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.domain.model.LogEvent>> getAllLogs();
    
    /**
     * Obtiene logs recientes.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.domain.model.LogEvent>> getRecentEvents(int limit);
    
    /**
     * Obtiene logs filtrados por severidad.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.domain.model.LogEvent>> getLogsBySeverity(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.LogEvent.Severity severity);
    
    /**
     * Obtiene logs filtrados por fuente.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.domain.model.LogEvent>> getLogsBySource(@org.jetbrains.annotations.NotNull()
    java.lang.String source);
    
    /**
     * Exporta logs a un formato específico.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object exportLogs(@org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.domain.model.LogEvent> logs, @org.jetbrains.annotations.NotNull()
    com.odin.core.domain.repository.ExportFormat format, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion);
    
    /**
     * Busca logs por query.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object searchLogs(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.odin.core.domain.model.LogEvent>> $completion);
}