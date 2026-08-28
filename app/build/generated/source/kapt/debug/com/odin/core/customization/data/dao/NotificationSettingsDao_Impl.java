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
import com.odin.core.customization.data.converters.DateConverter;
import com.odin.core.customization.data.converters.NotificationPriorityConverter;
import com.odin.core.customization.data.converters.VibrationPatternConverter;
import com.odin.core.customization.model.NotificationPriority;
import com.odin.core.customization.model.NotificationSettings;
import com.odin.core.customization.model.VibrationPattern;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NotificationSettingsDao_Impl implements NotificationSettingsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<NotificationSettings> __insertionAdapterOfNotificationSettings;

  private final EntityDeletionOrUpdateAdapter<NotificationSettings> __deletionAdapterOfNotificationSettings;

  private final EntityDeletionOrUpdateAdapter<NotificationSettings> __updateAdapterOfNotificationSettings;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public NotificationSettingsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNotificationSettings = new EntityInsertionAdapter<NotificationSettings>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `notification_settings` (`settingsId`,`notificationsEnabled`,`soundEnabled`,`soundUri`,`vibrationEnabled`,`vibrationPattern`,`ledEnabled`,`ledColor`,`ledOnMs`,`ledOffMs`,`priority`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final NotificationSettings entity) {
        if (entity.getSettingsId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getSettingsId());
        }
        final int _tmp = entity.isNotificationsEnabled() ? 1 : 0;
        statement.bindLong(2, _tmp);
        final int _tmp_1 = entity.isSoundEnabled() ? 1 : 0;
        statement.bindLong(3, _tmp_1);
        if (entity.getSoundUri() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getSoundUri());
        }
        final int _tmp_2 = entity.isVibrationEnabled() ? 1 : 0;
        statement.bindLong(5, _tmp_2);
        final String _tmp_3 = VibrationPatternConverter.toString(entity.getVibrationPattern());
        if (_tmp_3 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_3);
        }
        final int _tmp_4 = entity.isLedEnabled() ? 1 : 0;
        statement.bindLong(7, _tmp_4);
        statement.bindLong(8, entity.getLedColor());
        statement.bindLong(9, entity.getLedOnMs());
        statement.bindLong(10, entity.getLedOffMs());
        final String _tmp_5 = NotificationPriorityConverter.toString(entity.getPriority());
        if (_tmp_5 == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, _tmp_5);
        }
        final Long _tmp_6 = DateConverter.fromDate(entity.getUpdatedAt());
        if (_tmp_6 == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, _tmp_6);
        }
      }
    };
    this.__deletionAdapterOfNotificationSettings = new EntityDeletionOrUpdateAdapter<NotificationSettings>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `notification_settings` WHERE `settingsId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final NotificationSettings entity) {
        if (entity.getSettingsId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getSettingsId());
        }
      }
    };
    this.__updateAdapterOfNotificationSettings = new EntityDeletionOrUpdateAdapter<NotificationSettings>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `notification_settings` SET `settingsId` = ?,`notificationsEnabled` = ?,`soundEnabled` = ?,`soundUri` = ?,`vibrationEnabled` = ?,`vibrationPattern` = ?,`ledEnabled` = ?,`ledColor` = ?,`ledOnMs` = ?,`ledOffMs` = ?,`priority` = ?,`updatedAt` = ? WHERE `settingsId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final NotificationSettings entity) {
        if (entity.getSettingsId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getSettingsId());
        }
        final int _tmp = entity.isNotificationsEnabled() ? 1 : 0;
        statement.bindLong(2, _tmp);
        final int _tmp_1 = entity.isSoundEnabled() ? 1 : 0;
        statement.bindLong(3, _tmp_1);
        if (entity.getSoundUri() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getSoundUri());
        }
        final int _tmp_2 = entity.isVibrationEnabled() ? 1 : 0;
        statement.bindLong(5, _tmp_2);
        final String _tmp_3 = VibrationPatternConverter.toString(entity.getVibrationPattern());
        if (_tmp_3 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_3);
        }
        final int _tmp_4 = entity.isLedEnabled() ? 1 : 0;
        statement.bindLong(7, _tmp_4);
        statement.bindLong(8, entity.getLedColor());
        statement.bindLong(9, entity.getLedOnMs());
        statement.bindLong(10, entity.getLedOffMs());
        final String _tmp_5 = NotificationPriorityConverter.toString(entity.getPriority());
        if (_tmp_5 == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, _tmp_5);
        }
        final Long _tmp_6 = DateConverter.fromDate(entity.getUpdatedAt());
        if (_tmp_6 == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, _tmp_6);
        }
        if (entity.getSettingsId() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getSettingsId());
        }
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM notification_settings WHERE settingsId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM notification_settings";
        return _query;
      }
    };
  }

  @Override
  public void insert(final NotificationSettings settings) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfNotificationSettings.insert(settings);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final List<NotificationSettings> settingsList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfNotificationSettings.insert(settingsList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final NotificationSettings settings) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfNotificationSettings.handle(settings);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final NotificationSettings settings) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfNotificationSettings.handle(settings);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteById(final String settingsId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
    int _argIndex = 1;
    if (settingsId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, settingsId);
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
  public NotificationSettings getById(final String settingsId) {
    final String _sql = "SELECT * FROM notification_settings WHERE settingsId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (settingsId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, settingsId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfSettingsId = CursorUtil.getColumnIndexOrThrow(_cursor, "settingsId");
      final int _cursorIndexOfNotificationsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationsEnabled");
      final int _cursorIndexOfSoundEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "soundEnabled");
      final int _cursorIndexOfSoundUri = CursorUtil.getColumnIndexOrThrow(_cursor, "soundUri");
      final int _cursorIndexOfVibrationEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "vibrationEnabled");
      final int _cursorIndexOfVibrationPattern = CursorUtil.getColumnIndexOrThrow(_cursor, "vibrationPattern");
      final int _cursorIndexOfLedEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "ledEnabled");
      final int _cursorIndexOfLedColor = CursorUtil.getColumnIndexOrThrow(_cursor, "ledColor");
      final int _cursorIndexOfLedOnMs = CursorUtil.getColumnIndexOrThrow(_cursor, "ledOnMs");
      final int _cursorIndexOfLedOffMs = CursorUtil.getColumnIndexOrThrow(_cursor, "ledOffMs");
      final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final NotificationSettings _result;
      if (_cursor.moveToFirst()) {
        _result = new NotificationSettings();
        final String _tmpSettingsId;
        if (_cursor.isNull(_cursorIndexOfSettingsId)) {
          _tmpSettingsId = null;
        } else {
          _tmpSettingsId = _cursor.getString(_cursorIndexOfSettingsId);
        }
        _result.setSettingsId(_tmpSettingsId);
        final boolean _tmpNotificationsEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfNotificationsEnabled);
        _tmpNotificationsEnabled = _tmp != 0;
        _result.setNotificationsEnabled(_tmpNotificationsEnabled);
        final boolean _tmpSoundEnabled;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfSoundEnabled);
        _tmpSoundEnabled = _tmp_1 != 0;
        _result.setSoundEnabled(_tmpSoundEnabled);
        final String _tmpSoundUri;
        if (_cursor.isNull(_cursorIndexOfSoundUri)) {
          _tmpSoundUri = null;
        } else {
          _tmpSoundUri = _cursor.getString(_cursorIndexOfSoundUri);
        }
        _result.setSoundUri(_tmpSoundUri);
        final boolean _tmpVibrationEnabled;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfVibrationEnabled);
        _tmpVibrationEnabled = _tmp_2 != 0;
        _result.setVibrationEnabled(_tmpVibrationEnabled);
        final VibrationPattern _tmpVibrationPattern;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfVibrationPattern)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfVibrationPattern);
        }
        _tmpVibrationPattern = VibrationPatternConverter.fromString(_tmp_3);
        _result.setVibrationPattern(_tmpVibrationPattern);
        final boolean _tmpLedEnabled;
        final int _tmp_4;
        _tmp_4 = _cursor.getInt(_cursorIndexOfLedEnabled);
        _tmpLedEnabled = _tmp_4 != 0;
        _result.setLedEnabled(_tmpLedEnabled);
        final int _tmpLedColor;
        _tmpLedColor = _cursor.getInt(_cursorIndexOfLedColor);
        _result.setLedColor(_tmpLedColor);
        final int _tmpLedOnMs;
        _tmpLedOnMs = _cursor.getInt(_cursorIndexOfLedOnMs);
        _result.setLedOnMs(_tmpLedOnMs);
        final int _tmpLedOffMs;
        _tmpLedOffMs = _cursor.getInt(_cursorIndexOfLedOffMs);
        _result.setLedOffMs(_tmpLedOffMs);
        final NotificationPriority _tmpPriority;
        final String _tmp_5;
        if (_cursor.isNull(_cursorIndexOfPriority)) {
          _tmp_5 = null;
        } else {
          _tmp_5 = _cursor.getString(_cursorIndexOfPriority);
        }
        _tmpPriority = NotificationPriorityConverter.fromString(_tmp_5);
        _result.setPriority(_tmpPriority);
        final Date _tmpUpdatedAt;
        final Long _tmp_6;
        if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
          _tmp_6 = null;
        } else {
          _tmp_6 = _cursor.getLong(_cursorIndexOfUpdatedAt);
        }
        _tmpUpdatedAt = DateConverter.toDate(_tmp_6);
        _result.setUpdatedAt(_tmpUpdatedAt);
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
  public List<NotificationSettings> getAll() {
    final String _sql = "SELECT * FROM notification_settings";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfSettingsId = CursorUtil.getColumnIndexOrThrow(_cursor, "settingsId");
      final int _cursorIndexOfNotificationsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationsEnabled");
      final int _cursorIndexOfSoundEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "soundEnabled");
      final int _cursorIndexOfSoundUri = CursorUtil.getColumnIndexOrThrow(_cursor, "soundUri");
      final int _cursorIndexOfVibrationEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "vibrationEnabled");
      final int _cursorIndexOfVibrationPattern = CursorUtil.getColumnIndexOrThrow(_cursor, "vibrationPattern");
      final int _cursorIndexOfLedEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "ledEnabled");
      final int _cursorIndexOfLedColor = CursorUtil.getColumnIndexOrThrow(_cursor, "ledColor");
      final int _cursorIndexOfLedOnMs = CursorUtil.getColumnIndexOrThrow(_cursor, "ledOnMs");
      final int _cursorIndexOfLedOffMs = CursorUtil.getColumnIndexOrThrow(_cursor, "ledOffMs");
      final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final List<NotificationSettings> _result = new ArrayList<NotificationSettings>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final NotificationSettings _item;
        _item = new NotificationSettings();
        final String _tmpSettingsId;
        if (_cursor.isNull(_cursorIndexOfSettingsId)) {
          _tmpSettingsId = null;
        } else {
          _tmpSettingsId = _cursor.getString(_cursorIndexOfSettingsId);
        }
        _item.setSettingsId(_tmpSettingsId);
        final boolean _tmpNotificationsEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfNotificationsEnabled);
        _tmpNotificationsEnabled = _tmp != 0;
        _item.setNotificationsEnabled(_tmpNotificationsEnabled);
        final boolean _tmpSoundEnabled;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfSoundEnabled);
        _tmpSoundEnabled = _tmp_1 != 0;
        _item.setSoundEnabled(_tmpSoundEnabled);
        final String _tmpSoundUri;
        if (_cursor.isNull(_cursorIndexOfSoundUri)) {
          _tmpSoundUri = null;
        } else {
          _tmpSoundUri = _cursor.getString(_cursorIndexOfSoundUri);
        }
        _item.setSoundUri(_tmpSoundUri);
        final boolean _tmpVibrationEnabled;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfVibrationEnabled);
        _tmpVibrationEnabled = _tmp_2 != 0;
        _item.setVibrationEnabled(_tmpVibrationEnabled);
        final VibrationPattern _tmpVibrationPattern;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfVibrationPattern)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfVibrationPattern);
        }
        _tmpVibrationPattern = VibrationPatternConverter.fromString(_tmp_3);
        _item.setVibrationPattern(_tmpVibrationPattern);
        final boolean _tmpLedEnabled;
        final int _tmp_4;
        _tmp_4 = _cursor.getInt(_cursorIndexOfLedEnabled);
        _tmpLedEnabled = _tmp_4 != 0;
        _item.setLedEnabled(_tmpLedEnabled);
        final int _tmpLedColor;
        _tmpLedColor = _cursor.getInt(_cursorIndexOfLedColor);
        _item.setLedColor(_tmpLedColor);
        final int _tmpLedOnMs;
        _tmpLedOnMs = _cursor.getInt(_cursorIndexOfLedOnMs);
        _item.setLedOnMs(_tmpLedOnMs);
        final int _tmpLedOffMs;
        _tmpLedOffMs = _cursor.getInt(_cursorIndexOfLedOffMs);
        _item.setLedOffMs(_tmpLedOffMs);
        final NotificationPriority _tmpPriority;
        final String _tmp_5;
        if (_cursor.isNull(_cursorIndexOfPriority)) {
          _tmp_5 = null;
        } else {
          _tmp_5 = _cursor.getString(_cursorIndexOfPriority);
        }
        _tmpPriority = NotificationPriorityConverter.fromString(_tmp_5);
        _item.setPriority(_tmpPriority);
        final Date _tmpUpdatedAt;
        final Long _tmp_6;
        if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
          _tmp_6 = null;
        } else {
          _tmp_6 = _cursor.getLong(_cursorIndexOfUpdatedAt);
        }
        _tmpUpdatedAt = DateConverter.toDate(_tmp_6);
        _item.setUpdatedAt(_tmpUpdatedAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<NotificationSettings> getByEnabledStatus(final boolean enabled) {
    final String _sql = "SELECT * FROM notification_settings WHERE notificationsEnabled = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final int _tmp = enabled ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfSettingsId = CursorUtil.getColumnIndexOrThrow(_cursor, "settingsId");
      final int _cursorIndexOfNotificationsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationsEnabled");
      final int _cursorIndexOfSoundEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "soundEnabled");
      final int _cursorIndexOfSoundUri = CursorUtil.getColumnIndexOrThrow(_cursor, "soundUri");
      final int _cursorIndexOfVibrationEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "vibrationEnabled");
      final int _cursorIndexOfVibrationPattern = CursorUtil.getColumnIndexOrThrow(_cursor, "vibrationPattern");
      final int _cursorIndexOfLedEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "ledEnabled");
      final int _cursorIndexOfLedColor = CursorUtil.getColumnIndexOrThrow(_cursor, "ledColor");
      final int _cursorIndexOfLedOnMs = CursorUtil.getColumnIndexOrThrow(_cursor, "ledOnMs");
      final int _cursorIndexOfLedOffMs = CursorUtil.getColumnIndexOrThrow(_cursor, "ledOffMs");
      final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final List<NotificationSettings> _result = new ArrayList<NotificationSettings>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final NotificationSettings _item;
        _item = new NotificationSettings();
        final String _tmpSettingsId;
        if (_cursor.isNull(_cursorIndexOfSettingsId)) {
          _tmpSettingsId = null;
        } else {
          _tmpSettingsId = _cursor.getString(_cursorIndexOfSettingsId);
        }
        _item.setSettingsId(_tmpSettingsId);
        final boolean _tmpNotificationsEnabled;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfNotificationsEnabled);
        _tmpNotificationsEnabled = _tmp_1 != 0;
        _item.setNotificationsEnabled(_tmpNotificationsEnabled);
        final boolean _tmpSoundEnabled;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfSoundEnabled);
        _tmpSoundEnabled = _tmp_2 != 0;
        _item.setSoundEnabled(_tmpSoundEnabled);
        final String _tmpSoundUri;
        if (_cursor.isNull(_cursorIndexOfSoundUri)) {
          _tmpSoundUri = null;
        } else {
          _tmpSoundUri = _cursor.getString(_cursorIndexOfSoundUri);
        }
        _item.setSoundUri(_tmpSoundUri);
        final boolean _tmpVibrationEnabled;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfVibrationEnabled);
        _tmpVibrationEnabled = _tmp_3 != 0;
        _item.setVibrationEnabled(_tmpVibrationEnabled);
        final VibrationPattern _tmpVibrationPattern;
        final String _tmp_4;
        if (_cursor.isNull(_cursorIndexOfVibrationPattern)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getString(_cursorIndexOfVibrationPattern);
        }
        _tmpVibrationPattern = VibrationPatternConverter.fromString(_tmp_4);
        _item.setVibrationPattern(_tmpVibrationPattern);
        final boolean _tmpLedEnabled;
        final int _tmp_5;
        _tmp_5 = _cursor.getInt(_cursorIndexOfLedEnabled);
        _tmpLedEnabled = _tmp_5 != 0;
        _item.setLedEnabled(_tmpLedEnabled);
        final int _tmpLedColor;
        _tmpLedColor = _cursor.getInt(_cursorIndexOfLedColor);
        _item.setLedColor(_tmpLedColor);
        final int _tmpLedOnMs;
        _tmpLedOnMs = _cursor.getInt(_cursorIndexOfLedOnMs);
        _item.setLedOnMs(_tmpLedOnMs);
        final int _tmpLedOffMs;
        _tmpLedOffMs = _cursor.getInt(_cursorIndexOfLedOffMs);
        _item.setLedOffMs(_tmpLedOffMs);
        final NotificationPriority _tmpPriority;
        final String _tmp_6;
        if (_cursor.isNull(_cursorIndexOfPriority)) {
          _tmp_6 = null;
        } else {
          _tmp_6 = _cursor.getString(_cursorIndexOfPriority);
        }
        _tmpPriority = NotificationPriorityConverter.fromString(_tmp_6);
        _item.setPriority(_tmpPriority);
        final Date _tmpUpdatedAt;
        final Long _tmp_7;
        if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
          _tmp_7 = null;
        } else {
          _tmp_7 = _cursor.getLong(_cursorIndexOfUpdatedAt);
        }
        _tmpUpdatedAt = DateConverter.toDate(_tmp_7);
        _item.setUpdatedAt(_tmpUpdatedAt);
        _result.add(_item);
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
