package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.odin.core.customization.model.Action;
import java.lang.reflect.Type;

/**
 * ActionConverter para Room.
 * No convierte silenciosamente datos corruptos en objetos vacios validos.
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
            Action result = gson.fromJson(value, actionType);
            return result != null ? result : new Action();
        } catch (JsonSyntaxException e) {
            throw new ConverterCorruptionException(
                    "Datos corruptos en columna de Action: JSON invalido", e);
        } catch (Exception e) {
            throw new ConverterCorruptionException(
                    "Error al deserializar Action", e);
        }
    }

    @TypeConverter
    public static String toString(Action action) {
        return action != null ? gson.toJson(action) : gson.toJson(new Action());
    }
}
