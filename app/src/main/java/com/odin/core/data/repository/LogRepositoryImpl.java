package com.odin.core.data.repository;

import com.odin.core.data.local.SecureLocalStorage;
import com.odin.core.domain.model.LogEvent;
import com.odin.core.domain.repository.LogRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementación concreta de LogRepository usando almacenamiento local.
 * Esta clase mantiene la persistencia interna de eventos de auditoría del agente.
 */
public class LogRepositoryImpl implements LogRepository {

    private static final String KEY_LOG_EVENTS = "key_log_events";
    private final SecureLocalStorage storage;

    public LogRepositoryImpl(SecureLocalStorage storage) {
        this.storage = storage;
    }

    @Override
    public void appendEvent(LogEvent event) {
        String payload = storage.loadString(KEY_LOG_EVENTS, "[]");
        JSONArray array = parseJsonArray(payload);

        JSONObject eventJson = new JSONObject();
        try {
            eventJson.put("timestamp", event.getTimestamp());
            eventJson.put("source", event.getSource());
            eventJson.put("message", event.getMessage());
            array.put(eventJson);
        } catch (JSONException e) {
            // La persistencia de logs es crítica, pero se puede reconstruir en memoria.
        }

        storage.saveString(KEY_LOG_EVENTS, array.toString());
    }

    @Override
    public List<LogEvent> fetchRecentEvents(int maximumEntries) {
        String payload = storage.loadString(KEY_LOG_EVENTS, "[]");
        JSONArray array = parseJsonArray(payload);
        int size = array.length();
        if (size == 0) {
            return Collections.emptyList();
        }

        List<LogEvent> events = new ArrayList<>();
        int start = Math.max(0, size - maximumEntries);
        for (int i = start; i < size; i++) {
            JSONObject object = array.optJSONObject(i);
            if (object == null) {
                continue;
            }
            long timestamp = object.optLong("timestamp", System.currentTimeMillis());
            String source = object.optString("source", "unknown");
            String message = object.optString("message", "<sin datos>");
            events.add(new LogEvent(timestamp, source, message));
        }

        return events;
    }

    private JSONArray parseJsonArray(String raw) {
        try {
            return new JSONArray(raw);
        } catch (JSONException e) {
            return new JSONArray();
        }
    }
}
