package dev.smartshub.hubCombat.listener

import dev.smartshub.hubCombat.combat.AllowCombatHandler
import dev.smartshub.hubCombat.service.CooldownService
import dev.smartshub.hubCombat.service.PDCCheckService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent

class ItemHoldListener(
    private val pdcCheckService: PDCCheckService,
    private val allowCombatHandler: AllowCombatHandler,
    private val cooldownService: CooldownService
) : Listener {

    @EventHandler
    fun onItemHold(event: PlayerItemHeldEvent) {
        val player = event.player
        val slot = event.newSlot
        val item = player.inventory.getItem(slot)

        if(pdcCheckService.hasHubCombatTag(item) && !allowCombatHandler.isInCombat(player.uniqueId)) {
            cooldownService.addPlayerToEnable(player.uniqueId)
            return
        }

        if(!pdcCheckService.hasHubCombatTag(item) && allowCombatHandler.isInCombat(player.uniqueId)) {
            cooldownService.addPlayerToDisable(player.uniqueId)
            return
        }

    }


}