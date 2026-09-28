package com.odin.core.customization.data.converters;

/**
 * Excepcion lanzada cuando un TypeConverter de Room detecta datos corruptos
 * en la base de datos que no pueden ser deserializados correctamente.
 *
 * Esta excepcion preserva la distincion entre:
 * - Valor null/no almacenado (retorna lista vacia o valor por defecto)
 * - Datos corruptos (lanza esta excepcion)
 *
 * Room propagara esta excepcion como SQLiteException, lo que permite que
 * el repositorio la maneje como un error de persistencia en lugar de
 * tratar silenciosamente los datos corruptos como validos.
 */
public class ConverterCorruptionException extends RuntimeException {

    public ConverterCorruptionException(String message) {
        super(message);
    }

    public ConverterCorruptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
