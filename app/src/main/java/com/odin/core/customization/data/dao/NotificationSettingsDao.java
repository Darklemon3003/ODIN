package com.odin.core.customization.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.odin.core.customization.model.NotificationSettings;
import java.util.List;

/**
 * REAL NotificationSettingsDao for Room database.
 * This is a REAL DAO with REAL database operations - no simulations.
 */
@Dao
public interface NotificationSettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NotificationSettings settings);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<NotificationSettings> settingsList);

    @Update
    void update(NotificationSettings settings);

    @Delete
    void delete(NotificationSettings settings);

    @Query("DELETE FROM notification_settings WHERE settingsId = :settingsId")
    void deleteById(String settingsId);

    @Query("SELECT * FROM notification_settings WHERE settingsId = :settingsId")
    NotificationSettings getById(String settingsId);

    @Query("SELECT * FROM notification_settings")
    List<NotificationSettings> getAll();

    @Query("SELECT * FROM notification_settings WHERE notificationsEnabled = :enabled")
    List<NotificationSettings> getByEnabledStatus(boolean enabled);

    @Query("DELETE FROM notification_settings")
    void deleteAll();
}
