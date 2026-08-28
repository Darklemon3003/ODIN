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
import com.odin.core.customization.data.converters.BackupStatusConverter;
import com.odin.core.customization.data.converters.BackupTypeConverter;
import com.odin.core.customization.data.converters.DateConverter;
import com.odin.core.customization.model.Backup;
import com.odin.core.customization.model.BackupStatus;
import com.odin.core.customization.model.BackupType;
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
public final class BackupDao_Impl implements BackupDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Backup> __insertionAdapterOfBackup;

  private final EntityDeletionOrUpdateAdapter<Backup> __deletionAdapterOfBackup;

  private final EntityDeletionOrUpdateAdapter<Backup> __updateAdapterOfBackup;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public BackupDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBackup = new EntityInsertionAdapter<Backup>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `backups` (`backupId`,`backupName`,`backupTimestamp`,`backupSize`,`backupPath`,`isEncrypted`,`encryptionAlgorithm`,`backupType`,`status`,`completedAt`,`restoredAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Backup entity) {
        if (entity.getBackupId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getBackupId());
        }
        if (entity.getBackupName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getBackupName());
        }
        final Long _tmp = DateConverter.fromDate(entity.getBackupTimestamp());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, _tmp);
        }
        statement.bindLong(4, entity.getBackupSize());
        if (entity.getBackupPath() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getBackupPath());
        }
        final int _tmp_1 = entity.isEncrypted() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        if (entity.getEncryptionAlgorithm() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getEncryptionAlgorithm());
        }
        final String _tmp_2 = BackupTypeConverter.toString(entity.getBackupType());
        if (_tmp_2 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_2);
        }
        final String _tmp_3 = BackupStatusConverter.toString(entity.getStatus());
        if (_tmp_3 == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, _tmp_3);
        }
        final Long _tmp_4 = DateConverter.fromDate(entity.getCompletedAt());
        if (_tmp_4 == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, _tmp_4);
        }
        final Long _tmp_5 = DateConverter.fromDate(entity.getRestoredAt());
        if (_tmp_5 == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, _tmp_5);
        }
      }
    };
    this.__deletionAdapterOfBackup = new EntityDeletionOrUpdateAdapter<Backup>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `backups` WHERE `backupId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Backup entity) {
        if (entity.getBackupId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getBackupId());
        }
      }
    };
    this.__updateAdapterOfBackup = new EntityDeletionOrUpdateAdapter<Backup>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `backups` SET `backupId` = ?,`backupName` = ?,`backupTimestamp` = ?,`backupSize` = ?,`backupPath` = ?,`isEncrypted` = ?,`encryptionAlgorithm` = ?,`backupType` = ?,`status` = ?,`completedAt` = ?,`restoredAt` = ? WHERE `backupId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Backup entity) {
        if (entity.getBackupId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getBackupId());
        }
        if (entity.getBackupName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getBackupName());
        }
        final Long _tmp = DateConverter.fromDate(entity.getBackupTimestamp());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, _tmp);
        }
        statement.bindLong(4, entity.getBackupSize());
        if (entity.getBackupPath() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getBackupPath());
        }
        final int _tmp_1 = entity.isEncrypted() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        if (entity.getEncryptionAlgorithm() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getEncryptionAlgorithm());
        }
        final String _tmp_2 = BackupTypeConverter.toString(entity.getBackupType());
        if (_tmp_2 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_2);
        }
        final String _tmp_3 = BackupStatusConverter.toString(entity.getStatus());
        if (_tmp_3 == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, _tmp_3);
        }
        final Long _tmp_4 = DateConverter.fromDate(entity.getCompletedAt());
        if (_tmp_4 == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, _tmp_4);
        }
        final Long _tmp_5 = DateConverter.fromDate(entity.getRestoredAt());
        if (_tmp_5 == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, _tmp_5);
        }
        if (entity.getBackupId() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getBackupId());
        }
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM backups WHERE backupId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM backups";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Backup backup) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfBackup.insert(backup);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final List<Backup> backups) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfBackup.insert(backups);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Backup backup) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfBackup.handle(backup);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Backup backup) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfBackup.handle(backup);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteById(final String backupId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
    int _argIndex = 1;
    if (backupId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, backupId);
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
  public Backup getById(final String backupId) {
    final String _sql = "SELECT * FROM backups WHERE backupId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (backupId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, backupId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBackupId = CursorUtil.getColumnIndexOrThrow(_cursor, "backupId");
      final int _cursorIndexOfBackupName = CursorUtil.getColumnIndexOrThrow(_cursor, "backupName");
      final int _cursorIndexOfBackupTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "backupTimestamp");
      final int _cursorIndexOfBackupSize = CursorUtil.getColumnIndexOrThrow(_cursor, "backupSize");
      final int _cursorIndexOfBackupPath = CursorUtil.getColumnIndexOrThrow(_cursor, "backupPath");
      final int _cursorIndexOfIsEncrypted = CursorUtil.getColumnIndexOrThrow(_cursor, "isEncrypted");
      final int _cursorIndexOfEncryptionAlgorithm = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptionAlgorithm");
      final int _cursorIndexOfBackupType = CursorUtil.getColumnIndexOrThrow(_cursor, "backupType");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
      final int _cursorIndexOfRestoredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "restoredAt");
      final Backup _result;
      if (_cursor.moveToFirst()) {
        _result = new Backup();
        final String _tmpBackupId;
        if (_cursor.isNull(_cursorIndexOfBackupId)) {
          _tmpBackupId = null;
        } else {
          _tmpBackupId = _cursor.getString(_cursorIndexOfBackupId);
        }
        _result.setBackupId(_tmpBackupId);
        final String _tmpBackupName;
        if (_cursor.isNull(_cursorIndexOfBackupName)) {
          _tmpBackupName = null;
        } else {
          _tmpBackupName = _cursor.getString(_cursorIndexOfBackupName);
        }
        _result.setBackupName(_tmpBackupName);
        final Date _tmpBackupTimestamp;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfBackupTimestamp)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfBackupTimestamp);
        }
        _tmpBackupTimestamp = DateConverter.toDate(_tmp);
        _result.setBackupTimestamp(_tmpBackupTimestamp);
        final long _tmpBackupSize;
        _tmpBackupSize = _cursor.getLong(_cursorIndexOfBackupSize);
        _result.setBackupSize(_tmpBackupSize);
        final String _tmpBackupPath;
        if (_cursor.isNull(_cursorIndexOfBackupPath)) {
          _tmpBackupPath = null;
        } else {
          _tmpBackupPath = _cursor.getString(_cursorIndexOfBackupPath);
        }
        _result.setBackupPath(_tmpBackupPath);
        final boolean _tmpIsEncrypted;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsEncrypted);
        _tmpIsEncrypted = _tmp_1 != 0;
        _result.setEncrypted(_tmpIsEncrypted);
        final String _tmpEncryptionAlgorithm;
        if (_cursor.isNull(_cursorIndexOfEncryptionAlgorithm)) {
          _tmpEncryptionAlgorithm = null;
        } else {
          _tmpEncryptionAlgorithm = _cursor.getString(_cursorIndexOfEncryptionAlgorithm);
        }
        _result.setEncryptionAlgorithm(_tmpEncryptionAlgorithm);
        final BackupType _tmpBackupType;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfBackupType)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfBackupType);
        }
        _tmpBackupType = BackupTypeConverter.fromString(_tmp_2);
        _result.setBackupType(_tmpBackupType);
        final BackupStatus _tmpStatus;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfStatus);
        }
        _tmpStatus = BackupStatusConverter.fromString(_tmp_3);
        _result.setStatus(_tmpStatus);
        final Date _tmpCompletedAt;
        final Long _tmp_4;
        if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getLong(_cursorIndexOfCompletedAt);
        }
        _tmpCompletedAt = DateConverter.toDate(_tmp_4);
        _result.setCompletedAt(_tmpCompletedAt);
        final Date _tmpRestoredAt;
        final Long _tmp_5;
        if (_cursor.isNull(_cursorIndexOfRestoredAt)) {
          _tmp_5 = null;
        } else {
          _tmp_5 = _cursor.getLong(_cursorIndexOfRestoredAt);
        }
        _tmpRestoredAt = DateConverter.toDate(_tmp_5);
        _result.setRestoredAt(_tmpRestoredAt);
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
  public List<Backup> getAll() {
    final String _sql = "SELECT * FROM backups";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBackupId = CursorUtil.getColumnIndexOrThrow(_cursor, "backupId");
      final int _cursorIndexOfBackupName = CursorUtil.getColumnIndexOrThrow(_cursor, "backupName");
      final int _cursorIndexOfBackupTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "backupTimestamp");
      final int _cursorIndexOfBackupSize = CursorUtil.getColumnIndexOrThrow(_cursor, "backupSize");
      final int _cursorIndexOfBackupPath = CursorUtil.getColumnIndexOrThrow(_cursor, "backupPath");
      final int _cursorIndexOfIsEncrypted = CursorUtil.getColumnIndexOrThrow(_cursor, "isEncrypted");
      final int _cursorIndexOfEncryptionAlgorithm = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptionAlgorithm");
      final int _cursorIndexOfBackupType = CursorUtil.getColumnIndexOrThrow(_cursor, "backupType");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
      final int _cursorIndexOfRestoredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "restoredAt");
      final List<Backup> _result = new ArrayList<Backup>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Backup _item;
        _item = new Backup();
        final String _tmpBackupId;
        if (_cursor.isNull(_cursorIndexOfBackupId)) {
          _tmpBackupId = null;
        } else {
          _tmpBackupId = _cursor.getString(_cursorIndexOfBackupId);
        }
        _item.setBackupId(_tmpBackupId);
        final String _tmpBackupName;
        if (_cursor.isNull(_cursorIndexOfBackupName)) {
          _tmpBackupName = null;
        } else {
          _tmpBackupName = _cursor.getString(_cursorIndexOfBackupName);
        }
        _item.setBackupName(_tmpBackupName);
        final Date _tmpBackupTimestamp;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfBackupTimestamp)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfBackupTimestamp);
        }
        _tmpBackupTimestamp = DateConverter.toDate(_tmp);
        _item.setBackupTimestamp(_tmpBackupTimestamp);
        final long _tmpBackupSize;
        _tmpBackupSize = _cursor.getLong(_cursorIndexOfBackupSize);
        _item.setBackupSize(_tmpBackupSize);
        final String _tmpBackupPath;
        if (_cursor.isNull(_cursorIndexOfBackupPath)) {
          _tmpBackupPath = null;
        } else {
          _tmpBackupPath = _cursor.getString(_cursorIndexOfBackupPath);
        }
        _item.setBackupPath(_tmpBackupPath);
        final boolean _tmpIsEncrypted;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsEncrypted);
        _tmpIsEncrypted = _tmp_1 != 0;
        _item.setEncrypted(_tmpIsEncrypted);
        final String _tmpEncryptionAlgorithm;
        if (_cursor.isNull(_cursorIndexOfEncryptionAlgorithm)) {
          _tmpEncryptionAlgorithm = null;
        } else {
          _tmpEncryptionAlgorithm = _cursor.getString(_cursorIndexOfEncryptionAlgorithm);
        }
        _item.setEncryptionAlgorithm(_tmpEncryptionAlgorithm);
        final BackupType _tmpBackupType;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfBackupType)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfBackupType);
        }
        _tmpBackupType = BackupTypeConverter.fromString(_tmp_2);
        _item.setBackupType(_tmpBackupType);
        final BackupStatus _tmpStatus;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfStatus);
        }
        _tmpStatus = BackupStatusConverter.fromString(_tmp_3);
        _item.setStatus(_tmpStatus);
        final Date _tmpCompletedAt;
        final Long _tmp_4;
        if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getLong(_cursorIndexOfCompletedAt);
        }
        _tmpCompletedAt = DateConverter.toDate(_tmp_4);
        _item.setCompletedAt(_tmpCompletedAt);
        final Date _tmpRestoredAt;
        final Long _tmp_5;
        if (_cursor.isNull(_cursorIndexOfRestoredAt)) {
          _tmp_5 = null;
        } else {
          _tmp_5 = _cursor.getLong(_cursorIndexOfRestoredAt);
        }
        _tmpRestoredAt = DateConverter.toDate(_tmp_5);
        _item.setRestoredAt(_tmpRestoredAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Backup> getByStatus(final String status) {
    final String _sql = "SELECT * FROM backups WHERE status = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (status == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, status);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBackupId = CursorUtil.getColumnIndexOrThrow(_cursor, "backupId");
      final int _cursorIndexOfBackupName = CursorUtil.getColumnIndexOrThrow(_cursor, "backupName");
      final int _cursorIndexOfBackupTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "backupTimestamp");
      final int _cursorIndexOfBackupSize = CursorUtil.getColumnIndexOrThrow(_cursor, "backupSize");
      final int _cursorIndexOfBackupPath = CursorUtil.getColumnIndexOrThrow(_cursor, "backupPath");
      final int _cursorIndexOfIsEncrypted = CursorUtil.getColumnIndexOrThrow(_cursor, "isEncrypted");
      final int _cursorIndexOfEncryptionAlgorithm = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptionAlgorithm");
      final int _cursorIndexOfBackupType = CursorUtil.getColumnIndexOrThrow(_cursor, "backupType");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
      final int _cursorIndexOfRestoredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "restoredAt");
      final List<Backup> _result = new ArrayList<Backup>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Backup _item;
        _item = new Backup();
        final String _tmpBackupId;
        if (_cursor.isNull(_cursorIndexOfBackupId)) {
          _tmpBackupId = null;
        } else {
          _tmpBackupId = _cursor.getString(_cursorIndexOfBackupId);
        }
        _item.setBackupId(_tmpBackupId);
        final String _tmpBackupName;
        if (_cursor.isNull(_cursorIndexOfBackupName)) {
          _tmpBackupName = null;
        } else {
          _tmpBackupName = _cursor.getString(_cursorIndexOfBackupName);
        }
        _item.setBackupName(_tmpBackupName);
        final Date _tmpBackupTimestamp;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfBackupTimestamp)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfBackupTimestamp);
        }
        _tmpBackupTimestamp = DateConverter.toDate(_tmp);
        _item.setBackupTimestamp(_tmpBackupTimestamp);
        final long _tmpBackupSize;
        _tmpBackupSize = _cursor.getLong(_cursorIndexOfBackupSize);
        _item.setBackupSize(_tmpBackupSize);
        final String _tmpBackupPath;
        if (_cursor.isNull(_cursorIndexOfBackupPath)) {
          _tmpBackupPath = null;
        } else {
          _tmpBackupPath = _cursor.getString(_cursorIndexOfBackupPath);
        }
        _item.setBackupPath(_tmpBackupPath);
        final boolean _tmpIsEncrypted;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsEncrypted);
        _tmpIsEncrypted = _tmp_1 != 0;
        _item.setEncrypted(_tmpIsEncrypted);
        final String _tmpEncryptionAlgorithm;
        if (_cursor.isNull(_cursorIndexOfEncryptionAlgorithm)) {
          _tmpEncryptionAlgorithm = null;
        } else {
          _tmpEncryptionAlgorithm = _cursor.getString(_cursorIndexOfEncryptionAlgorithm);
        }
        _item.setEncryptionAlgorithm(_tmpEncryptionAlgorithm);
        final BackupType _tmpBackupType;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfBackupType)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfBackupType);
        }
        _tmpBackupType = BackupTypeConverter.fromString(_tmp_2);
        _item.setBackupType(_tmpBackupType);
        final BackupStatus _tmpStatus;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfStatus);
        }
        _tmpStatus = BackupStatusConverter.fromString(_tmp_3);
        _item.setStatus(_tmpStatus);
        final Date _tmpCompletedAt;
        final Long _tmp_4;
        if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getLong(_cursorIndexOfCompletedAt);
        }
        _tmpCompletedAt = DateConverter.toDate(_tmp_4);
        _item.setCompletedAt(_tmpCompletedAt);
        final Date _tmpRestoredAt;
        final Long _tmp_5;
        if (_cursor.isNull(_cursorIndexOfRestoredAt)) {
          _tmp_5 = null;
        } else {
          _tmp_5 = _cursor.getLong(_cursorIndexOfRestoredAt);
        }
        _tmpRestoredAt = DateConverter.toDate(_tmp_5);
        _item.setRestoredAt(_tmpRestoredAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Backup> getByType(final String backupType) {
    final String _sql = "SELECT * FROM backups WHERE backupType = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (backupType == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, backupType);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBackupId = CursorUtil.getColumnIndexOrThrow(_cursor, "backupId");
      final int _cursorIndexOfBackupName = CursorUtil.getColumnIndexOrThrow(_cursor, "backupName");
      final int _cursorIndexOfBackupTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "backupTimestamp");
      final int _cursorIndexOfBackupSize = CursorUtil.getColumnIndexOrThrow(_cursor, "backupSize");
      final int _cursorIndexOfBackupPath = CursorUtil.getColumnIndexOrThrow(_cursor, "backupPath");
      final int _cursorIndexOfIsEncrypted = CursorUtil.getColumnIndexOrThrow(_cursor, "isEncrypted");
      final int _cursorIndexOfEncryptionAlgorithm = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptionAlgorithm");
      final int _cursorIndexOfBackupType = CursorUtil.getColumnIndexOrThrow(_cursor, "backupType");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
      final int _cursorIndexOfRestoredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "restoredAt");
      final List<Backup> _result = new ArrayList<Backup>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Backup _item;
        _item = new Backup();
        final String _tmpBackupId;
        if (_cursor.isNull(_cursorIndexOfBackupId)) {
          _tmpBackupId = null;
        } else {
          _tmpBackupId = _cursor.getString(_cursorIndexOfBackupId);
        }
        _item.setBackupId(_tmpBackupId);
        final String _tmpBackupName;
        if (_cursor.isNull(_cursorIndexOfBackupName)) {
          _tmpBackupName = null;
        } else {
          _tmpBackupName = _cursor.getString(_cursorIndexOfBackupName);
        }
        _item.setBackupName(_tmpBackupName);
        final Date _tmpBackupTimestamp;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfBackupTimestamp)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfBackupTimestamp);
        }
        _tmpBackupTimestamp = DateConverter.toDate(_tmp);
        _item.setBackupTimestamp(_tmpBackupTimestamp);
        final long _tmpBackupSize;
        _tmpBackupSize = _cursor.getLong(_cursorIndexOfBackupSize);
        _item.setBackupSize(_tmpBackupSize);
        final String _tmpBackupPath;
        if (_cursor.isNull(_cursorIndexOfBackupPath)) {
          _tmpBackupPath = null;
        } else {
          _tmpBackupPath = _cursor.getString(_cursorIndexOfBackupPath);
        }
        _item.setBackupPath(_tmpBackupPath);
        final boolean _tmpIsEncrypted;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsEncrypted);
        _tmpIsEncrypted = _tmp_1 != 0;
        _item.setEncrypted(_tmpIsEncrypted);
        final String _tmpEncryptionAlgorithm;
        if (_cursor.isNull(_cursorIndexOfEncryptionAlgorithm)) {
          _tmpEncryptionAlgorithm = null;
        } else {
          _tmpEncryptionAlgorithm = _cursor.getString(_cursorIndexOfEncryptionAlgorithm);
        }
        _item.setEncryptionAlgorithm(_tmpEncryptionAlgorithm);
        final BackupType _tmpBackupType;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfBackupType)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfBackupType);
        }
        _tmpBackupType = BackupTypeConverter.fromString(_tmp_2);
        _item.setBackupType(_tmpBackupType);
        final BackupStatus _tmpStatus;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfStatus);
        }
        _tmpStatus = BackupStatusConverter.fromString(_tmp_3);
        _item.setStatus(_tmpStatus);
        final Date _tmpCompletedAt;
        final Long _tmp_4;
        if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getLong(_cursorIndexOfCompletedAt);
        }
        _tmpCompletedAt = DateConverter.toDate(_tmp_4);
        _item.setCompletedAt(_tmpCompletedAt);
        final Date _tmpRestoredAt;
        final Long _tmp_5;
        if (_cursor.isNull(_cursorIndexOfRestoredAt)) {
          _tmp_5 = null;
        } else {
          _tmp_5 = _cursor.getLong(_cursorIndexOfRestoredAt);
        }
        _tmpRestoredAt = DateConverter.toDate(_tmp_5);
        _item.setRestoredAt(_tmpRestoredAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Backup> getByEncryptionStatus(final boolean isEncrypted) {
    final String _sql = "SELECT * FROM backups WHERE isEncrypted = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final int _tmp = isEncrypted ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBackupId = CursorUtil.getColumnIndexOrThrow(_cursor, "backupId");
      final int _cursorIndexOfBackupName = CursorUtil.getColumnIndexOrThrow(_cursor, "backupName");
      final int _cursorIndexOfBackupTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "backupTimestamp");
      final int _cursorIndexOfBackupSize = CursorUtil.getColumnIndexOrThrow(_cursor, "backupSize");
      final int _cursorIndexOfBackupPath = CursorUtil.getColumnIndexOrThrow(_cursor, "backupPath");
      final int _cursorIndexOfIsEncrypted = CursorUtil.getColumnIndexOrThrow(_cursor, "isEncrypted");
      final int _cursorIndexOfEncryptionAlgorithm = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptionAlgorithm");
      final int _cursorIndexOfBackupType = CursorUtil.getColumnIndexOrThrow(_cursor, "backupType");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
      final int _cursorIndexOfRestoredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "restoredAt");
      final List<Backup> _result = new ArrayList<Backup>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Backup _item;
        _item = new Backup();
        final String _tmpBackupId;
        if (_cursor.isNull(_cursorIndexOfBackupId)) {
          _tmpBackupId = null;
        } else {
          _tmpBackupId = _cursor.getString(_cursorIndexOfBackupId);
        }
        _item.setBackupId(_tmpBackupId);
        final String _tmpBackupName;
        if (_cursor.isNull(_cursorIndexOfBackupName)) {
          _tmpBackupName = null;
        } else {
          _tmpBackupName = _cursor.getString(_cursorIndexOfBackupName);
        }
        _item.setBackupName(_tmpBackupName);
        final Date _tmpBackupTimestamp;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfBackupTimestamp)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfBackupTimestamp);
        }
        _tmpBackupTimestamp = DateConverter.toDate(_tmp_1);
        _item.setBackupTimestamp(_tmpBackupTimestamp);
        final long _tmpBackupSize;
        _tmpBackupSize = _cursor.getLong(_cursorIndexOfBackupSize);
        _item.setBackupSize(_tmpBackupSize);
        final String _tmpBackupPath;
        if (_cursor.isNull(_cursorIndexOfBackupPath)) {
          _tmpBackupPath = null;
        } else {
          _tmpBackupPath = _cursor.getString(_cursorIndexOfBackupPath);
        }
        _item.setBackupPath(_tmpBackupPath);
        final boolean _tmpIsEncrypted;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIsEncrypted);
        _tmpIsEncrypted = _tmp_2 != 0;
        _item.setEncrypted(_tmpIsEncrypted);
        final String _tmpEncryptionAlgorithm;
        if (_cursor.isNull(_cursorIndexOfEncryptionAlgorithm)) {
          _tmpEncryptionAlgorithm = null;
        } else {
          _tmpEncryptionAlgorithm = _cursor.getString(_cursorIndexOfEncryptionAlgorithm);
        }
        _item.setEncryptionAlgorithm(_tmpEncryptionAlgorithm);
        final BackupType _tmpBackupType;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfBackupType)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfBackupType);
        }
        _tmpBackupType = BackupTypeConverter.fromString(_tmp_3);
        _item.setBackupType(_tmpBackupType);
        final BackupStatus _tmpStatus;
        final String _tmp_4;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getString(_cursorIndexOfStatus);
        }
        _tmpStatus = BackupStatusConverter.fromString(_tmp_4);
        _item.setStatus(_tmpStatus);
        final Date _tmpCompletedAt;
        final Long _tmp_5;
        if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
          _tmp_5 = null;
        } else {
          _tmp_5 = _cursor.getLong(_cursorIndexOfCompletedAt);
        }
        _tmpCompletedAt = DateConverter.toDate(_tmp_5);
        _item.setCompletedAt(_tmpCompletedAt);
        final Date _tmpRestoredAt;
        final Long _tmp_6;
        if (_cursor.isNull(_cursorIndexOfRestoredAt)) {
          _tmp_6 = null;
        } else {
          _tmp_6 = _cursor.getLong(_cursorIndexOfRestoredAt);
        }
        _tmpRestoredAt = DateConverter.toDate(_tmp_6);
        _item.setRestoredAt(_tmpRestoredAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Backup> getAllOrderedByTimestamp() {
    final String _sql = "SELECT * FROM backups ORDER BY backupTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfBackupId = CursorUtil.getColumnIndexOrThrow(_cursor, "backupId");
      final int _cursorIndexOfBackupName = CursorUtil.getColumnIndexOrThrow(_cursor, "backupName");
      final int _cursorIndexOfBackupTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "backupTimestamp");
      final int _cursorIndexOfBackupSize = CursorUtil.getColumnIndexOrThrow(_cursor, "backupSize");
      final int _cursorIndexOfBackupPath = CursorUtil.getColumnIndexOrThrow(_cursor, "backupPath");
      final int _cursorIndexOfIsEncrypted = CursorUtil.getColumnIndexOrThrow(_cursor, "isEncrypted");
      final int _cursorIndexOfEncryptionAlgorithm = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptionAlgorithm");
      final int _cursorIndexOfBackupType = CursorUtil.getColumnIndexOrThrow(_cursor, "backupType");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
      final int _cursorIndexOfRestoredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "restoredAt");
      final List<Backup> _result = new ArrayList<Backup>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Backup _item;
        _item = new Backup();
        final String _tmpBackupId;
        if (_cursor.isNull(_cursorIndexOfBackupId)) {
          _tmpBackupId = null;
        } else {
          _tmpBackupId = _cursor.getString(_cursorIndexOfBackupId);
        }
        _item.setBackupId(_tmpBackupId);
        final String _tmpBackupName;
        if (_cursor.isNull(_cursorIndexOfBackupName)) {
          _tmpBackupName = null;
        } else {
          _tmpBackupName = _cursor.getString(_cursorIndexOfBackupName);
        }
        _item.setBackupName(_tmpBackupName);
        final Date _tmpBackupTimestamp;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfBackupTimestamp)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfBackupTimestamp);
        }
        _tmpBackupTimestamp = DateConverter.toDate(_tmp);
        _item.setBackupTimestamp(_tmpBackupTimestamp);
        final long _tmpBackupSize;
        _tmpBackupSize = _cursor.getLong(_cursorIndexOfBackupSize);
        _item.setBackupSize(_tmpBackupSize);
        final String _tmpBackupPath;
        if (_cursor.isNull(_cursorIndexOfBackupPath)) {
          _tmpBackupPath = null;
        } else {
          _tmpBackupPath = _cursor.getString(_cursorIndexOfBackupPath);
        }
        _item.setBackupPath(_tmpBackupPath);
        final boolean _tmpIsEncrypted;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsEncrypted);
        _tmpIsEncrypted = _tmp_1 != 0;
        _item.setEncrypted(_tmpIsEncrypted);
        final String _tmpEncryptionAlgorithm;
        if (_cursor.isNull(_cursorIndexOfEncryptionAlgorithm)) {
          _tmpEncryptionAlgorithm = null;
        } else {
          _tmpEncryptionAlgorithm = _cursor.getString(_cursorIndexOfEncryptionAlgorithm);
        }
        _item.setEncryptionAlgorithm(_tmpEncryptionAlgorithm);
        final BackupType _tmpBackupType;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfBackupType)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfBackupType);
        }
        _tmpBackupType = BackupTypeConverter.fromString(_tmp_2);
        _item.setBackupType(_tmpBackupType);
        final BackupStatus _tmpStatus;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfStatus);
        }
        _tmpStatus = BackupStatusConverter.fromString(_tmp_3);
        _item.setStatus(_tmpStatus);
        final Date _tmpCompletedAt;
        final Long _tmp_4;
        if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getLong(_cursorIndexOfCompletedAt);
        }
        _tmpCompletedAt = DateConverter.toDate(_tmp_4);
        _item.setCompletedAt(_tmpCompletedAt);
        final Date _tmpRestoredAt;
        final Long _tmp_5;
        if (_cursor.isNull(_cursorIndexOfRestoredAt)) {
          _tmp_5 = null;
        } else {
          _tmp_5 = _cursor.getLong(_cursorIndexOfRestoredAt);
        }
        _tmpRestoredAt = DateConverter.toDate(_tmp_5);
        _item.setRestoredAt(_tmpRestoredAt);
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
