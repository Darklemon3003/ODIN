package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.odin.core.customization.model.Condition;
import java.lang.reflect.Type;
import java.util.List;

/**
 * ConditionListConverter para Room.
 * No convierte silenciosamente datos corruptos en null valido.
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
        try {
            return gson.fromJson(conditionsJson, type);
        } catch (JsonSyntaxException e) {
            throw new ConverterCorruptionException(
                    "Datos corruptos en columna de ConditionList: JSON invalido", e);
        } catch (Exception e) {
            throw new ConverterCorruptionException(
                    "Error al deserializar ConditionList", e);
        }
    }
}
