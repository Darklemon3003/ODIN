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
import com.odin.core.customization.data.converters.ColorSchemeConverter;
import com.odin.core.customization.data.converters.DateConverter;
import com.odin.core.customization.data.converters.FontSchemeConverter;
import com.odin.core.customization.data.converters.IconSchemeConverter;
import com.odin.core.customization.data.converters.ThemeTypeConverter;
import com.odin.core.customization.model.ColorScheme;
import com.odin.core.customization.model.FontScheme;
import com.odin.core.customization.model.IconScheme;
import com.odin.core.customization.model.Theme;
import com.odin.core.customization.model.ThemeType;
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
public final class ThemeDao_Impl implements ThemeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Theme> __insertionAdapterOfTheme;

  private final EntityDeletionOrUpdateAdapter<Theme> __deletionAdapterOfTheme;

  private final EntityDeletionOrUpdateAdapter<Theme> __updateAdapterOfTheme;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public ThemeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTheme = new EntityInsertionAdapter<Theme>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `themes` (`themeId`,`themeName`,`themeType`,`colorScheme`,`fontScheme`,`iconScheme`,`isCustom`,`createdAt`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Theme entity) {
        if (entity.getThemeId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getThemeId());
        }
        if (entity.getThemeName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getThemeName());
        }
        final String _tmp = ThemeTypeConverter.toString(entity.getThemeType());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        final String _tmp_1 = ColorSchemeConverter.toString(entity.getColorScheme());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_1);
        }
        final String _tmp_2 = FontSchemeConverter.toString(entity.getFontScheme());
        if (_tmp_2 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_2);
        }
        final String _tmp_3 = IconSchemeConverter.toString(entity.getIconScheme());
        if (_tmp_3 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_3);
        }
        final int _tmp_4 = entity.isCustom() ? 1 : 0;
        statement.bindLong(7, _tmp_4);
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
      }
    };
    this.__deletionAdapterOfTheme = new EntityDeletionOrUpdateAdapter<Theme>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `themes` WHERE `themeId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Theme entity) {
        if (entity.getThemeId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getThemeId());
        }
      }
    };
    this.__updateAdapterOfTheme = new EntityDeletionOrUpdateAdapter<Theme>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `themes` SET `themeId` = ?,`themeName` = ?,`themeType` = ?,`colorScheme` = ?,`fontScheme` = ?,`iconScheme` = ?,`isCustom` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `themeId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Theme entity) {
        if (entity.getThemeId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getThemeId());
        }
        if (entity.getThemeName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getThemeName());
        }
        final String _tmp = ThemeTypeConverter.toString(entity.getThemeType());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        final String _tmp_1 = ColorSchemeConverter.toString(entity.getColorScheme());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_1);
        }
        final String _tmp_2 = FontSchemeConverter.toString(entity.getFontScheme());
        if (_tmp_2 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_2);
        }
        final String _tmp_3 = IconSchemeConverter.toString(entity.getIconScheme());
        if (_tmp_3 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_3);
        }
        final int _tmp_4 = entity.isCustom() ? 1 : 0;
        statement.bindLong(7, _tmp_4);
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
        if (entity.getThemeId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getThemeId());
        }
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM themes WHERE themeId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM themes";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Theme theme) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTheme.insert(theme);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final List<Theme> themes) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTheme.insert(themes);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Theme theme) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfTheme.handle(theme);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Theme theme) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfTheme.handle(theme);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteById(final String themeId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
    int _argIndex = 1;
    if (themeId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, themeId);
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
  public Theme getById(final String themeId) {
    final String _sql = "SELECT * FROM themes WHERE themeId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (themeId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, themeId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfThemeId = CursorUtil.getColumnIndexOrThrow(_cursor, "themeId");
      final int _cursorIndexOfThemeName = CursorUtil.getColumnIndexOrThrow(_cursor, "themeName");
      final int _cursorIndexOfThemeType = CursorUtil.getColumnIndexOrThrow(_cursor, "themeType");
      final int _cursorIndexOfColorScheme = CursorUtil.getColumnIndexOrThrow(_cursor, "colorScheme");
      final int _cursorIndexOfFontScheme = CursorUtil.getColumnIndexOrThrow(_cursor, "fontScheme");
      final int _cursorIndexOfIconScheme = CursorUtil.getColumnIndexOrThrow(_cursor, "iconScheme");
      final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "isCustom");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final Theme _result;
      if (_cursor.moveToFirst()) {
        _result = new Theme();
        final String _tmpThemeId;
        if (_cursor.isNull(_cursorIndexOfThemeId)) {
          _tmpThemeId = null;
        } else {
          _tmpThemeId = _cursor.getString(_cursorIndexOfThemeId);
        }
        _result.setThemeId(_tmpThemeId);
        final String _tmpThemeName;
        if (_cursor.isNull(_cursorIndexOfThemeName)) {
          _tmpThemeName = null;
        } else {
          _tmpThemeName = _cursor.getString(_cursorIndexOfThemeName);
        }
        _result.setThemeName(_tmpThemeName);
        final ThemeType _tmpThemeType;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfThemeType)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfThemeType);
        }
        _tmpThemeType = ThemeTypeConverter.fromString(_tmp);
        _result.setThemeType(_tmpThemeType);
        final ColorScheme _tmpColorScheme;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfColorScheme)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfColorScheme);
        }
        _tmpColorScheme = ColorSchemeConverter.fromString(_tmp_1);
        _result.setColorScheme(_tmpColorScheme);
        final FontScheme _tmpFontScheme;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfFontScheme)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfFontScheme);
        }
        _tmpFontScheme = FontSchemeConverter.fromString(_tmp_2);
        _result.setFontScheme(_tmpFontScheme);
        final IconScheme _tmpIconScheme;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfIconScheme)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfIconScheme);
        }
        _tmpIconScheme = IconSchemeConverter.fromString(_tmp_3);
        _result.setIconScheme(_tmpIconScheme);
        final boolean _tmpIsCustom;
        final int _tmp_4;
        _tmp_4 = _cursor.getInt(_cursorIndexOfIsCustom);
        _tmpIsCustom = _tmp_4 != 0;
        _result.setCustom(_tmpIsCustom);
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
  public List<Theme> getAll() {
    final String _sql = "SELECT * FROM themes";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfThemeId = CursorUtil.getColumnIndexOrThrow(_cursor, "themeId");
      final int _cursorIndexOfThemeName = CursorUtil.getColumnIndexOrThrow(_cursor, "themeName");
      final int _cursorIndexOfThemeType = CursorUtil.getColumnIndexOrThrow(_cursor, "themeType");
      final int _cursorIndexOfColorScheme = CursorUtil.getColumnIndexOrThrow(_cursor, "colorScheme");
      final int _cursorIndexOfFontScheme = CursorUtil.getColumnIndexOrThrow(_cursor, "fontScheme");
      final int _cursorIndexOfIconScheme = CursorUtil.getColumnIndexOrThrow(_cursor, "iconScheme");
      final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "isCustom");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final List<Theme> _result = new ArrayList<Theme>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Theme _item;
        _item = new Theme();
        final String _tmpThemeId;
        if (_cursor.isNull(_cursorIndexOfThemeId)) {
          _tmpThemeId = null;
        } else {
          _tmpThemeId = _cursor.getString(_cursorIndexOfThemeId);
        }
        _item.setThemeId(_tmpThemeId);
        final String _tmpThemeName;
        if (_cursor.isNull(_cursorIndexOfThemeName)) {
          _tmpThemeName = null;
        } else {
          _tmpThemeName = _cursor.getString(_cursorIndexOfThemeName);
        }
        _item.setThemeName(_tmpThemeName);
        final ThemeType _tmpThemeType;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfThemeType)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfThemeType);
        }
        _tmpThemeType = ThemeTypeConverter.fromString(_tmp);
        _item.setThemeType(_tmpThemeType);
        final ColorScheme _tmpColorScheme;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfColorScheme)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfColorScheme);
        }
        _tmpColorScheme = ColorSchemeConverter.fromString(_tmp_1);
        _item.setColorScheme(_tmpColorScheme);
        final FontScheme _tmpFontScheme;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfFontScheme)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfFontScheme);
        }
        _tmpFontScheme = FontSchemeConverter.fromString(_tmp_2);
        _item.setFontScheme(_tmpFontScheme);
        final IconScheme _tmpIconScheme;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfIconScheme)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfIconScheme);
        }
        _tmpIconScheme = IconSchemeConverter.fromString(_tmp_3);
        _item.setIconScheme(_tmpIconScheme);
        final boolean _tmpIsCustom;
        final int _tmp_4;
        _tmp_4 = _cursor.getInt(_cursorIndexOfIsCustom);
        _tmpIsCustom = _tmp_4 != 0;
        _item.setCustom(_tmpIsCustom);
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
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Theme> getByType(final String themeType) {
    final String _sql = "SELECT * FROM themes WHERE themeType = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (themeType == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, themeType);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfThemeId = CursorUtil.getColumnIndexOrThrow(_cursor, "themeId");
      final int _cursorIndexOfThemeName = CursorUtil.getColumnIndexOrThrow(_cursor, "themeName");
      final int _cursorIndexOfThemeType = CursorUtil.getColumnIndexOrThrow(_cursor, "themeType");
      final int _cursorIndexOfColorScheme = CursorUtil.getColumnIndexOrThrow(_cursor, "colorScheme");
      final int _cursorIndexOfFontScheme = CursorUtil.getColumnIndexOrThrow(_cursor, "fontScheme");
      final int _cursorIndexOfIconScheme = CursorUtil.getColumnIndexOrThrow(_cursor, "iconScheme");
      final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "isCustom");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final List<Theme> _result = new ArrayList<Theme>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Theme _item;
        _item = new Theme();
        final String _tmpThemeId;
        if (_cursor.isNull(_cursorIndexOfThemeId)) {
          _tmpThemeId = null;
        } else {
          _tmpThemeId = _cursor.getString(_cursorIndexOfThemeId);
        }
        _item.setThemeId(_tmpThemeId);
        final String _tmpThemeName;
        if (_cursor.isNull(_cursorIndexOfThemeName)) {
          _tmpThemeName = null;
        } else {
          _tmpThemeName = _cursor.getString(_cursorIndexOfThemeName);
        }
        _item.setThemeName(_tmpThemeName);
        final ThemeType _tmpThemeType;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfThemeType)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfThemeType);
        }
        _tmpThemeType = ThemeTypeConverter.fromString(_tmp);
        _item.setThemeType(_tmpThemeType);
        final ColorScheme _tmpColorScheme;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfColorScheme)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfColorScheme);
        }
        _tmpColorScheme = ColorSchemeConverter.fromString(_tmp_1);
        _item.setColorScheme(_tmpColorScheme);
        final FontScheme _tmpFontScheme;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfFontScheme)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfFontScheme);
        }
        _tmpFontScheme = FontSchemeConverter.fromString(_tmp_2);
        _item.setFontScheme(_tmpFontScheme);
        final IconScheme _tmpIconScheme;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfIconScheme)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfIconScheme);
        }
        _tmpIconScheme = IconSchemeConverter.fromString(_tmp_3);
        _item.setIconScheme(_tmpIconScheme);
        final boolean _tmpIsCustom;
        final int _tmp_4;
        _tmp_4 = _cursor.getInt(_cursorIndexOfIsCustom);
        _tmpIsCustom = _tmp_4 != 0;
        _item.setCustom(_tmpIsCustom);
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
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Theme> getByCustomStatus(final boolean isCustom) {
    final String _sql = "SELECT * FROM themes WHERE isCustom = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final int _tmp = isCustom ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfThemeId = CursorUtil.getColumnIndexOrThrow(_cursor, "themeId");
      final int _cursorIndexOfThemeName = CursorUtil.getColumnIndexOrThrow(_cursor, "themeName");
      final int _cursorIndexOfThemeType = CursorUtil.getColumnIndexOrThrow(_cursor, "themeType");
      final int _cursorIndexOfColorScheme = CursorUtil.getColumnIndexOrThrow(_cursor, "colorScheme");
      final int _cursorIndexOfFontScheme = CursorUtil.getColumnIndexOrThrow(_cursor, "fontScheme");
      final int _cursorIndexOfIconScheme = CursorUtil.getColumnIndexOrThrow(_cursor, "iconScheme");
      final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "isCustom");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final List<Theme> _result = new ArrayList<Theme>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Theme _item;
        _item = new Theme();
        final String _tmpThemeId;
        if (_cursor.isNull(_cursorIndexOfThemeId)) {
          _tmpThemeId = null;
        } else {
          _tmpThemeId = _cursor.getString(_cursorIndexOfThemeId);
        }
        _item.setThemeId(_tmpThemeId);
        final String _tmpThemeName;
        if (_cursor.isNull(_cursorIndexOfThemeName)) {
          _tmpThemeName = null;
        } else {
          _tmpThemeName = _cursor.getString(_cursorIndexOfThemeName);
        }
        _item.setThemeName(_tmpThemeName);
        final ThemeType _tmpThemeType;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfThemeType)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfThemeType);
        }
        _tmpThemeType = ThemeTypeConverter.fromString(_tmp_1);
        _item.setThemeType(_tmpThemeType);
        final ColorScheme _tmpColorScheme;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfColorScheme)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfColorScheme);
        }
        _tmpColorScheme = ColorSchemeConverter.fromString(_tmp_2);
        _item.setColorScheme(_tmpColorScheme);
        final FontScheme _tmpFontScheme;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfFontScheme)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfFontScheme);
        }
        _tmpFontScheme = FontSchemeConverter.fromString(_tmp_3);
        _item.setFontScheme(_tmpFontScheme);
        final IconScheme _tmpIconScheme;
        final String _tmp_4;
        if (_cursor.isNull(_cursorIndexOfIconScheme)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getString(_cursorIndexOfIconScheme);
        }
        _tmpIconScheme = IconSchemeConverter.fromString(_tmp_4);
        _item.setIconScheme(_tmpIconScheme);
        final boolean _tmpIsCustom;
        final int _tmp_5;
        _tmp_5 = _cursor.getInt(_cursorIndexOfIsCustom);
        _tmpIsCustom = _tmp_5 != 0;
        _item.setCustom(_tmpIsCustom);
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
