package com.odin.core.domain.repository;

import com.odin.core.domain.model.SystemStatus;

/**
 * Contrato para persistencia y lectura del estado local del agente.
 */
public interface SystemStatusRepository {

    void saveStatus(SystemStatus status);

    SystemStatus loadStatus();
}
