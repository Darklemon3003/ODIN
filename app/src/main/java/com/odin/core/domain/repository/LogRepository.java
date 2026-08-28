package com.odin.core.domain.repository;

import com.odin.core.domain.model.LogEvent;

import java.util.List;

/**
 * Contrato que define el comportamiento de almacenamiento y consulta de eventos de auditoría.
 */
public interface LogRepository {

    void appendEvent(LogEvent event);

    List<LogEvent> fetchRecentEvents(int maximumEntries);
}
