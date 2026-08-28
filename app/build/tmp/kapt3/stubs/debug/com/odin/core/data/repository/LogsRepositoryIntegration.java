package com.odin.core.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0006\b\u0007\u0018\u0000 )2\u00020\u0001:\u0001)B\u0019\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\nJ\u0012\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0015J$\u0010\u0016\u001a\u00020\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0002\u0010\u001bJ\u0014\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0015H\u0016J\u001c\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u00152\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\u001c\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u00152\u0006\u0010!\u001a\u00020\u0017H\u0016J\u001c\u0010\"\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u00152\u0006\u0010#\u001a\u00020$H\u0016J\u0016\u0010%\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010#\u001a\u00020$H\u0002J\u001c\u0010&\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\'\u001a\u00020\u0017H\u0096@\u00a2\u0006\u0002\u0010(R\u001a\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000b\u001a\u00020\f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\r\u0010\u000e\u00a8\u0006*"}, d2 = {"Lcom/odin/core/data/repository/LogsRepositoryIntegration;", "Lcom/odin/core/domain/repository/LogsRepository;", "context", "Landroid/content/Context;", "logExporter", "Lcom/odin/core/forensics/LogExporter;", "(Landroid/content/Context;Lcom/odin/core/forensics/LogExporter;)V", "_logs", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/odin/core/domain/model/LogEvent;", "sentryCore", "Lcom/odin/core/persistence/SentryCore;", "getSentryCore", "()Lcom/odin/core/persistence/SentryCore;", "sentryCore$delegate", "Lkotlin/Lazy;", "addLog", "", "log", "cachedLogs", "Lkotlinx/coroutines/flow/Flow;", "exportLogs", "", "logs", "format", "Lcom/odin/core/domain/repository/ExportFormat;", "(Ljava/util/List;Lcom/odin/core/domain/repository/ExportFormat;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllLogs", "getLogsBySeverity", "severity", "Lcom/odin/core/domain/model/LogEvent$Severity;", "getLogsBySource", "source", "getRecentEvents", "limit", "", "loadRealLogs", "searchLogs", "query", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class LogsRepositoryIntegration implements com.odin.core.domain.repository.LogsRepository {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.forensics.LogExporter logExporter = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.odin.core.domain.model.LogEvent>> _logs = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy sentryCore$delegate = null;
    @java.lang.Deprecated()
    public static final int MAX_VISIBLE_EVENTS = 200;
    @org.jetbrains.annotations.NotNull()
    private static final com.odin.core.data.repository.LogsRepositoryIntegration.Companion Companion = null;
    
    @javax.inject.Inject()
    public LogsRepositoryIntegration(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.odin.core.forensics.LogExporter logExporter) {
        super();
    }
    
    private final com.odin.core.persistence.SentryCore getSentryCore() {
        return null;
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
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object searchLogs(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.odin.core.domain.model.LogEvent>> $completion) {
        return null;
    }
    
    public final void addLog(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.LogEvent log) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.domain.model.LogEvent>> cachedLogs() {
        return null;
    }
    
    private final java.util.List<com.odin.core.domain.model.LogEvent> loadRealLogs(int limit) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/odin/core/data/repository/LogsRepositoryIntegration$Companion;", "", "()V", "MAX_VISIBLE_EVENTS", "", "app_debug"})
    static final class Companion {
        
        private Companion() {
            super();
        }
    }
}