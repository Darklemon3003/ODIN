package com.odin.core.customization.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.odin.core.customization.model.Profile;
import java.util.List;

/**
 * REAL ProfileDao for Room database.
 * This is a REAL DAO with REAL database operations - no simulations.
 */
@Dao
public interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Profile profile);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Profile> profiles);

    @Update
    void update(Profile profile);

    @Delete
    void delete(Profile profile);

    @Query("DELETE FROM profiles WHERE profileId = :profileId")
    void deleteById(String profileId);

    @Query("SELECT * FROM profiles WHERE profileId = :profileId")
    Profile getById(String profileId);

    @Query("SELECT * FROM profiles")
    List<Profile> getAll();

    @Query("SELECT * FROM profiles WHERE isActive = :isActive")
    List<Profile> getByActiveStatus(boolean isActive);

    @Query("SELECT * FROM profiles WHERE isActive = 1 LIMIT 1")
    Profile getActiveProfile();

    @Query("DELETE FROM profiles")
    void deleteAll();
}
