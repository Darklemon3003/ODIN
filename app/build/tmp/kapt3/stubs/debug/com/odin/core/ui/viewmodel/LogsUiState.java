package com.odin.core.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\'\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u007f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\r\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0013\u00a2\u0006\u0002\u0010\u0014J\t\u0010,\u001a\u00020\u0003H\u00c6\u0003J\t\u0010-\u001a\u00020\u0013H\u00c6\u0003J\u000f\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\u000f\u0010/\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\u000b\u00100\u001a\u0004\u0018\u00010\tH\u00c6\u0003J\u000b\u00101\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\t\u00102\u001a\u00020\rH\u00c6\u0003J\u000b\u00103\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u0010\u00104\u001a\u0004\u0018\u00010\u0010H\u00c6\u0003\u00a2\u0006\u0002\u0010#J\u000b\u00105\u001a\u0004\u0018\u00010\rH\u00c6\u0003J\u0088\u0001\u00106\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\r2\b\b\u0002\u0010\u0012\u001a\u00020\u0013H\u00c6\u0001\u00a2\u0006\u0002\u00107J\u0013\u00108\u001a\u00020\u00032\b\u00109\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010:\u001a\u00020;H\u00d6\u0001J\t\u0010<\u001a\u00020\rH\u00d6\u0001R\u0013\u0010\u0011\u001a\u0004\u0018\u00010\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0012\u001a\u00020\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0018R\u0011\u0010\u001c\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\u001f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001eR\u0011\u0010!\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001eR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u001eR\u0015\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u00a2\u0006\n\n\u0002\u0010$\u001a\u0004\b\"\u0010#R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u0016R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0013\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+\u00a8\u0006="}, d2 = {"Lcom/odin/core/ui/viewmodel/LogsUiState;", "", "isLoading", "", "events", "", "Lcom/odin/core/ui/model/LogEventUi;", "filteredEvents", "selectedSeverity", "Lcom/odin/core/ui/model/LogSeverity;", "selectedModule", "Lcom/odin/core/ui/model/LogModule;", "searchQuery", "", "selectedEvent", "lastRefreshTime", "", "errorMessage", "exportState", "Lcom/odin/core/ui/model/LogExportState;", "(ZLjava/util/List;Ljava/util/List;Lcom/odin/core/ui/model/LogSeverity;Lcom/odin/core/ui/model/LogModule;Ljava/lang/String;Lcom/odin/core/ui/model/LogEventUi;Ljava/lang/Long;Ljava/lang/String;Lcom/odin/core/ui/model/LogExportState;)V", "getErrorMessage", "()Ljava/lang/String;", "getEvents", "()Ljava/util/List;", "getExportState", "()Lcom/odin/core/ui/model/LogExportState;", "getFilteredEvents", "hasEvents", "getHasEvents", "()Z", "hasFilteredEvents", "getHasFilteredEvents", "isFiltered", "getLastRefreshTime", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getSearchQuery", "getSelectedEvent", "()Lcom/odin/core/ui/model/LogEventUi;", "getSelectedModule", "()Lcom/odin/core/ui/model/LogModule;", "getSelectedSeverity", "()Lcom/odin/core/ui/model/LogSeverity;", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(ZLjava/util/List;Ljava/util/List;Lcom/odin/core/ui/model/LogSeverity;Lcom/odin/core/ui/model/LogModule;Ljava/lang/String;Lcom/odin/core/ui/model/LogEventUi;Ljava/lang/Long;Ljava/lang/String;Lcom/odin/core/ui/model/LogExportState;)Lcom/odin/core/ui/viewmodel/LogsUiState;", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class LogsUiState {
    private final boolean isLoading = false;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.odin.core.ui.model.LogEventUi> events = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.odin.core.ui.model.LogEventUi> filteredEvents = null;
    @org.jetbrains.annotations.Nullable()
    private final com.odin.core.ui.model.LogSeverity selectedSeverity = null;
    @org.jetbrains.annotations.Nullable()
    private final com.odin.core.ui.model.LogModule selectedModule = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String searchQuery = null;
    @org.jetbrains.annotations.Nullable()
    private final com.odin.core.ui.model.LogEventUi selectedEvent = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long lastRefreshTime = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String errorMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.ui.model.LogExportState exportState = null;
    private final boolean hasEvents = false;
    private final boolean hasFilteredEvents = false;
    private final boolean isFiltered = false;
    
    public LogsUiState(boolean isLoading, @org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.ui.model.LogEventUi> events, @org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.ui.model.LogEventUi> filteredEvents, @org.jetbrains.annotations.Nullable()
    com.odin.core.ui.model.LogSeverity selectedSeverity, @org.jetbrains.annotations.Nullable()
    com.odin.core.ui.model.LogModule selectedModule, @org.jetbrains.annotations.NotNull()
    java.lang.String searchQuery, @org.jetbrains.annotations.Nullable()
    com.odin.core.ui.model.LogEventUi selectedEvent, @org.jetbrains.annotations.Nullable()
    java.lang.Long lastRefreshTime, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.LogExportState exportState) {
        super();
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.odin.core.ui.model.LogEventUi> getEvents() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.odin.core.ui.model.LogEventUi> getFilteredEvents() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.odin.core.ui.model.LogSeverity getSelectedSeverity() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.odin.core.ui.model.LogModule getSelectedModule() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSearchQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.odin.core.ui.model.LogEventUi getSelectedEvent() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getLastRefreshTime() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErrorMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.LogExportState getExportState() {
        return null;
    }
    
    public final boolean getHasEvents() {
        return false;
    }
    
    public final boolean getHasFilteredEvents() {
        return false;
    }
    
    public final boolean isFiltered() {
        return false;
    }
    
    public LogsUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.LogExportState component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.odin.core.ui.model.LogEventUi> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.odin.core.ui.model.LogEventUi> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.odin.core.ui.model.LogSeverity component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.odin.core.ui.model.LogModule component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.odin.core.ui.model.LogEventUi component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.viewmodel.LogsUiState copy(boolean isLoading, @org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.ui.model.LogEventUi> events, @org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.ui.model.LogEventUi> filteredEvents, @org.jetbrains.annotations.Nullable()
    com.odin.core.ui.model.LogSeverity selectedSeverity, @org.jetbrains.annotations.Nullable()
    com.odin.core.ui.model.LogModule selectedModule, @org.jetbrains.annotations.NotNull()
    java.lang.String searchQuery, @org.jetbrains.annotations.Nullable()
    com.odin.core.ui.model.LogEventUi selectedEvent, @org.jetbrains.annotations.Nullable()
    java.lang.Long lastRefreshTime, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.LogExportState exportState) {
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