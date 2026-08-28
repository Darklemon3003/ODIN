package com.odin.core.customization.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.odin.core.customization.data.dao.AutomationRuleDao;
import com.odin.core.customization.data.dao.AutomationRuleDao_Impl;
import com.odin.core.customization.data.dao.BackupDao;
import com.odin.core.customization.data.dao.BackupDao_Impl;
import com.odin.core.customization.data.dao.NotificationSettingsDao;
import com.odin.core.customization.data.dao.NotificationSettingsDao_Impl;
import com.odin.core.customization.data.dao.ProfileDao;
import com.odin.core.customization.data.dao.ProfileDao_Impl;
import com.odin.core.customization.data.dao.ThemeDao;
import com.odin.core.customization.data.dao.ThemeDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CustomizationDatabase_Impl extends CustomizationDatabase {
  private volatile ThemeDao _themeDao;

  private volatile NotificationSettingsDao _notificationSettingsDao;

  private volatile AutomationRuleDao _automationRuleDao;

  private volatile ProfileDao _profileDao;

  private volatile BackupDao _backupDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `themes` (`themeId` TEXT NOT NULL, `themeName` TEXT, `themeType` TEXT, `colorScheme` TEXT, `fontScheme` TEXT, `iconScheme` TEXT, `isCustom` INTEGER NOT NULL, `createdAt` INTEGER, `updatedAt` INTEGER, PRIMARY KEY(`themeId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `notification_settings` (`settingsId` TEXT NOT NULL, `notificationsEnabled` INTEGER NOT NULL, `soundEnabled` INTEGER NOT NULL, `soundUri` TEXT, `vibrationEnabled` INTEGER NOT NULL, `vibrationPattern` TEXT, `ledEnabled` INTEGER NOT NULL, `ledColor` INTEGER NOT NULL, `ledOnMs` INTEGER NOT NULL, `ledOffMs` INTEGER NOT NULL, `priority` TEXT, `updatedAt` INTEGER, PRIMARY KEY(`settingsId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `automation_rules` (`ruleId` TEXT NOT NULL, `ruleName` TEXT, `enabled` INTEGER NOT NULL, `trigger` TEXT, `conditions` TEXT, `actions` TEXT, `priority` TEXT, `createdAt` INTEGER, `updatedAt` INTEGER, `lastExecuted` INTEGER, PRIMARY KEY(`ruleId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `profiles` (`profileId` TEXT NOT NULL, `profileName` TEXT, `isActive` INTEGER NOT NULL, `theme` TEXT, `notificationSettings` TEXT, `automationRules` TEXT, `customSettings` TEXT, `createdAt` INTEGER, `updatedAt` INTEGER, `activatedAt` INTEGER, PRIMARY KEY(`profileId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `backups` (`backupId` TEXT NOT NULL, `backupName` TEXT, `backupTimestamp` INTEGER, `backupSize` INTEGER NOT NULL, `backupPath` TEXT, `isEncrypted` INTEGER NOT NULL, `encryptionAlgorithm` TEXT, `backupType` TEXT, `status` TEXT, `completedAt` INTEGER, `restoredAt` INTEGER, PRIMARY KEY(`backupId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'd2e92cf824e493a77fc4a1a77b1f323d')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `themes`");
        db.execSQL("DROP TABLE IF EXISTS `notification_settings`");
        db.execSQL("DROP TABLE IF EXISTS `automation_rules`");
        db.execSQL("DROP TABLE IF EXISTS `profiles`");
        db.execSQL("DROP TABLE IF EXISTS `backups`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsThemes = new HashMap<String, TableInfo.Column>(9);
        _columnsThemes.put("themeId", new TableInfo.Column("themeId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThemes.put("themeName", new TableInfo.Column("themeName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThemes.put("themeType", new TableInfo.Column("themeType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThemes.put("colorScheme", new TableInfo.Column("colorScheme", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThemes.put("fontScheme", new TableInfo.Column("fontScheme", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThemes.put("iconScheme", new TableInfo.Column("iconScheme", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThemes.put("isCustom", new TableInfo.Column("isCustom", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThemes.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThemes.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysThemes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesThemes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoThemes = new TableInfo("themes", _columnsThemes, _foreignKeysThemes, _indicesThemes);
        final TableInfo _existingThemes = TableInfo.read(db, "themes");
        if (!_infoThemes.equals(_existingThemes)) {
          return new RoomOpenHelper.ValidationResult(false, "themes(com.odin.core.customization.model.Theme).\n"
                  + " Expected:\n" + _infoThemes + "\n"
                  + " Found:\n" + _existingThemes);
        }
        final HashMap<String, TableInfo.Column> _columnsNotificationSettings = new HashMap<String, TableInfo.Column>(12);
        _columnsNotificationSettings.put("settingsId", new TableInfo.Column("settingsId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationSettings.put("notificationsEnabled", new TableInfo.Column("notificationsEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationSettings.put("soundEnabled", new TableInfo.Column("soundEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationSettings.put("soundUri", new TableInfo.Column("soundUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationSettings.put("vibrationEnabled", new TableInfo.Column("vibrationEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationSettings.put("vibrationPattern", new TableInfo.Column("vibrationPattern", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationSettings.put("ledEnabled", new TableInfo.Column("ledEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationSettings.put("ledColor", new TableInfo.Column("ledColor", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationSettings.put("ledOnMs", new TableInfo.Column("ledOnMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationSettings.put("ledOffMs", new TableInfo.Column("ledOffMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationSettings.put("priority", new TableInfo.Column("priority", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationSettings.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNotificationSettings = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNotificationSettings = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNotificationSettings = new TableInfo("notification_settings", _columnsNotificationSettings, _foreignKeysNotificationSettings, _indicesNotificationSettings);
        final TableInfo _existingNotificationSettings = TableInfo.read(db, "notification_settings");
        if (!_infoNotificationSettings.equals(_existingNotificationSettings)) {
          return new RoomOpenHelper.ValidationResult(false, "notification_settings(com.odin.core.customization.model.NotificationSettings).\n"
                  + " Expected:\n" + _infoNotificationSettings + "\n"
                  + " Found:\n" + _existingNotificationSettings);
        }
        final HashMap<String, TableInfo.Column> _columnsAutomationRules = new HashMap<String, TableInfo.Column>(10);
        _columnsAutomationRules.put("ruleId", new TableInfo.Column("ruleId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("ruleName", new TableInfo.Column("ruleName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("enabled", new TableInfo.Column("enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("trigger", new TableInfo.Column("trigger", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("conditions", new TableInfo.Column("conditions", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("actions", new TableInfo.Column("actions", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("priority", new TableInfo.Column("priority", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("lastExecuted", new TableInfo.Column("lastExecuted", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAutomationRules = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAutomationRules = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAutomationRules = new TableInfo("automation_rules", _columnsAutomationRules, _foreignKeysAutomationRules, _indicesAutomationRules);
        final TableInfo _existingAutomationRules = TableInfo.read(db, "automation_rules");
        if (!_infoAutomationRules.equals(_existingAutomationRules)) {
          return new RoomOpenHelper.ValidationResult(false, "automation_rules(com.odin.core.customization.model.AutomationRule).\n"
                  + " Expected:\n" + _infoAutomationRules + "\n"
                  + " Found:\n" + _existingAutomationRules);
        }
        final HashMap<String, TableInfo.Column> _columnsProfiles = new HashMap<String, TableInfo.Column>(10);
        _columnsProfiles.put("profileId", new TableInfo.Column("profileId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("profileName", new TableInfo.Column("profileName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("theme", new TableInfo.Column("theme", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("notificationSettings", new TableInfo.Column("notificationSettings", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("automationRules", new TableInfo.Column("automationRules", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("customSettings", new TableInfo.Column("customSettings", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("activatedAt", new TableInfo.Column("activatedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProfiles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProfiles = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProfiles = new TableInfo("profiles", _columnsProfiles, _foreignKeysProfiles, _indicesProfiles);
        final TableInfo _existingProfiles = TableInfo.read(db, "profiles");
        if (!_infoProfiles.equals(_existingProfiles)) {
          return new RoomOpenHelper.ValidationResult(false, "profiles(com.odin.core.customization.model.Profile).\n"
                  + " Expected:\n" + _infoProfiles + "\n"
                  + " Found:\n" + _existingProfiles);
        }
        final HashMap<String, TableInfo.Column> _columnsBackups = new HashMap<String, TableInfo.Column>(11);
        _columnsBackups.put("backupId", new TableInfo.Column("backupId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackups.put("backupName", new TableInfo.Column("backupName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackups.put("backupTimestamp", new TableInfo.Column("backupTimestamp", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackups.put("backupSize", new TableInfo.Column("backupSize", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackups.put("backupPath", new TableInfo.Column("backupPath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackups.put("isEncrypted", new TableInfo.Column("isEncrypted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackups.put("encryptionAlgorithm", new TableInfo.Column("encryptionAlgorithm", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackups.put("backupType", new TableInfo.Column("backupType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackups.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackups.put("completedAt", new TableInfo.Column("completedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBackups.put("restoredAt", new TableInfo.Column("restoredAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBackups = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBackups = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBackups = new TableInfo("backups", _columnsBackups, _foreignKeysBackups, _indicesBackups);
        final TableInfo _existingBackups = TableInfo.read(db, "backups");
        if (!_infoBackups.equals(_existingBackups)) {
          return new RoomOpenHelper.ValidationResult(false, "backups(com.odin.core.customization.model.Backup).\n"
                  + " Expected:\n" + _infoBackups + "\n"
                  + " Found:\n" + _existingBackups);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "d2e92cf824e493a77fc4a1a77b1f323d", "c24b989048a5a0627c5cf00bfc560d90");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "themes","notification_settings","automation_rules","profiles","backups");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `themes`");
      _db.execSQL("DELETE FROM `notification_settings`");
      _db.execSQL("DELETE FROM `automation_rules`");
      _db.execSQL("DELETE FROM `profiles`");
      _db.execSQL("DELETE FROM `backups`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ThemeDao.class, ThemeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(NotificationSettingsDao.class, NotificationSettingsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AutomationRuleDao.class, AutomationRuleDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProfileDao.class, ProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BackupDao.class, BackupDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ThemeDao themeDao() {
    if (_themeDao != null) {
      return _themeDao;
    } else {
      synchronized(this) {
        if(_themeDao == null) {
          _themeDao = new ThemeDao_Impl(this);
        }
        return _themeDao;
      }
    }
  }

  @Override
  public NotificationSettingsDao notificationSettingsDao() {
    if (_notificationSettingsDao != null) {
      return _notificationSettingsDao;
    } else {
      synchronized(this) {
        if(_notificationSettingsDao == null) {
          _notificationSettingsDao = new NotificationSettingsDao_Impl(this);
        }
        return _notificationSettingsDao;
      }
    }
  }

  @Override
  public AutomationRuleDao automationRuleDao() {
    if (_automationRuleDao != null) {
      return _automationRuleDao;
    } else {
      synchronized(this) {
        if(_automationRuleDao == null) {
          _automationRuleDao = new AutomationRuleDao_Impl(this);
        }
        return _automationRuleDao;
      }
    }
  }

  @Override
  public ProfileDao profileDao() {
    if (_profileDao != null) {
      return _profileDao;
    } else {
      synchronized(this) {
        if(_profileDao == null) {
          _profileDao = new ProfileDao_Impl(this);
        }
        return _profileDao;
      }
    }
  }

  @Override
  public BackupDao backupDao() {
    if (_backupDao != null) {
      return _backupDao;
    } else {
      synchronized(this) {
        if(_backupDao == null) {
          _backupDao = new BackupDao_Impl(this);
        }
        return _backupDao;
      }
    }
  }
}
