package com.odin.core.customization.data.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.odin.core.customization.model.NotificationSettings;

/**
 * NotificationSettingsConverter para Room.
 * No convierte silenciosamente datos corruptos en null valido.
 */
public class NotificationSettingsConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static NotificationSettings fromString(String value) {
        if (value == null) {
            return null;
        }
        try {
            return gson.fromJson(value, NotificationSettings.class);
        } catch (JsonSyntaxException e) {
            throw new ConverterCorruptionException(
                    "Datos corruptos en columna de NotificationSettings: JSON invalido", e);
        } catch (Exception e) {
            throw new ConverterCorruptionException(
                    "Error al deserializar NotificationSettings", e);
        }
    }

    @TypeConverter
    public static String toString(NotificationSettings settings) {
        return settings != null ? gson.toJson(settings) : null;
    }
}
