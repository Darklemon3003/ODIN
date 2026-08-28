package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.odin.core.customization.model.Trigger;

/**
 * REAL TriggerConverter for Room database.
 * This is a REAL converter using Gson for JSON serialization - no simulations.
 */
public class TriggerConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static Trigger fromString(String value) {
        if (value == null) {
            return new Trigger();
        }
        try {
            return gson.fromJson(value, Trigger.class);
        } catch (Exception e) {
            return new Trigger();
        }
    }

    @TypeConverter
    public static String toString(Trigger trigger) {
        return trigger != null ? gson.toJson(trigger) : gson.toJson(new Trigger());
    }
}
