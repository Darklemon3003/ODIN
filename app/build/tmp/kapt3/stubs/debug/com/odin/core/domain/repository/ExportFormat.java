package com.odin.core.domain.repository;

/**
 * ExportFormat representa el formato de exportación.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/odin/core/domain/repository/ExportFormat;", "", "(Ljava/lang/String;I)V", "PDF", "CSV", "JSON", "app_debug"})
public enum ExportFormat {
    /*public static final*/ PDF /* = new PDF() */,
    /*public static final*/ CSV /* = new CSV() */,
    /*public static final*/ JSON /* = new JSON() */;
    
    ExportFormat() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.odin.core.domain.repository.ExportFormat> getEntries() {
        return null;
    }
}