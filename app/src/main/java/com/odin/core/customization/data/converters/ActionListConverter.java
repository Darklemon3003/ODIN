package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odin.core.customization.model.Action;
import java.lang.reflect.Type;
import java.util.List;

/**
 * REAL ActionListConverter for ODIN customization system.
 * This is a REAL converter with REAL List<Action> serialization - no simulations.
 */
public class ActionListConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static String fromActionList(List<Action> actions) {
        if (actions == null) {
            return null;
        }
        return gson.toJson(actions);
    }

    @TypeConverter
    public static List<Action> toActionList(String actionsJson) {
        if (actionsJson == null) {
            return null;
        }
        Type type = new TypeToken<List<Action>>(){}.getType();
        return gson.fromJson(actionsJson, type);
    }
}
