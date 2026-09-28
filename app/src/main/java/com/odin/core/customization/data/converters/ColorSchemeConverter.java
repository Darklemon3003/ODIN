package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.odin.core.customization.model.ColorScheme;

/**
 * ColorSchemeConverter para Room.
 * No convierte silenciosamente datos corruptos en objetos vacios validos.
 */
public class ColorSchemeConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static ColorScheme fromString(String value) {
        if (value == null) {
            return new ColorScheme();
        }
        try {
            ColorScheme result = gson.fromJson(value, ColorScheme.class);
            return result != null ? result : new ColorScheme();
        } catch (JsonSyntaxException e) {
            throw new ConverterCorruptionException(
                    "Datos corruptos en columna de ColorScheme: JSON invalido", e);
        } catch (Exception e) {
            throw new ConverterCorruptionException(
                    "Error al deserializar ColorScheme", e);
        }
    }

    @TypeConverter
    public static String toString(ColorScheme colorScheme) {
        return colorScheme != null ? gson.toJson(colorScheme) : gson.toJson(new ColorScheme());
    }
}
