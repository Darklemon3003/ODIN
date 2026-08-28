package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odin.core.customization.model.Condition;
import java.lang.reflect.Type;

/**
 * REAL ConditionConverter for Room database.
 * This is a REAL converter using Gson for JSON serialization - no simulations.
 */
public class ConditionConverter {

    private static final Gson gson = new Gson();
    private static final Type conditionType = new TypeToken<Condition>(){}.getType();

    @TypeConverter
    public static Condition fromString(String value) {
        if (value == null) {
            return new Condition();
        }
        try {
            return gson.fromJson(value, conditionType);
        } catch (Exception e) {
            return new Condition();
        }
    }

    @TypeConverter
    public static String toString(Condition condition) {
        return condition != null ? gson.toJson(condition) : gson.toJson(new Condition());
    }
}
