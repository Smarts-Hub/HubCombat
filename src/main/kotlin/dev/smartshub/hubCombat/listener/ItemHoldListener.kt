package dev.smartshub.hubCombat.listener

import dev.smartshub.hubCombat.combat.AllowCombatHandler
import dev.smartshub.hubCombat.service.PDCCheckService
import dev.smartshub.hubCombat.task.Timer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent

class ItemHoldListener(
    private val pdcCheckService: PDCCheckService,
    private val allowCombatHandler: AllowCombatHandler,
    private val timer: Timer
) : Listener {

    @EventHandler
    fun onItemHold(event: PlayerItemHeldEvent) {
        val player = event.player
        val slot = event.newSlot
        val item = player.inventory.getItem(slot) ?: return

        // This will handle by task, then return
        if(allowCombatHandler.isInCombat(player.uniqueId)) return

        // If is not a weapon from the plugin, then return
        if(!pdcCheckService.hasHubCombatTag(item)) return

        timer.addPlayer(player.uniqueId)

    }


}