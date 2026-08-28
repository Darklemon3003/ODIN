package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odin.core.customization.model.Condition;
import java.lang.reflect.Type;
import java.util.List;

/**
 * REAL ConditionListConverter for ODIN customization system.
 * This is a REAL converter with REAL List<Condition> serialization - no simulations.
 */
public class ConditionListConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static String fromConditionList(List<Condition> conditions) {
        if (conditions == null) {
            return null;
        }
        return gson.toJson(conditions);
    }

    @TypeConverter
    public static List<Condition> toConditionList(String conditionsJson) {
        if (conditionsJson == null) {
            return null;
        }
        Type type = new TypeToken<List<Condition>>(){}.getType();
        return gson.fromJson(conditionsJson, type);
    }
}
