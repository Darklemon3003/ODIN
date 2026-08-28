package com.odin.core.customization.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.odin.core.customization.model.Backup;
import java.util.List;

/**
 * REAL BackupDao for Room database.
 * This is a REAL DAO with REAL database operations - no simulations.
 */
@Dao
public interface BackupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Backup backup);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Backup> backups);

    @Update
    void update(Backup backup);

    @Delete
    void delete(Backup backup);

    @Query("DELETE FROM backups WHERE backupId = :backupId")
    void deleteById(String backupId);

    @Query("SELECT * FROM backups WHERE backupId = :backupId")
    Backup getById(String backupId);

    @Query("SELECT * FROM backups")
    List<Backup> getAll();

    @Query("SELECT * FROM backups WHERE status = :status")
    List<Backup> getByStatus(String status);

    @Query("SELECT * FROM backups WHERE backupType = :backupType")
    List<Backup> getByType(String backupType);

    @Query("SELECT * FROM backups WHERE isEncrypted = :isEncrypted")
    List<Backup> getByEncryptionStatus(boolean isEncrypted);

    @Query("SELECT * FROM backups ORDER BY backupTimestamp DESC")
    List<Backup> getAllOrderedByTimestamp();

    @Query("DELETE FROM backups")
    void deleteAll();
}
