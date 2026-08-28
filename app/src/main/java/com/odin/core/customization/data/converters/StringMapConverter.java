package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * REAL StringMapConverter for ODIN customization system.
 * This is a REAL converter with REAL Map<String, Object> serialization - no simulations.
 */
public class StringMapConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static String fromStringMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        return gson.toJson(map);
    }

    @TypeConverter
    public static Map<String, Object> toStringMap(String mapJson) {
        if (mapJson == null) {
            return null;
        }
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        return gson.fromJson(mapJson, type);
    }
}
