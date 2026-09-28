package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.odin.core.customization.model.IconScheme;

/**
 * IconSchemeConverter para Room.
 * No convierte silenciosamente datos corruptos en objetos vacios validos.
 */
public class IconSchemeConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static IconScheme fromString(String value) {
        if (value == null) {
            return new IconScheme();
        }
        try {
            IconScheme result = gson.fromJson(value, IconScheme.class);
            return result != null ? result : new IconScheme();
        } catch (JsonSyntaxException e) {
            throw new ConverterCorruptionException(
                    "Datos corruptos en columna de IconScheme: JSON invalido", e);
        } catch (Exception e) {
            throw new ConverterCorruptionException(
                    "Error al deserializar IconScheme", e);
        }
    }

    @TypeConverter
    public static String toString(IconScheme iconScheme) {
        return iconScheme != null ? gson.toJson(iconScheme) : gson.toJson(new IconScheme());
    }
}
