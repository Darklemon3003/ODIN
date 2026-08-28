package com.odin.core.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 32\u00020\u0001:\u000234B\u0019\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0013\u001a\u00020\u00142\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0002J\u0010\u0010\u0016\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\u0016\u0010\u0019\u001a\u00020\u00142\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0002J\u0006\u0010\u001a\u001a\u00020\u001bJ\u0006\u0010\u001c\u001a\u00020\u001bJ\u0006\u0010\u001d\u001a\u00020\u001bJ\u0010\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u001f\u001a\u00020\u0014H\u0002J8\u0010 \u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010!\u001a\u00020\u00142\b\u0010\"\u001a\u0004\u0018\u00010#2\b\u0010$\u001a\u0004\u0018\u00010%H\u0002J\u0006\u0010&\u001a\u00020\u001bJ\u0010\u0010\'\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0017\u001a\u00020\u0018J\u0006\u0010(\u001a\u00020\u001bJ\u000e\u0010)\u001a\u00020\u001b2\u0006\u0010*\u001a\u00020\fJ\u0010\u0010+\u001a\u00020\u001b2\b\u0010$\u001a\u0004\u0018\u00010%J\u0010\u0010,\u001a\u00020\u001b2\b\u0010\"\u001a\u0004\u0018\u00010#J\u0010\u0010-\u001a\u00020\u001b2\u0006\u0010.\u001a\u00020\tH\u0002J\u000e\u0010/\u001a\u00020\u001b2\u0006\u0010!\u001a\u00020\u0014J\u0010\u00100\u001a\u00020\u001b2\b\u00101\u001a\u0004\u0018\u000102R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u00065"}, d2 = {"Lcom/odin/core/ui/viewmodel/LogsViewModel;", "Landroidx/lifecycle/ViewModel;", "context", "Landroid/content/Context;", "logsRepository", "Lcom/odin/core/domain/repository/LogsRepository;", "(Landroid/content/Context;Lcom/odin/core/domain/repository/LogsRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/odin/core/ui/viewmodel/LogsUiState;", "allEvents", "", "Lcom/odin/core/ui/model/LogEventUi;", "pendingExport", "Lcom/odin/core/ui/viewmodel/LogsViewModel$PendingExport;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "buildCsvExport", "", "events", "buildExportFileName", "format", "Lcom/odin/core/ui/model/LogExportFormat;", "buildJsonExport", "clearExportState", "", "clearFilters", "clearSelectedEvent", "csv", "value", "filterEvents", "query", "severity", "Lcom/odin/core/ui/model/LogSeverity;", "module", "Lcom/odin/core/ui/model/LogModule;", "markPdfUnavailable", "prepareExport", "refreshLogs", "selectEvent", "event", "selectModule", "selectSeverity", "updateFilteredState", "state", "updateSearchQuery", "writePendingExport", "destination", "Landroid/net/Uri;", "Companion", "PendingExport", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class LogsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.domain.repository.LogsRepository logsRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.odin.core.ui.viewmodel.LogsUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.odin.core.ui.viewmodel.LogsUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.odin.core.ui.model.LogEventUi> allEvents;
    @org.jetbrains.annotations.Nullable()
    private com.odin.core.ui.viewmodel.LogsViewModel.PendingExport pendingExport;
    @java.lang.Deprecated()
    public static final int MAX_VISIBLE_EVENTS = 200;
    @org.jetbrains.annotations.NotNull()
    private static final java.text.SimpleDateFormat exportDateFormat = null;
    @org.jetbrains.annotations.NotNull()
    private static final com.odin.core.ui.viewmodel.LogsViewModel.Companion Companion = null;
    
    @javax.inject.Inject()
    public LogsViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.odin.core.domain.repository.LogsRepository logsRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.odin.core.ui.viewmodel.LogsUiState> getUiState() {
        return null;
    }
    
    public final void refreshLogs() {
    }
    
    public final void updateSearchQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void selectSeverity(@org.jetbrains.annotations.Nullable()
    com.odin.core.ui.model.LogSeverity severity) {
    }
    
    public final void selectModule(@org.jetbrains.annotations.Nullable()
    com.odin.core.ui.model.LogModule module) {
    }
    
    public final void clearFilters() {
    }
    
    public final void selectEvent(@org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.LogEventUi event) {
    }
    
    public final void clearSelectedEvent() {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String prepareExport(@org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.LogExportFormat format) {
        return null;
    }
    
    public final void markPdfUnavailable() {
    }
    
    public final void writePendingExport(@org.jetbrains.annotations.Nullable()
    android.net.Uri destination) {
    }
    
    public final void clearExportState() {
    }
    
    private final void updateFilteredState(com.odin.core.ui.viewmodel.LogsUiState state) {
    }
    
    private final java.util.List<com.odin.core.ui.model.LogEventUi> filterEvents(java.util.List<com.odin.core.ui.model.LogEventUi> events, java.lang.String query, com.odin.core.ui.model.LogSeverity severity, com.odin.core.ui.model.LogModule module) {
        return null;
    }
    
    private final java.lang.String buildJsonExport(java.util.List<com.odin.core.ui.model.LogEventUi> events) {
        return null;
    }
    
    private final java.lang.String buildCsvExport(java.util.List<com.odin.core.ui.model.LogEventUi> events) {
        return null;
    }
    
    private final java.lang.String csv(java.lang.String value) {
        return null;
    }
    
    private final java.lang.String buildExportFileName(com.odin.core.ui.model.LogExportFormat format) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\t"}, d2 = {"Lcom/odin/core/ui/viewmodel/LogsViewModel$Companion;", "", "()V", "MAX_VISIBLE_EVENTS", "", "exportDateFormat", "Ljava/text/SimpleDateFormat;", "getExportDateFormat", "()Ljava/text/SimpleDateFormat;", "app_debug"})
    static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.text.SimpleDateFormat getExportDateFormat() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0082\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\u0007H\u00c6\u0003J\'\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0007H\u00d6\u0001J\t\u0010\u0017\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0018"}, d2 = {"Lcom/odin/core/ui/viewmodel/LogsViewModel$PendingExport;", "", "format", "Lcom/odin/core/ui/model/LogExportFormat;", "content", "", "eventCount", "", "(Lcom/odin/core/ui/model/LogExportFormat;Ljava/lang/String;I)V", "getContent", "()Ljava/lang/String;", "getEventCount", "()I", "getFormat", "()Lcom/odin/core/ui/model/LogExportFormat;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
    static final class PendingExport {
        @org.jetbrains.annotations.NotNull()
        private final com.odin.core.ui.model.LogExportFormat format = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String content = null;
        private final int eventCount = 0;
        
        public PendingExport(@org.jetbrains.annotations.NotNull()
        com.odin.core.ui.model.LogExportFormat format, @org.jetbrains.annotations.NotNull()
        java.lang.String content, int eventCount) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.odin.core.ui.model.LogExportFormat getFormat() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getContent() {
            return null;
        }
        
        public final int getEventCount() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.odin.core.ui.model.LogExportFormat component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }
        
        public final int component3() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.odin.core.ui.viewmodel.LogsViewModel.PendingExport copy(@org.jetbrains.annotations.NotNull()
        com.odin.core.ui.model.LogExportFormat format, @org.jetbrains.annotations.NotNull()
        java.lang.String content, int eventCount) {
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
}