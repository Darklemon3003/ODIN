package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.odin.core.customization.model.Theme;

/**
 * ThemeConverter para Room.
 * No convierte silenciosamente datos corruptos en null valido.
 */
public class ThemeConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static Theme fromString(String value) {
        if (value == null) {
            return null;
        }
        try {
            return gson.fromJson(value, Theme.class);
        } catch (JsonSyntaxException e) {
            throw new ConverterCorruptionException(
                    "Datos corruptos en columna de Theme: JSON invalido", e);
        } catch (Exception e) {
            throw new ConverterCorruptionException(
                    "Error al deserializar Theme", e);
        }
    }

    @TypeConverter
    public static String toString(Theme theme) {
        return theme != null ? gson.toJson(theme) : null;
    }
}
