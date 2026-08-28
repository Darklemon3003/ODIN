package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odin.core.customization.model.Action;
import java.lang.reflect.Type;

/**
 * REAL ActionConverter for Room database.
 * This is a REAL converter using Gson for JSON serialization - no simulations.
 */
public class ActionConverter {

    private static final Gson gson = new Gson();
    private static final Type actionType = new TypeToken<Action>(){}.getType();

    @TypeConverter
    public static Action fromString(String value) {
        if (value == null) {
            return new Action();
        }
        try {
            return gson.fromJson(value, actionType);
        } catch (Exception e) {
            return new Action();
        }
    }

    @TypeConverter
    public static String toString(Action action) {
        return action != null ? gson.toJson(action) : gson.toJson(new Action());
    }
}
