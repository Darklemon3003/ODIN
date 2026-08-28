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
import com.odin.core.customization.data.converters.ActionListConverter;
import com.odin.core.customization.data.converters.ConditionListConverter;
import com.odin.core.customization.data.converters.DateConverter;
import com.odin.core.customization.data.converters.RulePriorityConverter;
import com.odin.core.customization.data.converters.TriggerConverter;
import com.odin.core.customization.model.Action;
import com.odin.core.customization.model.AutomationRule;
import com.odin.core.customization.model.Condition;
import com.odin.core.customization.model.RulePriority;
import com.odin.core.customization.model.Trigger;
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
public final class AutomationRuleDao_Impl implements AutomationRuleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AutomationRule> __insertionAdapterOfAutomationRule;

  private final EntityDeletionOrUpdateAdapter<AutomationRule> __deletionAdapterOfAutomationRule;

  private final EntityDeletionOrUpdateAdapter<AutomationRule> __updateAdapterOfAutomationRule;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public AutomationRuleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAutomationRule = new EntityInsertionAdapter<AutomationRule>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `automation_rules` (`ruleId`,`ruleName`,`enabled`,`trigger`,`conditions`,`actions`,`priority`,`createdAt`,`updatedAt`,`lastExecuted`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final AutomationRule entity) {
        if (entity.getRuleId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getRuleId());
        }
        if (entity.getRuleName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getRuleName());
        }
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(3, _tmp);
        final String _tmp_1 = TriggerConverter.toString(entity.getTrigger());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_1);
        }
        final String _tmp_2 = ConditionListConverter.fromConditionList(entity.getConditions());
        if (_tmp_2 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_2);
        }
        final String _tmp_3 = ActionListConverter.fromActionList(entity.getActions());
        if (_tmp_3 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_3);
        }
        final String _tmp_4 = RulePriorityConverter.toString(entity.getPriority());
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
        final Long _tmp_7 = DateConverter.fromDate(entity.getLastExecuted());
        if (_tmp_7 == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, _tmp_7);
        }
      }
    };
    this.__deletionAdapterOfAutomationRule = new EntityDeletionOrUpdateAdapter<AutomationRule>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `automation_rules` WHERE `ruleId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final AutomationRule entity) {
        if (entity.getRuleId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getRuleId());
        }
      }
    };
    this.__updateAdapterOfAutomationRule = new EntityDeletionOrUpdateAdapter<AutomationRule>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `automation_rules` SET `ruleId` = ?,`ruleName` = ?,`enabled` = ?,`trigger` = ?,`conditions` = ?,`actions` = ?,`priority` = ?,`createdAt` = ?,`updatedAt` = ?,`lastExecuted` = ? WHERE `ruleId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final AutomationRule entity) {
        if (entity.getRuleId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getRuleId());
        }
        if (entity.getRuleName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getRuleName());
        }
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(3, _tmp);
        final String _tmp_1 = TriggerConverter.toString(entity.getTrigger());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_1);
        }
        final String _tmp_2 = ConditionListConverter.fromConditionList(entity.getConditions());
        if (_tmp_2 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_2);
        }
        final String _tmp_3 = ActionListConverter.fromActionList(entity.getActions());
        if (_tmp_3 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_3);
        }
        final String _tmp_4 = RulePriorityConverter.toString(entity.getPriority());
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
        final Long _tmp_7 = DateConverter.fromDate(entity.getLastExecuted());
        if (_tmp_7 == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, _tmp_7);
        }
        if (entity.getRuleId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getRuleId());
        }
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM automation_rules WHERE ruleId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM automation_rules";
        return _query;
      }
    };
  }

  @Override
  public void insert(final AutomationRule rule) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfAutomationRule.insert(rule);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final List<AutomationRule> rules) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfAutomationRule.insert(rules);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final AutomationRule rule) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfAutomationRule.handle(rule);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final AutomationRule rule) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfAutomationRule.handle(rule);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteById(final String ruleId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
    int _argIndex = 1;
    if (ruleId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, ruleId);
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
  public AutomationRule getById(final String ruleId) {
    final String _sql = "SELECT * FROM automation_rules WHERE ruleId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (ruleId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, ruleId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfRuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleId");
      final int _cursorIndexOfRuleName = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleName");
      final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
      final int _cursorIndexOfTrigger = CursorUtil.getColumnIndexOrThrow(_cursor, "trigger");
      final int _cursorIndexOfConditions = CursorUtil.getColumnIndexOrThrow(_cursor, "conditions");
      final int _cursorIndexOfActions = CursorUtil.getColumnIndexOrThrow(_cursor, "actions");
      final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final int _cursorIndexOfLastExecuted = CursorUtil.getColumnIndexOrThrow(_cursor, "lastExecuted");
      final AutomationRule _result;
      if (_cursor.moveToFirst()) {
        _result = new AutomationRule();
        final String _tmpRuleId;
        if (_cursor.isNull(_cursorIndexOfRuleId)) {
          _tmpRuleId = null;
        } else {
          _tmpRuleId = _cursor.getString(_cursorIndexOfRuleId);
        }
        _result.setRuleId(_tmpRuleId);
        final String _tmpRuleName;
        if (_cursor.isNull(_cursorIndexOfRuleName)) {
          _tmpRuleName = null;
        } else {
          _tmpRuleName = _cursor.getString(_cursorIndexOfRuleName);
        }
        _result.setRuleName(_tmpRuleName);
        final boolean _tmpEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfEnabled);
        _tmpEnabled = _tmp != 0;
        _result.setEnabled(_tmpEnabled);
        final Trigger _tmpTrigger;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfTrigger)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfTrigger);
        }
        _tmpTrigger = TriggerConverter.fromString(_tmp_1);
        _result.setTrigger(_tmpTrigger);
        final List<Condition> _tmpConditions;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfConditions)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfConditions);
        }
        _tmpConditions = ConditionListConverter.toConditionList(_tmp_2);
        _result.setConditions(_tmpConditions);
        final List<Action> _tmpActions;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfActions)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfActions);
        }
        _tmpActions = ActionListConverter.toActionList(_tmp_3);
        _result.setActions(_tmpActions);
        final RulePriority _tmpPriority;
        final String _tmp_4;
        if (_cursor.isNull(_cursorIndexOfPriority)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getString(_cursorIndexOfPriority);
        }
        _tmpPriority = RulePriorityConverter.fromString(_tmp_4);
        _result.setPriority(_tmpPriority);
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
        final Date _tmpLastExecuted;
        final Long _tmp_7;
        if (_cursor.isNull(_cursorIndexOfLastExecuted)) {
          _tmp_7 = null;
        } else {
          _tmp_7 = _cursor.getLong(_cursorIndexOfLastExecuted);
        }
        _tmpLastExecuted = DateConverter.toDate(_tmp_7);
        _result.setLastExecuted(_tmpLastExecuted);
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
  public List<AutomationRule> getAll() {
    final String _sql = "SELECT * FROM automation_rules";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfRuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleId");
      final int _cursorIndexOfRuleName = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleName");
      final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
      final int _cursorIndexOfTrigger = CursorUtil.getColumnIndexOrThrow(_cursor, "trigger");
      final int _cursorIndexOfConditions = CursorUtil.getColumnIndexOrThrow(_cursor, "conditions");
      final int _cursorIndexOfActions = CursorUtil.getColumnIndexOrThrow(_cursor, "actions");
      final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final int _cursorIndexOfLastExecuted = CursorUtil.getColumnIndexOrThrow(_cursor, "lastExecuted");
      final List<AutomationRule> _result = new ArrayList<AutomationRule>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final AutomationRule _item;
        _item = new AutomationRule();
        final String _tmpRuleId;
        if (_cursor.isNull(_cursorIndexOfRuleId)) {
          _tmpRuleId = null;
        } else {
          _tmpRuleId = _cursor.getString(_cursorIndexOfRuleId);
        }
        _item.setRuleId(_tmpRuleId);
        final String _tmpRuleName;
        if (_cursor.isNull(_cursorIndexOfRuleName)) {
          _tmpRuleName = null;
        } else {
          _tmpRuleName = _cursor.getString(_cursorIndexOfRuleName);
        }
        _item.setRuleName(_tmpRuleName);
        final boolean _tmpEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfEnabled);
        _tmpEnabled = _tmp != 0;
        _item.setEnabled(_tmpEnabled);
        final Trigger _tmpTrigger;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfTrigger)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfTrigger);
        }
        _tmpTrigger = TriggerConverter.fromString(_tmp_1);
        _item.setTrigger(_tmpTrigger);
        final List<Condition> _tmpConditions;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfConditions)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfConditions);
        }
        _tmpConditions = ConditionListConverter.toConditionList(_tmp_2);
        _item.setConditions(_tmpConditions);
        final List<Action> _tmpActions;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfActions)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfActions);
        }
        _tmpActions = ActionListConverter.toActionList(_tmp_3);
        _item.setActions(_tmpActions);
        final RulePriority _tmpPriority;
        final String _tmp_4;
        if (_cursor.isNull(_cursorIndexOfPriority)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getString(_cursorIndexOfPriority);
        }
        _tmpPriority = RulePriorityConverter.fromString(_tmp_4);
        _item.setPriority(_tmpPriority);
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
        final Date _tmpLastExecuted;
        final Long _tmp_7;
        if (_cursor.isNull(_cursorIndexOfLastExecuted)) {
          _tmp_7 = null;
        } else {
          _tmp_7 = _cursor.getLong(_cursorIndexOfLastExecuted);
        }
        _tmpLastExecuted = DateConverter.toDate(_tmp_7);
        _item.setLastExecuted(_tmpLastExecuted);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<AutomationRule> getByEnabledStatus(final boolean enabled) {
    final String _sql = "SELECT * FROM automation_rules WHERE enabled = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final int _tmp = enabled ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfRuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleId");
      final int _cursorIndexOfRuleName = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleName");
      final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
      final int _cursorIndexOfTrigger = CursorUtil.getColumnIndexOrThrow(_cursor, "trigger");
      final int _cursorIndexOfConditions = CursorUtil.getColumnIndexOrThrow(_cursor, "conditions");
      final int _cursorIndexOfActions = CursorUtil.getColumnIndexOrThrow(_cursor, "actions");
      final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final int _cursorIndexOfLastExecuted = CursorUtil.getColumnIndexOrThrow(_cursor, "lastExecuted");
      final List<AutomationRule> _result = new ArrayList<AutomationRule>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final AutomationRule _item;
        _item = new AutomationRule();
        final String _tmpRuleId;
        if (_cursor.isNull(_cursorIndexOfRuleId)) {
          _tmpRuleId = null;
        } else {
          _tmpRuleId = _cursor.getString(_cursorIndexOfRuleId);
        }
        _item.setRuleId(_tmpRuleId);
        final String _tmpRuleName;
        if (_cursor.isNull(_cursorIndexOfRuleName)) {
          _tmpRuleName = null;
        } else {
          _tmpRuleName = _cursor.getString(_cursorIndexOfRuleName);
        }
        _item.setRuleName(_tmpRuleName);
        final boolean _tmpEnabled;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfEnabled);
        _tmpEnabled = _tmp_1 != 0;
        _item.setEnabled(_tmpEnabled);
        final Trigger _tmpTrigger;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfTrigger)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfTrigger);
        }
        _tmpTrigger = TriggerConverter.fromString(_tmp_2);
        _item.setTrigger(_tmpTrigger);
        final List<Condition> _tmpConditions;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfConditions)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfConditions);
        }
        _tmpConditions = ConditionListConverter.toConditionList(_tmp_3);
        _item.setConditions(_tmpConditions);
        final List<Action> _tmpActions;
        final String _tmp_4;
        if (_cursor.isNull(_cursorIndexOfActions)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getString(_cursorIndexOfActions);
        }
        _tmpActions = ActionListConverter.toActionList(_tmp_4);
        _item.setActions(_tmpActions);
        final RulePriority _tmpPriority;
        final String _tmp_5;
        if (_cursor.isNull(_cursorIndexOfPriority)) {
          _tmp_5 = null;
        } else {
          _tmp_5 = _cursor.getString(_cursorIndexOfPriority);
        }
        _tmpPriority = RulePriorityConverter.fromString(_tmp_5);
        _item.setPriority(_tmpPriority);
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
        final Date _tmpLastExecuted;
        final Long _tmp_8;
        if (_cursor.isNull(_cursorIndexOfLastExecuted)) {
          _tmp_8 = null;
        } else {
          _tmp_8 = _cursor.getLong(_cursorIndexOfLastExecuted);
        }
        _tmpLastExecuted = DateConverter.toDate(_tmp_8);
        _item.setLastExecuted(_tmpLastExecuted);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<AutomationRule> getByPriority(final String priority) {
    final String _sql = "SELECT * FROM automation_rules WHERE priority = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (priority == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, priority);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfRuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleId");
      final int _cursorIndexOfRuleName = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleName");
      final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
      final int _cursorIndexOfTrigger = CursorUtil.getColumnIndexOrThrow(_cursor, "trigger");
      final int _cursorIndexOfConditions = CursorUtil.getColumnIndexOrThrow(_cursor, "conditions");
      final int _cursorIndexOfActions = CursorUtil.getColumnIndexOrThrow(_cursor, "actions");
      final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final int _cursorIndexOfLastExecuted = CursorUtil.getColumnIndexOrThrow(_cursor, "lastExecuted");
      final List<AutomationRule> _result = new ArrayList<AutomationRule>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final AutomationRule _item;
        _item = new AutomationRule();
        final String _tmpRuleId;
        if (_cursor.isNull(_cursorIndexOfRuleId)) {
          _tmpRuleId = null;
        } else {
          _tmpRuleId = _cursor.getString(_cursorIndexOfRuleId);
        }
        _item.setRuleId(_tmpRuleId);
        final String _tmpRuleName;
        if (_cursor.isNull(_cursorIndexOfRuleName)) {
          _tmpRuleName = null;
        } else {
          _tmpRuleName = _cursor.getString(_cursorIndexOfRuleName);
        }
        _item.setRuleName(_tmpRuleName);
        final boolean _tmpEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfEnabled);
        _tmpEnabled = _tmp != 0;
        _item.setEnabled(_tmpEnabled);
        final Trigger _tmpTrigger;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfTrigger)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfTrigger);
        }
        _tmpTrigger = TriggerConverter.fromString(_tmp_1);
        _item.setTrigger(_tmpTrigger);
        final List<Condition> _tmpConditions;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfConditions)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfConditions);
        }
        _tmpConditions = ConditionListConverter.toConditionList(_tmp_2);
        _item.setConditions(_tmpConditions);
        final List<Action> _tmpActions;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfActions)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfActions);
        }
        _tmpActions = ActionListConverter.toActionList(_tmp_3);
        _item.setActions(_tmpActions);
        final RulePriority _tmpPriority;
        final String _tmp_4;
        if (_cursor.isNull(_cursorIndexOfPriority)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getString(_cursorIndexOfPriority);
        }
        _tmpPriority = RulePriorityConverter.fromString(_tmp_4);
        _item.setPriority(_tmpPriority);
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
        final Date _tmpLastExecuted;
        final Long _tmp_7;
        if (_cursor.isNull(_cursorIndexOfLastExecuted)) {
          _tmp_7 = null;
        } else {
          _tmp_7 = _cursor.getLong(_cursorIndexOfLastExecuted);
        }
        _tmpLastExecuted = DateConverter.toDate(_tmp_7);
        _item.setLastExecuted(_tmpLastExecuted);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<AutomationRule> getAllOrderedByPriority() {
    final String _sql = "SELECT * FROM automation_rules ORDER BY priority DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfRuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleId");
      final int _cursorIndexOfRuleName = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleName");
      final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
      final int _cursorIndexOfTrigger = CursorUtil.getColumnIndexOrThrow(_cursor, "trigger");
      final int _cursorIndexOfConditions = CursorUtil.getColumnIndexOrThrow(_cursor, "conditions");
      final int _cursorIndexOfActions = CursorUtil.getColumnIndexOrThrow(_cursor, "actions");
      final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final int _cursorIndexOfLastExecuted = CursorUtil.getColumnIndexOrThrow(_cursor, "lastExecuted");
      final List<AutomationRule> _result = new ArrayList<AutomationRule>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final AutomationRule _item;
        _item = new AutomationRule();
        final String _tmpRuleId;
        if (_cursor.isNull(_cursorIndexOfRuleId)) {
          _tmpRuleId = null;
        } else {
          _tmpRuleId = _cursor.getString(_cursorIndexOfRuleId);
        }
        _item.setRuleId(_tmpRuleId);
        final String _tmpRuleName;
        if (_cursor.isNull(_cursorIndexOfRuleName)) {
          _tmpRuleName = null;
        } else {
          _tmpRuleName = _cursor.getString(_cursorIndexOfRuleName);
        }
        _item.setRuleName(_tmpRuleName);
        final boolean _tmpEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfEnabled);
        _tmpEnabled = _tmp != 0;
        _item.setEnabled(_tmpEnabled);
        final Trigger _tmpTrigger;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfTrigger)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfTrigger);
        }
        _tmpTrigger = TriggerConverter.fromString(_tmp_1);
        _item.setTrigger(_tmpTrigger);
        final List<Condition> _tmpConditions;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfConditions)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfConditions);
        }
        _tmpConditions = ConditionListConverter.toConditionList(_tmp_2);
        _item.setConditions(_tmpConditions);
        final List<Action> _tmpActions;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfActions)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfActions);
        }
        _tmpActions = ActionListConverter.toActionList(_tmp_3);
        _item.setActions(_tmpActions);
        final RulePriority _tmpPriority;
        final String _tmp_4;
        if (_cursor.isNull(_cursorIndexOfPriority)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getString(_cursorIndexOfPriority);
        }
        _tmpPriority = RulePriorityConverter.fromString(_tmp_4);
        _item.setPriority(_tmpPriority);
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
        final Date _tmpLastExecuted;
        final Long _tmp_7;
        if (_cursor.isNull(_cursorIndexOfLastExecuted)) {
          _tmp_7 = null;
        } else {
          _tmp_7 = _cursor.getLong(_cursorIndexOfLastExecuted);
        }
        _tmpLastExecuted = DateConverter.toDate(_tmp_7);
        _item.setLastExecuted(_tmpLastExecuted);
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
