package com.odin.core.data.repository;

import com.odin.core.data.local.SecureLocalStorage;
import com.odin.core.domain.model.SystemStatus;
import com.odin.core.domain.repository.SystemStatusRepository;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Implementación local de SystemStatusRepository. Mantiene un snapshot ligero
 * del estado interno del servicio para soportar auditoría y reanudación.
 */
public class SystemStatusRepositoryImpl implements SystemStatusRepository {

    private static final String KEY_STATUS = "key_system_status";
    private final SecureLocalStorage storage;

    public SystemStatusRepositoryImpl(SecureLocalStorage storage) {
        this.storage = storage;
    }

    @Override
    public void saveStatus(SystemStatus status) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("serviceActive", status.isServiceActive());
            payload.put("lastHeartbeat", status.getLastHeartbeat());
            payload.put("currentState", status.getCurrentState());
        } catch (JSONException e) {
            // Si ocurre un error, solo persistimos lo mínimo.
        }
        storage.saveString(KEY_STATUS, payload.toString());
    }

    @Override
    public SystemStatus loadStatus() {
        String raw = storage.loadString(KEY_STATUS, "{}");
        try {
            JSONObject payload = new JSONObject(raw);
            boolean active = payload.optBoolean("serviceActive", false);
            long heartbeat = payload.optLong("lastHeartbeat", 0);
            String currentState = payload.optString("currentState", "desconocido");
            return new SystemStatus(active, heartbeat, currentState);
        } catch (JSONException e) {
            return new SystemStatus(false, 0, "desconocido");
        }
    }
}
