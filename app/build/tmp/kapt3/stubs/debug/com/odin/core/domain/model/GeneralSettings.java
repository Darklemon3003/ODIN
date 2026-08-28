package com.odin.core.domain.model;

/**
 * GeneralSettings representa configuraciones generales.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0011\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0007H\u00c6\u0003J1\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0016\u001a\u00020\u00072\b\u0010\u0017\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0018\u001a\u00020\u0019H\u00d6\u0001J\t\u0010\u001a\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u001b"}, d2 = {"Lcom/odin/core/domain/model/GeneralSettings;", "", "theme", "Lcom/odin/core/domain/model/ThemeMode;", "language", "", "notificationsEnabled", "", "autoUpdateEnabled", "(Lcom/odin/core/domain/model/ThemeMode;Ljava/lang/String;ZZ)V", "getAutoUpdateEnabled", "()Z", "getLanguage", "()Ljava/lang/String;", "getNotificationsEnabled", "getTheme", "()Lcom/odin/core/domain/model/ThemeMode;", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class GeneralSettings {
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.domain.model.ThemeMode theme = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String language = null;
    private final boolean notificationsEnabled = false;
    private final boolean autoUpdateEnabled = false;
    
    public GeneralSettings(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.ThemeMode theme, @org.jetbrains.annotations.NotNull()
    java.lang.String language, boolean notificationsEnabled, boolean autoUpdateEnabled) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.domain.model.ThemeMode getTheme() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLanguage() {
        return null;
    }
    
    public final boolean getNotificationsEnabled() {
        return false;
    }
    
    public final boolean getAutoUpdateEnabled() {
        return false;
    }
    
    public GeneralSettings() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.domain.model.ThemeMode component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.domain.model.GeneralSettings copy(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.ThemeMode theme, @org.jetbrains.annotations.NotNull()
    java.lang.String language, boolean notificationsEnabled, boolean autoUpdateEnabled) {
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