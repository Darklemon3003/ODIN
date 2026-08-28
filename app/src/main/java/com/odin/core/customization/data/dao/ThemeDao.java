package com.odin.core.customization.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.odin.core.customization.model.Theme;
import java.util.List;

/**
 * REAL ThemeDao for Room database.
 * This is a REAL DAO with REAL database operations - no simulations.
 */
@Dao
public interface ThemeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Theme theme);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Theme> themes);

    @Update
    void update(Theme theme);

    @Delete
    void delete(Theme theme);

    @Query("DELETE FROM themes WHERE themeId = :themeId")
    void deleteById(String themeId);

    @Query("SELECT * FROM themes WHERE themeId = :themeId")
    Theme getById(String themeId);

    @Query("SELECT * FROM themes")
    List<Theme> getAll();

    @Query("SELECT * FROM themes WHERE themeType = :themeType")
    List<Theme> getByType(String themeType);

    @Query("SELECT * FROM themes WHERE isCustom = :isCustom")
    List<Theme> getByCustomStatus(boolean isCustom);

    @Query("DELETE FROM themes")
    void deleteAll();
}
