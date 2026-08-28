package com.odin.core.customization.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.odin.core.customization.data.converters.AutomationRuleListConverter;
import com.odin.core.customization.data.converters.DateConverter;
import com.odin.core.customization.data.converters.NotificationSettingsConverter;
import com.odin.core.customization.data.converters.StringMapConverter;
import com.odin.core.customization.data.converters.ThemeConverter;
import com.odin.core.customization.model.AutomationRule;
import com.odin.core.customization.model.NotificationSettings;
import com.odin.core.customization.model.Profile;
import com.odin.core.customization.model.Theme;
import java.lang.Class;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ProfileDao_Impl implements ProfileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Profile> __insertionAdapterOfProfile;

  private final EntityDeletionOrUpdateAdapter<Profile> __deletionAdapterOfProfile;

  private final EntityDeletionOrUpdateAdapter<Profile> __updateAdapterOfProfile;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public ProfileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProfile = new EntityInsertionAdapter<Profile>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `profiles` (`profileId`,`profileName`,`isActive`,`theme`,`notificationSettings`,`automationRules`,`customSettings`,`createdAt`,`updatedAt`,`activatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Profile entity) {
        if (entity.getProfileId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getProfileId());
        }
        if (entity.getProfileName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getProfileName());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(3, _tmp);
        final String _tmp_1 = ThemeConverter.toString(entity.getTheme());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_1);
        }
        final String _tmp_2 = NotificationSettingsConverter.toString(entity.getNotificationSettings());
        if (_tmp_2 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_2);
        }
        final String _tmp_3 = AutomationRuleListConverter.toString(entity.getAutomationRules());
        if (_tmp_3 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_3);
        }
        final String _tmp_4 = StringMapConverter.fromStringMap(entity.getCustomSettings());
        if (_tmp_4 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_4);
        }
        final Long _tmp_5 = DateConverter.fromDate(entity.getCreatedAt());
        if (_tmp_5 == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, _tmp_5);
        }
        final Long _tmp_6 = DateConverter.fromDate(entity.getUpdatedAt());
        if (_tmp_6 == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, _tmp_6);
        }
        final Long _tmp_7 = DateConverter.fromDate(entity.getActivatedAt());
        if (_tmp_7 == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, _tmp_7);
        }
      }
    };
    this.__deletionAdapterOfProfile = new EntityDeletionOrUpdateAdapter<Profile>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `profiles` WHERE `profileId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Profile entity) {
        if (entity.getProfileId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getProfileId());
        }
      }
    };
    this.__updateAdapterOfProfile = new EntityDeletionOrUpdateAdapter<Profile>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `profiles` SET `profileId` = ?,`profileName` = ?,`isActive` = ?,`theme` = ?,`notificationSettings` = ?,`automationRules` = ?,`customSettings` = ?,`createdAt` = ?,`updatedAt` = ?,`activatedAt` = ? WHERE `profileId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Profile entity) {
        if (entity.getProfileId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getProfileId());
        }
        if (entity.getProfileName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getProfileName());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(3, _tmp);
        final String _tmp_1 = ThemeConverter.toString(entity.getTheme());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_1);
        }
        final String _tmp_2 = NotificationSettingsConverter.toString(entity.getNotificationSettings());
        if (_tmp_2 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_2);
        }
        final String _tmp_3 = AutomationRuleListConverter.toString(entity.getAutomationRules());
        if (_tmp_3 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_3);
        }
        final String _tmp_4 = StringMapConverter.fromStringMap(entity.getCustomSettings());
        if (_tmp_4 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_4);
        }
        final Long _tmp_5 = DateConverter.fromDate(entity.getCreatedAt());
        if (_tmp_5 == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, _tmp_5);
        }
        final Long _tmp_6 = DateConverter.fromDate(entity.getUpdatedAt());
        if (_tmp_6 == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, _tmp_6);
        }
        final Long _tmp_7 = DateConverter.fromDate(entity.getActivatedAt());
        if (_tmp_7 == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, _tmp_7);
        }
        if (entity.getProfileId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getProfileId());
        }
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM profiles WHERE profileId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM profiles";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Profile profile) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfProfile.insert(profile);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final List<Profile> profiles) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfProfile.insert(profiles);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Profile profile) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfProfile.handle(profile);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Profile profile) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfProfile.handle(profile);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteById(final String profileId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
    int _argIndex = 1;
    if (profileId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, profileId);
    }
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteById.release(_stmt);
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public Profile getById(final String profileId) {
    final String _sql = "SELECT * FROM profiles WHERE profileId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (profileId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, profileId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profileId");
      final int _cursorIndexOfProfileName = CursorUtil.getColumnIndexOrThrow(_cursor, "profileName");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfTheme = CursorUtil.getColumnIndexOrThrow(_cursor, "theme");
      final int _cursorIndexOfNotificationSettings = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationSettings");
      final int _cursorIndexOfAutomationRules = CursorUtil.getColumnIndexOrThrow(_cursor, "automationRules");
      final int _cursorIndexOfCustomSettings = CursorUtil.getColumnIndexOrThrow(_cursor, "customSettings");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final int _cursorIndexOfActivatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "activatedAt");
      final Profile _result;
      if (_cursor.moveToFirst()) {
        _result = new Profile();
        final String _tmpProfileId;
        if (_cursor.isNull(_cursorIndexOfProfileId)) {
          _tmpProfileId = null;
        } else {
          _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
        }
        _result.setProfileId(_tmpProfileId);
        final String _tmpProfileName;
        if (_cursor.isNull(_cursorIndexOfProfileName)) {
          _tmpProfileName = null;
        } else {
          _tmpProfileName = _cursor.getString(_cursorIndexOfProfileName);
        }
        _result.setProfileName(_tmpProfileName);
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        _result.setActive(_tmpIsActive);
        final Theme _tmpTheme;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfTheme)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfTheme);
        }
        _tmpTheme = ThemeConverter.fromString(_tmp_1);
        _result.setTheme(_tmpTheme);
        final NotificationSettings _tmpNotificationSettings;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfNotificationSettings)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfNotificationSettings);
        }
        _tmpNotificationSettings = NotificationSettingsConverter.fromString(_tmp_2);
        _result.setNotificationSettings(_tmpNotificationSettings);
        final List<AutomationRule> _tmpAutomationRules;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfAutomationRules)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfAutomationRules);
        }
        _tmpAutomationRules = AutomationRuleListConverter.fromString(_tmp_3);
        _result.setAutomationRules(_tmpAutomationRules);
        final Map<String, Object> _tmpCustomSettings;
        final String _tmp_4;
        if (_cursor.isNull(_cursorIndexOfCustomSettings)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getString(_cursorIndexOfCustomSettings);
        }
        _tmpCustomSettings = StringMapConverter.toStringMap(_tmp_4);
        _result.setCustomSettings(_tmpCustomSettings);
        final Date _tmpCreatedAt;
        final Long _tmp_5;
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _tmp_5 = null;
        } else {
          _tmp_5 = _cursor.getLong(_cursorIndexOfCreatedAt);
        }
        _tmpCreatedAt = DateConverter.toDate(_tmp_5);
        _result.setCreatedAt(_tmpCreatedAt);
        final Date _tmpUpdatedAt;
        final Long _tmp_6;
        if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
          _tmp_6 = null;
        } else {
          _tmp_6 = _cursor.getLong(_cursorIndexOfUpdatedAt);
        }
        _tmpUpdatedAt = DateConverter.toDate(_tmp_6);
        _result.setUpdatedAt(_tmpUpdatedAt);
        final Date _tmpActivatedAt;
        final Long _tmp_7;
        if (_cursor.isNull(_cursorIndexOfActivatedAt)) {
          _tmp_7 = null;
        } else {
          _tmp_7 = _cursor.getLong(_cursorIndexOfActivatedAt);
        }
        _tmpActivatedAt = DateConverter.toDate(_tmp_7);
        _result.setActivatedAt(_tmpActivatedAt);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Profile> getAll() {
    final String _sql = "SELECT * FROM profiles";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profileId");
      final int _cursorIndexOfProfileName = CursorUtil.getColumnIndexOrThrow(_cursor, "profileName");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfTheme = CursorUtil.getColumnIndexOrThrow(_cursor, "theme");
      final int _cursorIndexOfNotificationSettings = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationSettings");
      final int _cursorIndexOfAutomationRules = CursorUtil.getColumnIndexOrThrow(_cursor, "automationRules");
      final int _cursorIndexOfCustomSettings = CursorUtil.getColumnIndexOrThrow(_cursor, "customSettings");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final int _cursorIndexOfActivatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "activatedAt");
      final List<Profile> _result = new ArrayList<Profile>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Profile _item;
        _item = new Profile();
        final String _tmpProfileId;
        if (_cursor.isNull(_cursorIndexOfProfileId)) {
          _tmpProfileId = null;
        } else {
          _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
        }
        _item.setProfileId(_tmpProfileId);
        final String _tmpProfileName;
        if (_cursor.isNull(_cursorIndexOfProfileName)) {
          _tmpProfileName = null;
        } else {
          _tmpProfileName = _cursor.getString(_cursorIndexOfProfileName);
        }
        _item.setProfileName(_tmpProfileName);
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        _item.setActive(_tmpIsActive);
        final Theme _tmpTheme;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfTheme)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfTheme);
        }
        _tmpTheme = ThemeConverter.fromString(_tmp_1);
        _item.setTheme(_tmpTheme);
        final NotificationSettings _tmpNotificationSettings;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfNotificationSettings)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfNotificationSettings);
        }
        _tmpNotificationSettings = NotificationSettingsConverter.fromString(_tmp_2);
        _item.setNotificationSettings(_tmpNotificationSettings);
        final List<AutomationRule> _tmpAutomationRules;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfAutomationRules)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfAutomationRules);
        }
        _tmpAutomationRules = AutomationRuleListConverter.fromString(_tmp_3);
        _item.setAutomationRules(_tmpAutomationRules);
        final Map<String, Object> _tmpCustomSettings;
        final String _tmp_4;
        if (_cursor.isNull(_cursorIndexOfCustomSettings)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getString(_cursorIndexOfCustomSettings);
        }
        _tmpCustomSettings = StringMapConverter.toStringMap(_tmp_4);
        _item.setCustomSettings(_tmpCustomSettings);
        final Date _tmpCreatedAt;
        final Long _tmp_5;
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _tmp_5 = null;
        } else {
          _tmp_5 = _cursor.getLong(_cursorIndexOfCreatedAt);
        }
        _tmpCreatedAt = DateConverter.toDate(_tmp_5);
        _item.setCreatedAt(_tmpCreatedAt);
        final Date _tmpUpdatedAt;
        final Long _tmp_6;
        if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
          _tmp_6 = null;
        } else {
          _tmp_6 = _cursor.getLong(_cursorIndexOfUpdatedAt);
        }
        _tmpUpdatedAt = DateConverter.toDate(_tmp_6);
        _item.setUpdatedAt(_tmpUpdatedAt);
        final Date _tmpActivatedAt;
        final Long _tmp_7;
        if (_cursor.isNull(_cursorIndexOfActivatedAt)) {
          _tmp_7 = null;
        } else {
          _tmp_7 = _cursor.getLong(_cursorIndexOfActivatedAt);
        }
        _tmpActivatedAt = DateConverter.toDate(_tmp_7);
        _item.setActivatedAt(_tmpActivatedAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Profile> getByActiveStatus(final boolean isActive) {
    final String _sql = "SELECT * FROM profiles WHERE isActive = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final int _tmp = isActive ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profileId");
      final int _cursorIndexOfProfileName = CursorUtil.getColumnIndexOrThrow(_cursor, "profileName");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfTheme = CursorUtil.getColumnIndexOrThrow(_cursor, "theme");
      final int _cursorIndexOfNotificationSettings = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationSettings");
      final int _cursorIndexOfAutomationRules = CursorUtil.getColumnIndexOrThrow(_cursor, "automationRules");
      final int _cursorIndexOfCustomSettings = CursorUtil.getColumnIndexOrThrow(_cursor, "customSettings");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final int _cursorIndexOfActivatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "activatedAt");
      final List<Profile> _result = new ArrayList<Profile>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Profile _item;
        _item = new Profile();
        final String _tmpProfileId;
        if (_cursor.isNull(_cursorIndexOfProfileId)) {
          _tmpProfileId = null;
        } else {
          _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
        }
        _item.setProfileId(_tmpProfileId);
        final String _tmpProfileName;
        if (_cursor.isNull(_cursorIndexOfProfileName)) {
          _tmpProfileName = null;
        } else {
          _tmpProfileName = _cursor.getString(_cursorIndexOfProfileName);
        }
        _item.setProfileName(_tmpProfileName);
        final boolean _tmpIsActive;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp_1 != 0;
        _item.setActive(_tmpIsActive);
        final Theme _tmpTheme;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfTheme)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfTheme);
        }
        _tmpTheme = ThemeConverter.fromString(_tmp_2);
        _item.setTheme(_tmpTheme);
        final NotificationSettings _tmpNotificationSettings;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfNotificationSettings)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfNotificationSettings);
        }
        _tmpNotificationSettings = NotificationSettingsConverter.fromString(_tmp_3);
        _item.setNotificationSettings(_tmpNotificationSettings);
        final List<AutomationRule> _tmpAutomationRules;
        final String _tmp_4;
        if (_cursor.isNull(_cursorIndexOfAutomationRules)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getString(_cursorIndexOfAutomationRules);
        }
        _tmpAutomationRules = AutomationRuleListConverter.fromString(_tmp_4);
        _item.setAutomationRules(_tmpAutomationRules);
        final Map<String, Object> _tmpCustomSettings;
        final String _tmp_5;
        if (_cursor.isNull(_cursorIndexOfCustomSettings)) {
          _tmp_5 = null;
        } else {
          _tmp_5 = _cursor.getString(_cursorIndexOfCustomSettings);
        }
        _tmpCustomSettings = StringMapConverter.toStringMap(_tmp_5);
        _item.setCustomSettings(_tmpCustomSettings);
        final Date _tmpCreatedAt;
        final Long _tmp_6;
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _tmp_6 = null;
        } else {
          _tmp_6 = _cursor.getLong(_cursorIndexOfCreatedAt);
        }
        _tmpCreatedAt = DateConverter.toDate(_tmp_6);
        _item.setCreatedAt(_tmpCreatedAt);
        final Date _tmpUpdatedAt;
        final Long _tmp_7;
        if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
          _tmp_7 = null;
        } else {
          _tmp_7 = _cursor.getLong(_cursorIndexOfUpdatedAt);
        }
        _tmpUpdatedAt = DateConverter.toDate(_tmp_7);
        _item.setUpdatedAt(_tmpUpdatedAt);
        final Date _tmpActivatedAt;
        final Long _tmp_8;
        if (_cursor.isNull(_cursorIndexOfActivatedAt)) {
          _tmp_8 = null;
        } else {
          _tmp_8 = _cursor.getLong(_cursorIndexOfActivatedAt);
        }
        _tmpActivatedAt = DateConverter.toDate(_tmp_8);
        _item.setActivatedAt(_tmpActivatedAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Profile getActiveProfile() {
    final String _sql = "SELECT * FROM profiles WHERE isActive = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profileId");
      final int _cursorIndexOfProfileName = CursorUtil.getColumnIndexOrThrow(_cursor, "profileName");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfTheme = CursorUtil.getColumnIndexOrThrow(_cursor, "theme");
      final int _cursorIndexOfNotificationSettings = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationSettings");
      final int _cursorIndexOfAutomationRules = CursorUtil.getColumnIndexOrThrow(_cursor, "automationRules");
      final int _cursorIndexOfCustomSettings = CursorUtil.getColumnIndexOrThrow(_cursor, "customSettings");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final int _cursorIndexOfActivatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "activatedAt");
      final Profile _result;
      if (_cursor.moveToFirst()) {
        _result = new Profile();
        final String _tmpProfileId;
        if (_cursor.isNull(_cursorIndexOfProfileId)) {
          _tmpProfileId = null;
        } else {
          _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
        }
        _result.setProfileId(_tmpProfileId);
        final String _tmpProfileName;
        if (_cursor.isNull(_cursorIndexOfProfileName)) {
          _tmpProfileName = null;
        } else {
          _tmpProfileName = _cursor.getString(_cursorIndexOfProfileName);
        }
        _result.setProfileName(_tmpProfileName);
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        _result.setActive(_tmpIsActive);
        final Theme _tmpTheme;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfTheme)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfTheme);
        }
        _tmpTheme = ThemeConverter.fromString(_tmp_1);
        _result.setTheme(_tmpTheme);
        final NotificationSettings _tmpNotificationSettings;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfNotificationSettings)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfNotificationSettings);
        }
        _tmpNotificationSettings = NotificationSettingsConverter.fromString(_tmp_2);
        _result.setNotificationSettings(_tmpNotificationSettings);
        final List<AutomationRule> _tmpAutomationRules;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfAutomationRules)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfAutomationRules);
        }
        _tmpAutomationRules = AutomationRuleListConverter.fromString(_tmp_3);
        _result.setAutomationRules(_tmpAutomationRules);
        final Map<String, Object> _tmpCustomSettings;
        final String _tmp_4;
        if (_cursor.isNull(_cursorIndexOfCustomSettings)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getString(_cursorIndexOfCustomSettings);
        }
        _tmpCustomSettings = StringMapConverter.toStringMap(_tmp_4);
        _result.setCustomSettings(_tmpCustomSettings);
        final Date _tmpCreatedAt;
        final Long _tmp_5;
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _tmp_5 = null;
        } else {
          _tmp_5 = _cursor.getLong(_cursorIndexOfCreatedAt);
        }
        _tmpCreatedAt = DateConverter.toDate(_tmp_5);
        _result.setCreatedAt(_tmpCreatedAt);
        final Date _tmpUpdatedAt;
        final Long _tmp_6;
        if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
          _tmp_6 = null;
        } else {
          _tmp_6 = _cursor.getLong(_cursorIndexOfUpdatedAt);
        }
        _tmpUpdatedAt = DateConverter.toDate(_tmp_6);
        _result.setUpdatedAt(_tmpUpdatedAt);
        final Date _tmpActivatedAt;
        final Long _tmp_7;
        if (_cursor.isNull(_cursorIndexOfActivatedAt)) {
          _tmp_7 = null;
        } else {
          _tmp_7 = _cursor.getLong(_cursorIndexOfActivatedAt);
        }
        _tmpActivatedAt = DateConverter.toDate(_tmp_7);
        _result.setActivatedAt(_tmpActivatedAt);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
