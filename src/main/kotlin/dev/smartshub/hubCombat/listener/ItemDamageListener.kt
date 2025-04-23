package dev.smartshub.hubCombat.listener

import dev.smartshub.hubCombat.service.PDCCheckService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemDamageEvent

class ItemDamageListener(
    private val pdcCheckService: PDCCheckService
) : Listener {

    @EventHandler
    fun onItemDamage(event: PlayerItemDamageEvent) {
        // Prevent losing durability on items with the hub combat tag
        if(!pdcCheckService.hasHubCombatTag(event.item)) {
            return
        }
        event.isCancelled = true

    }

}