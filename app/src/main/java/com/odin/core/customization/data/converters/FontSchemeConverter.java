package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.odin.core.customization.model.FontScheme;

/**
 * FontSchemeConverter para Room.
 * No convierte silenciosamente datos corruptos en objetos vacios validos.
 */
public class FontSchemeConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static FontScheme fromString(String value) {
        if (value == null) {
            return new FontScheme();
        }
        try {
            FontScheme result = gson.fromJson(value, FontScheme.class);
            return result != null ? result : new FontScheme();
        } catch (JsonSyntaxException e) {
            throw new ConverterCorruptionException(
                    "Datos corruptos en columna de FontScheme: JSON invalido", e);
        } catch (Exception e) {
            throw new ConverterCorruptionException(
                    "Error al deserializar FontScheme", e);
        }
    }

    @TypeConverter
    public static String toString(FontScheme fontScheme) {
        return fontScheme != null ? gson.toJson(fontScheme) : gson.toJson(new FontScheme());
    }
}
