package com.odin.core.ui.navigation;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b7\u0018\u00002\u00020\u0001:\r\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001bB\'\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\bR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\f\u0082\u0001\r\u001c\u001d\u001e\u001f !\"#$%&\'(\u00a8\u0006)"}, d2 = {"Lcom/odin/core/ui/navigation/Screen;", "", "route", "", "title", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "selectedIcon", "(Ljava/lang/String;Ljava/lang/String;Landroidx/compose/ui/graphics/vector/ImageVector;Landroidx/compose/ui/graphics/vector/ImageVector;)V", "getIcon", "()Landroidx/compose/ui/graphics/vector/ImageVector;", "getRoute", "()Ljava/lang/String;", "getSelectedIcon", "getTitle", "About", "Dashboard", "Firewall", "Logs", "NetShieldControl", "Network", "Permissions", "Quarantine", "SentryControl", "ServicesStatus", "Settings", "Threats", "Tools", "Lcom/odin/core/ui/navigation/Screen$About;", "Lcom/odin/core/ui/navigation/Screen$Dashboard;", "Lcom/odin/core/ui/navigation/Screen$Firewall;", "Lcom/odin/core/ui/navigation/Screen$Logs;", "Lcom/odin/core/ui/navigation/Screen$NetShieldControl;", "Lcom/odin/core/ui/navigation/Screen$Network;", "Lcom/odin/core/ui/navigation/Screen$Permissions;", "Lcom/odin/core/ui/navigation/Screen$Quarantine;", "Lcom/odin/core/ui/navigation/Screen$SentryControl;", "Lcom/odin/core/ui/navigation/Screen$ServicesStatus;", "Lcom/odin/core/ui/navigation/Screen$Settings;", "Lcom/odin/core/ui/navigation/Screen$Threats;", "Lcom/odin/core/ui/navigation/Screen$Tools;", "app_debug"})
public abstract class Screen {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String route = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.ui.graphics.vector.ImageVector icon = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.ui.graphics.vector.ImageVector selectedIcon = null;
    
    private Screen(java.lang.String route, java.lang.String title, androidx.compose.ui.graphics.vector.ImageVector icon, androidx.compose.ui.graphics.vector.ImageVector selectedIcon) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRoute() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getIcon() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getSelectedIcon() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/navigation/Screen$About;", "Lcom/odin/core/ui/navigation/Screen;", "()V", "app_debug"})
    public static final class About extends com.odin.core.ui.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.navigation.Screen.About INSTANCE = null;
        
        private About() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/navigation/Screen$Dashboard;", "Lcom/odin/core/ui/navigation/Screen;", "()V", "app_debug"})
    public static final class Dashboard extends com.odin.core.ui.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.navigation.Screen.Dashboard INSTANCE = null;
        
        private Dashboard() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/navigation/Screen$Firewall;", "Lcom/odin/core/ui/navigation/Screen;", "()V", "app_debug"})
    public static final class Firewall extends com.odin.core.ui.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.navigation.Screen.Firewall INSTANCE = null;
        
        private Firewall() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/navigation/Screen$Logs;", "Lcom/odin/core/ui/navigation/Screen;", "()V", "app_debug"})
    public static final class Logs extends com.odin.core.ui.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.navigation.Screen.Logs INSTANCE = null;
        
        private Logs() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/navigation/Screen$NetShieldControl;", "Lcom/odin/core/ui/navigation/Screen;", "()V", "app_debug"})
    public static final class NetShieldControl extends com.odin.core.ui.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.navigation.Screen.NetShieldControl INSTANCE = null;
        
        private NetShieldControl() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/navigation/Screen$Network;", "Lcom/odin/core/ui/navigation/Screen;", "()V", "app_debug"})
    public static final class Network extends com.odin.core.ui.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.navigation.Screen.Network INSTANCE = null;
        
        private Network() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/navigation/Screen$Permissions;", "Lcom/odin/core/ui/navigation/Screen;", "()V", "app_debug"})
    public static final class Permissions extends com.odin.core.ui.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.navigation.Screen.Permissions INSTANCE = null;
        
        private Permissions() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/navigation/Screen$Quarantine;", "Lcom/odin/core/ui/navigation/Screen;", "()V", "app_debug"})
    public static final class Quarantine extends com.odin.core.ui.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.navigation.Screen.Quarantine INSTANCE = null;
        
        private Quarantine() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/navigation/Screen$SentryControl;", "Lcom/odin/core/ui/navigation/Screen;", "()V", "app_debug"})
    public static final class SentryControl extends com.odin.core.ui.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.navigation.Screen.SentryControl INSTANCE = null;
        
        private SentryControl() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/navigation/Screen$ServicesStatus;", "Lcom/odin/core/ui/navigation/Screen;", "()V", "app_debug"})
    public static final class ServicesStatus extends com.odin.core.ui.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.navigation.Screen.ServicesStatus INSTANCE = null;
        
        private ServicesStatus() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/navigation/Screen$Settings;", "Lcom/odin/core/ui/navigation/Screen;", "()V", "app_debug"})
    public static final class Settings extends com.odin.core.ui.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.navigation.Screen.Settings INSTANCE = null;
        
        private Settings() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/navigation/Screen$Threats;", "Lcom/odin/core/ui/navigation/Screen;", "()V", "app_debug"})
    public static final class Threats extends com.odin.core.ui.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.navigation.Screen.Threats INSTANCE = null;
        
        private Threats() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/odin/core/ui/navigation/Screen$Tools;", "Lcom/odin/core/ui/navigation/Screen;", "()V", "app_debug"})
    public static final class Tools extends com.odin.core.ui.navigation.Screen {
        @org.jetbrains.annotations.NotNull()
        public static final com.odin.core.ui.navigation.Screen.Tools INSTANCE = null;
        
        private Tools() {
        }
    }
}