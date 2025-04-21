package dev.smartshub.hubCombat.listener

import dev.smartshub.hubCombat.service.PDCCheckService
import dev.smartshub.hubCombat.util.Msg
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class DropItemListener(
    private val pdcCheckService: PDCCheckService
) : Listener {

    @EventHandler
    fun onDropItem(event: PlayerDropItemEvent) {
        val player = event.player
        val item = event.itemDrop.itemStack

        if(!pdcCheckService.hasHubCombatTag(item)) {
            return
        }

        event.isCancelled = true
        Msg.send(player, "cant-drop-item")
    }

}