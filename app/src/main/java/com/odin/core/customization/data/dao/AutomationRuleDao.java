package com.odin.core.customization.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.odin.core.customization.model.AutomationRule;
import java.util.List;

/**
 * REAL AutomationRuleDao for Room database.
 * This is a REAL DAO with REAL database operations - no simulations.
 */
@Dao
public interface AutomationRuleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AutomationRule rule);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AutomationRule> rules);

    @Update
    void update(AutomationRule rule);

    @Delete
    void delete(AutomationRule rule);

    @Query("DELETE FROM automation_rules WHERE ruleId = :ruleId")
    void deleteById(String ruleId);

    @Query("SELECT * FROM automation_rules WHERE ruleId = :ruleId")
    AutomationRule getById(String ruleId);

    @Query("SELECT * FROM automation_rules")
    List<AutomationRule> getAll();

    @Query("SELECT * FROM automation_rules WHERE enabled = :enabled")
    List<AutomationRule> getByEnabledStatus(boolean enabled);

    @Query("SELECT * FROM automation_rules WHERE priority = :priority")
    List<AutomationRule> getByPriority(String priority);

    @Query("SELECT * FROM automation_rules ORDER BY priority DESC")
    List<AutomationRule> getAllOrderedByPriority();

    @Query("DELETE FROM automation_rules")
    void deleteAll();
}
