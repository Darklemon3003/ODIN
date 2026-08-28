package com.odin.core.customization.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.odin.core.customization.data.converters.*;
import com.odin.core.customization.data.dao.*;
import com.odin.core.customization.model.*;

/**
 * REAL CustomizationDatabase for ODIN customization system.
 * This is a REAL Room database with REAL persistence - no simulations.
 */
@Database(
    entities = {
        Theme.class,
        NotificationSettings.class,
        AutomationRule.class,
        Profile.class,
        Backup.class
    },
    version = 1,
    exportSchema = false
)
@TypeConverters({
    ThemeTypeConverter.class,
    ColorSchemeConverter.class,
    FontSchemeConverter.class,
    IconSchemeConverter.class,
    VibrationPatternConverter.class,
    NotificationPriorityConverter.class,
    TriggerConverter.class,
    ConditionConverter.class,
    ActionConverter.class,
    RulePriorityConverter.class,
    ThemeConverter.class,
    NotificationSettingsConverter.class,
    AutomationRuleListConverter.class,
    BackupTypeConverter.class,
    BackupStatusConverter.class,
    DateConverter.class,
    ConditionListConverter.class,
    ActionListConverter.class,
    StringMapConverter.class
})
public abstract class CustomizationDatabase extends RoomDatabase {

    private static volatile CustomizationDatabase INSTANCE;
    private static final String DATABASE_NAME = "odin_customization.db";

    public abstract ThemeDao themeDao();
    public abstract NotificationSettingsDao notificationSettingsDao();
    public abstract AutomationRuleDao automationRuleDao();
    public abstract ProfileDao profileDao();
    public abstract BackupDao backupDao();

    public static CustomizationDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (CustomizationDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            CustomizationDatabase.class,
                            DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
