package com.odin.core.ui.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\f\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0010H\u0002J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J\u0016\u0010\u0018\u001a\u00020\u00102\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00100\u001aH\u0002J\u0018\u0010\u001b\u001a\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u00102\u0006\u0010\u001d\u001a\u00020\u001eH\u0002J\u0010\u0010\u001f\u001a\u00020\u00102\b\u0010 \u001a\u0004\u0018\u00010\u0010J\u0010\u0010!\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u001c\u0010\"\u001a\u00020\u00102\u0012\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00100$H\u0002J\u0010\u0010%\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0010H\u0002J\u0018\u0010&\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00102\u0006\u0010\'\u001a\u00020\u0012H\u0002J\u000e\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020+J\u0014\u0010,\u001a\u00020\u0010*\u00020\u00102\u0006\u0010-\u001a\u00020.H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006/"}, d2 = {"Lcom/odin/core/ui/model/LogSanitizer;", "", "()V", "androidPathPattern", "Lkotlin/text/Regex;", "dateFormat", "Ljava/text/SimpleDateFormat;", "emailPattern", "ipv4Pattern", "payloadPattern", "tokenPattern", "urlPattern", "windowsPathPattern", "isSensitiveKey", "", "key", "", "mapModule", "Lcom/odin/core/ui/model/LogModule;", "source", "mapSeverity", "Lcom/odin/core/ui/model/LogSeverity;", "severity", "Lcom/odin/core/domain/model/LogEvent$Severity;", "maskIp", "groups", "", "maskPath", "path", "separator", "", "sanitize", "raw", "sanitizeKey", "sanitizeMetadata", "metadata", "", "sanitizeSource", "titleFor", "module", "toUi", "Lcom/odin/core/ui/model/LogEventUi;", "event", "Lcom/odin/core/domain/model/LogEvent;", "limit", "maxLength", "", "app_debug"})
public final class LogSanitizer {
    @org.jetbrains.annotations.NotNull()
    private static final java.text.SimpleDateFormat dateFormat = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.text.Regex urlPattern = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.text.Regex tokenPattern = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.text.Regex payloadPattern = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.text.Regex androidPathPattern = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.text.Regex windowsPathPattern = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.text.Regex emailPattern = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.text.Regex ipv4Pattern = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.odin.core.ui.model.LogSanitizer INSTANCE = null;
    
    private LogSanitizer() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.LogEventUi toUi(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.LogEvent event) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String sanitize(@org.jetbrains.annotations.Nullable()
    java.lang.String raw) {
        return null;
    }
    
    private final java.lang.String sanitizeMetadata(java.util.Map<java.lang.String, java.lang.String> metadata) {
        return null;
    }
    
    private final java.lang.String sanitizeKey(java.lang.String key) {
        return null;
    }
    
    private final boolean isSensitiveKey(java.lang.String key) {
        return false;
    }
    
    private final java.lang.String maskIp(java.util.List<java.lang.String> groups) {
        return null;
    }
    
    private final java.lang.String maskPath(java.lang.String path, char separator) {
        return null;
    }
    
    private final java.lang.String titleFor(java.lang.String source, com.odin.core.ui.model.LogModule module) {
        return null;
    }
    
    private final java.lang.String sanitizeSource(java.lang.String source) {
        return null;
    }
    
    private final com.odin.core.ui.model.LogModule mapModule(java.lang.String source) {
        return null;
    }
    
    private final com.odin.core.ui.model.LogSeverity mapSeverity(com.odin.core.domain.model.LogEvent.Severity severity) {
        return null;
    }
    
    private final java.lang.String limit(java.lang.String $this$limit, int maxLength) {
        return null;
    }
}