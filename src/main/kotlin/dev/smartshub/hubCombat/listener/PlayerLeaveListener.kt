package dev.smartshub.hubCombat.listener

import dev.smartshub.hubCombat.combat.AllowCombatHandler
import dev.smartshub.hubCombat.task.Timer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerLeaveListener(
    private val timer: Timer,
    private val allowCombatHandler: AllowCombatHandler
) : Listener {

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        val player = event.player
        timer.attemptToDisable(player.uniqueId)
        allowCombatHandler.disallowCombat(player.uniqueId)
    }

}