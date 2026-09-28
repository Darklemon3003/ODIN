package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.odin.core.customization.model.Condition;
import java.lang.reflect.Type;

/**
 * ConditionConverter para Room.
 * No convierte silenciosamente datos corruptos en objetos vacios validos.
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
            Condition result = gson.fromJson(value, conditionType);
            return result != null ? result : new Condition();
        } catch (JsonSyntaxException e) {
            throw new ConverterCorruptionException(
                    "Datos corruptos en columna de Condition: JSON invalido", e);
        } catch (Exception e) {
            throw new ConverterCorruptionException(
                    "Error al deserializar Condition", e);
        }
    }

    @TypeConverter
    public static String toString(Condition condition) {
        return condition != null ? gson.toJson(condition) : gson.toJson(new Condition());
    }
}
