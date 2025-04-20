package dev.smartshub.hubCombat.combat

import dev.smartshub.hubCombat.HubCombat
import dev.smartshub.hubCombat.util.Msg
import java.util.UUID

class AllowCombatHandler(
    private val plugin: HubCombat,
) {

    private val combatAllowed = mutableListOf<UUID>()

    fun isInCombat(playerId: UUID): Boolean {
        return combatAllowed.contains(playerId)
    }

    fun allowCombat(playerId: UUID) {
        if (combatAllowed.contains(playerId)) return
        combatAllowed.add(playerId)
        val player = plugin.server.getPlayer(playerId) ?: return
        Msg.send(player, "messages.weapon-given")
    }

    fun disallowCombat(playerId: UUID) {
        if (!combatAllowed.contains(playerId)) return
        val player = plugin.server.getPlayer(playerId) ?: return
        Msg.send(player, "messages.weapon-given")
    }

    fun clearCombatAllowed() {
        combatAllowed.clear()
    }

}