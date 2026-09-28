package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.odin.core.customization.model.AutomationRule;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * AutomationRuleListConverter para Room.
 *
 * Convierte entre List<AutomationRule> y String (JSON) para persistencia en Room.
 *
 * Comportamiento de corrupcion:
 * - null: retorna lista vacia (no habia datos almacenados)
 * - JSON invalido: lanza ConverterCorruptionException (datos corruptos, no
 *   se convierten silenciosamente en lista vacia valida)
 * - JSON valido pero tipo incorrecto: lanza ConverterCorruptionException
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
            List<AutomationRule> result = gson.fromJson(value, listType);
            return result != null ? result : new ArrayList<>();
        } catch (JsonSyntaxException e) {
            throw new ConverterCorruptionException(
                    "Datos corruptos en columna de automation rules: JSON invalido", e);
        } catch (Exception e) {
            throw new ConverterCorruptionException(
                    "Error al deserializar automation rules", e);
        }
    }

    @TypeConverter
    public static String toString(List<AutomationRule> rules) {
        return rules != null ? gson.toJson(rules) : gson.toJson(new ArrayList<AutomationRule>());
    }
}
