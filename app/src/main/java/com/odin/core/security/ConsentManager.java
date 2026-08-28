package com.odin.core.security;

import android.content.Context;
import android.content.SharedPreferences;

public final class ConsentManager {
    public static final String PREFS_NAME = "odin_settings";

    public static final String CONSENT_SENTRY_ENABLED = "consent_sentry_enabled";
    public static final String CONSENT_BOOT_SENTRY_ENABLED = "consent_boot_sentry_enabled";
    public static final String CONSENT_NETSHIELD_VPN_ENABLED = "consent_netshield_vpn_enabled";
    public static final String CONSENT_NOTIFICATIONS_ENABLED = "consent_notifications_enabled";
    public static final String CONSENT_FILE_ACCESS_ENABLED = "consent_file_access_enabled";
    public static final String CONSENT_LOG_EXPORT_ENABLED = "consent_log_export_enabled";
    public static final String CONSENT_QUARANTINE_ACTIONS_ENABLED = "consent_quarantine_actions_enabled";

    private static volatile ConsentManager instance;

    private final SharedPreferences sharedPreferences;

    private ConsentManager(Context context) {
        this.sharedPreferences = context
                .getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static ConsentManager getInstance(Context context) {
        if (instance == null) {
            synchronized (ConsentManager.class) {
                if (instance == null) {
                    instance = new ConsentManager(context);
                }
            }
        }
        return instance;
    }

    public boolean isSentryEnabled() {
        return getBoolean(CONSENT_SENTRY_ENABLED);
    }

    public void setSentryEnabled(boolean enabled) {
        putBoolean(CONSENT_SENTRY_ENABLED, enabled);
    }

    public boolean isBootSentryEnabled() {
        return getBoolean(CONSENT_BOOT_SENTRY_ENABLED);
    }

    public void setBootSentryEnabled(boolean enabled) {
        putBoolean(CONSENT_BOOT_SENTRY_ENABLED, enabled);
    }

    public boolean canStartSentryOnBoot() {
        return isSentryEnabled() && isBootSentryEnabled();
    }

    public boolean isNetShieldVpnEnabled() {
        return getBoolean(CONSENT_NETSHIELD_VPN_ENABLED);
    }

    public void setNetShieldVpnEnabled(boolean enabled) {
        putBoolean(CONSENT_NETSHIELD_VPN_ENABLED, enabled);
    }

    public boolean isNotificationsEnabled() {
        return getBoolean(CONSENT_NOTIFICATIONS_ENABLED);
    }

    public void setNotificationsEnabled(boolean enabled) {
        putBoolean(CONSENT_NOTIFICATIONS_ENABLED, enabled);
    }

    public boolean isFileAccessEnabled() {
        return getBoolean(CONSENT_FILE_ACCESS_ENABLED);
    }

    public void setFileAccessEnabled(boolean enabled) {
        putBoolean(CONSENT_FILE_ACCESS_ENABLED, enabled);
    }

    public boolean isLogExportEnabled() {
        return getBoolean(CONSENT_LOG_EXPORT_ENABLED);
    }

    public void setLogExportEnabled(boolean enabled) {
        putBoolean(CONSENT_LOG_EXPORT_ENABLED, enabled);
    }

    public boolean isQuarantineActionsEnabled() {
        return getBoolean(CONSENT_QUARANTINE_ACTIONS_ENABLED);
    }

    public void setQuarantineActionsEnabled(boolean enabled) {
        putBoolean(CONSENT_QUARANTINE_ACTIONS_ENABLED, enabled);
    }

    private boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    private void putBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }
}
