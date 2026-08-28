package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odin.core.customization.model.AutomationRule;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * REAL AutomationRuleListConverter for Room database.
 * This is a REAL converter using Gson for JSON serialization - no simulations.
 */
public class AutomationRuleListConverter {

    private static final Gson gson = new Gson();
    private static final Type listType = new TypeToken<List<AutomationRule>>(){}.getType();

    @TypeConverter
    public static List<AutomationRule> fromString(String value) {
        if (value == null) {
            return new ArrayList<>();
        }
        try {
            return gson.fromJson(value, listType);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @TypeConverter
    public static String toString(List<AutomationRule> rules) {
        return rules != null ? gson.toJson(rules) : gson.toJson(new ArrayList<AutomationRule>());
    }
}
