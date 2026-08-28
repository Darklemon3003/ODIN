package com.odin.core.service.network;

import com.odin.core.domain.model.NetworkPacket;
import com.odin.core.domain.repository.FirewallRepository;

/**
 * LocalFirewallEngine decide de forma eficiente si un paquete debe ser bloqueado.
 */
public final class LocalFirewallEngine {

    private final FirewallRepository firewallRepository;

    public LocalFirewallEngine(FirewallRepository firewallRepository) {
        this.firewallRepository = firewallRepository;
    }

    public boolean shouldBlockPacket(NetworkPacket packet) {
        if (packet == null) {
            return true;
        }
        return firewallRepository.isBlocked(packet);
    }
}
