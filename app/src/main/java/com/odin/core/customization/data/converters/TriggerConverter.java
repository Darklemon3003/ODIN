package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.odin.core.customization.model.Trigger;

/**
 * TriggerConverter para Room.
 * No convierte silenciosamente datos corruptos en objetos vacios validos.
 */
public class TriggerConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static Trigger fromString(String value) {
        if (value == null) {
            return new Trigger();
        }
        try {
            Trigger result = gson.fromJson(value, Trigger.class);
            return result != null ? result : new Trigger();
        } catch (JsonSyntaxException e) {
            throw new ConverterCorruptionException(
                    "Datos corruptos en columna de Trigger: JSON invalido", e);
        } catch (Exception e) {
            throw new ConverterCorruptionException(
                    "Error al deserializar Trigger", e);
        }
    }

    @TypeConverter
    public static String toString(Trigger trigger) {
        return trigger != null ? gson.toJson(trigger) : gson.toJson(new Trigger());
    }
}
