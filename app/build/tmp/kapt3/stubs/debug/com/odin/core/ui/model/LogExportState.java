package com.odin.core.ui.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b7\u0018\u00002\u00020\u0001:\t\u0003\u0004\u0005\u0006\u0007\b\t\n\u000bB\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\t\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u00a8\u0006\u0015"}, d2 = {"Lcom/odin/core/ui/model/LogExportState;", "", "()V", "Cancelled", "Error", "Idle", "NoEvents", "PdfUnavailable", "Preparing", "Success", "WaitingForDestination", "Writing", "Lcom/odin/core/ui/model/LogExportState$Cancelled;", "Lcom/odin/core/ui/model/LogExportState$Error;", "Lcom/odin/core/ui/model/LogExportState$Idle;", "Lcom/odin/core/ui/model/LogExportState$NoEvents;", "Lcom/odin/core/ui/model/LogExportState$PdfUnavailable;", "Lcom/odin/core/ui/model/LogExportState$Preparing;", "Lcom/odin/core/ui/model/LogExportState$Success;", "Lcom/odin/core/ui/model/LogExportState$WaitingForDestination;", "Lcom/odin/core/ui/model/LogExportState$Writing;", "app_debug"})
public abstract class LogExportState {
    
    private LogExportState() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/model/LogExportState$Cancelled;", "Lcom/odin/core/ui/model/LogExportState;", "()V", "app_debug"})
    public static final class Cancelled extends com.odin.core.ui.model.LogExportState {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.model.LogExportState.Cancelled INSTANCE = null;
        
        private Cancelled() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/odin/core/ui/model/LogExportState$Error;", "Lcom/odin/core/ui/model/LogExportState;", "message", "", "(Ljava/lang/String;)V", "getMessage", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
    public static final class Error extends com.odin.core.ui.model.LogExportState {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String message = null;
        
        public Error(@org.jetbrains.annotations.NotNull()
        java.lang.String message) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getMessage() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.odin.core.ui.model.LogExportState.Error copy(@org.jetbrains.annotations.NotNull()
        java.lang.String message) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/model/LogExportState$Idle;", "Lcom/odin/core/ui/model/LogExportState;", "()V", "app_debug"})
    public static final class Idle extends com.odin.core.ui.model.LogExportState {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.model.LogExportState.Idle INSTANCE = null;
        
        private Idle() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/model/LogExportState$NoEvents;", "Lcom/odin/core/ui/model/LogExportState;", "()V", "app_debug"})
    public static final class NoEvents extends com.odin.core.ui.model.LogExportState {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.model.LogExportState.NoEvents INSTANCE = null;
        
        private NoEvents() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/model/LogExportState$PdfUnavailable;", "Lcom/odin/core/ui/model/LogExportState;", "()V", "app_debug"})
    public static final class PdfUnavailable extends com.odin.core.ui.model.LogExportState {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.model.LogExportState.PdfUnavailable INSTANCE = null;
        
        private PdfUnavailable() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/model/LogExportState$Preparing;", "Lcom/odin/core/ui/model/LogExportState;", "()V", "app_debug"})
    public static final class Preparing extends com.odin.core.ui.model.LogExportState {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.model.LogExportState.Preparing INSTANCE = null;
        
        private Preparing() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0005H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0015"}, d2 = {"Lcom/odin/core/ui/model/LogExportState$Success;", "Lcom/odin/core/ui/model/LogExportState;", "format", "Lcom/odin/core/ui/model/LogExportFormat;", "eventCount", "", "(Lcom/odin/core/ui/model/LogExportFormat;I)V", "getEventCount", "()I", "getFormat", "()Lcom/odin/core/ui/model/LogExportFormat;", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "toString", "", "app_debug"})
    public static final class Success extends com.odin.core.ui.model.LogExportState {
        @org.jetbrains.annotations.NotNull()
        private final com.odin.core.ui.model.LogExportFormat format = null;
        private final int eventCount = 0;
        
        public Success(@org.jetbrains.annotations.NotNull()
        com.odin.core.ui.model.LogExportFormat format, int eventCount) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.odin.core.ui.model.LogExportFormat getFormat() {
            return null;
        }
        
        public final int getEventCount() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.odin.core.ui.model.LogExportFormat component1() {
            return null;
        }
        
        public final int component2() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.odin.core.ui.model.LogExportState.Success copy(@org.jetbrains.annotations.NotNull()
        com.odin.core.ui.model.LogExportFormat format, int eventCount) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0015"}, d2 = {"Lcom/odin/core/ui/model/LogExportState$WaitingForDestination;", "Lcom/odin/core/ui/model/LogExportState;", "format", "Lcom/odin/core/ui/model/LogExportFormat;", "fileName", "", "(Lcom/odin/core/ui/model/LogExportFormat;Ljava/lang/String;)V", "getFileName", "()Ljava/lang/String;", "getFormat", "()Lcom/odin/core/ui/model/LogExportFormat;", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
    public static final class WaitingForDestination extends com.odin.core.ui.model.LogExportState {
        @org.jetbrains.annotations.NotNull()
        private final com.odin.core.ui.model.LogExportFormat format = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String fileName = null;
        
        public WaitingForDestination(@org.jetbrains.annotations.NotNull()
        com.odin.core.ui.model.LogExportFormat format, @org.jetbrains.annotations.NotNull()
        java.lang.String fileName) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.odin.core.ui.model.LogExportFormat getFormat() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getFileName() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.odin.core.ui.model.LogExportFormat component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.odin.core.ui.model.LogExportState.WaitingForDestination copy(@org.jetbrains.annotations.NotNull()
        com.odin.core.ui.model.LogExportFormat format, @org.jetbrains.annotations.NotNull()
        java.lang.String fileName) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/model/LogExportState$Writing;", "Lcom/odin/core/ui/model/LogExportState;", "()V", "app_debug"})
    public static final class Writing extends com.odin.core.ui.model.LogExportState {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.model.LogExportState.Writing INSTANCE = null;
        
        private Writing() {
        }
    }
}