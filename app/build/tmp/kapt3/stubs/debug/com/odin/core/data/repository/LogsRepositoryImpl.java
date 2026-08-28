package com.odin.core.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J$\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\u000f\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010\u0012\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0002J\u0014\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0014H\u0016J\u001c\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00142\u0006\u0010\u0016\u001a\u00020\u0017H\u0016J\u001c\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00142\u0006\u0010\u0019\u001a\u00020\rH\u0016J\u001c\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00142\u0006\u0010\u001b\u001a\u00020\u001cH\u0016J\b\u0010\u001d\u001a\u00020\u001eH\u0002J\u0006\u0010\u001f\u001a\u00020\u001eJ\u001c\u0010 \u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010!\u001a\u00020\rH\u0096@\u00a2\u0006\u0002\u0010\"R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u000b*\u0004\u0018\u00010\n0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/odin/core/data/repository/LogsRepositoryImpl;", "Lcom/odin/core/domain/repository/LogsRepository;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "_logs", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/odin/core/domain/model/LogEvent;", "sentryCore", "Lcom/odin/core/persistence/SentryCore;", "kotlin.jvm.PlatformType", "exportLogs", "", "logs", "format", "Lcom/odin/core/domain/repository/ExportFormat;", "(Ljava/util/List;Lcom/odin/core/domain/repository/ExportFormat;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "generatePdfExport", "getAllLogs", "Lkotlinx/coroutines/flow/Flow;", "getLogsBySeverity", "severity", "Lcom/odin/core/domain/model/LogEvent$Severity;", "getLogsBySource", "source", "getRecentEvents", "limit", "", "loadRealLogs", "", "refreshLogs", "searchLogs", "query", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class LogsRepositoryImpl implements com.odin.core.domain.repository.LogsRepository {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    private final com.odin.core.persistence.SentryCore sentryCore = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.odin.core.domain.model.LogEvent>> _logs = null;
    
    @javax.inject.Inject()
    public LogsRepositoryImpl(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    private final void loadRealLogs() {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.domain.model.LogEvent>> getAllLogs() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.domain.model.LogEvent>> getRecentEvents(int limit) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.domain.model.LogEvent>> getLogsBySeverity(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.LogEvent.Severity severity) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.domain.model.LogEvent>> getLogsBySource(@org.jetbrains.annotations.NotNull()
    java.lang.String source) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object exportLogs(@org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.domain.model.LogEvent> logs, @org.jetbrains.annotations.NotNull()
    com.odin.core.domain.repository.ExportFormat format, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    private final java.lang.String generatePdfExport(java.util.List<com.odin.core.domain.model.LogEvent> logs) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object searchLogs(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.odin.core.domain.model.LogEvent>> $completion) {
        return null;
    }
    
    public final void refreshLogs() {
    }
}