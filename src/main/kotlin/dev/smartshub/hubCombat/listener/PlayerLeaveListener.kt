package dev.smartshub.hubCombat.listener

import dev.smartshub.hubCombat.combat.AllowCombatHandler
import dev.smartshub.hubCombat.service.CooldownService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerLeaveListener(
    private val allowCombatHandler: AllowCombatHandler,
    private val cooldownService: CooldownService
) : Listener {

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        val player = event.player
        if (cooldownService.isPlayerInEnable(player.uniqueId)) {
            cooldownService.removePlayerFromEnable(player.uniqueId)
        }
        if (cooldownService.isPlayerInDisable(player.uniqueId)) {
            cooldownService.removePlayerFromDisable(player.uniqueId)
        }
        allowCombatHandler.disallowCombat(player.uniqueId)
    }

}