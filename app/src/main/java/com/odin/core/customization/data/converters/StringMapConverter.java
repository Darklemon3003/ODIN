package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * StringMapConverter para Room.
 * No convierte silenciosamente datos corruptos en null valido.
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
        try {
            return gson.fromJson(mapJson, type);
        } catch (JsonSyntaxException e) {
            throw new ConverterCorruptionException(
                    "Datos corruptos en columna de StringMap: JSON invalido", e);
        } catch (Exception e) {
            throw new ConverterCorruptionException(
                    "Error al deserializar StringMap", e);
        }
    }
}
