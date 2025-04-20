package dev.smartshub.hubCombat.listener

import dev.smartshub.hubCombat.combat.AllowCombatHandler
import dev.smartshub.hubCombat.service.PDCCheckService
import dev.smartshub.hubCombat.storage.data.Data
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class PlayerHitListener(
    private val pdcCheckService: PDCCheckService,
    private val allowCombatHandler: AllowCombatHandler
) : Listener {

    @EventHandler
    suspend fun onPlayerHit(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        if(!allowCombatHandler.isInCombat(damager.uniqueId)){
            event.isCancelled = true
            return
        }

        val victim = event.entity
        if(!allowCombatHandler.isInCombat(victim.uniqueId)){
            event.isCancelled = true
            return
        }

        val item = (damager as Player).inventory.itemInMainHand
        if (!pdcCheckService.hasHubCombatTag(item)) return

        Data.increaseHits(damager.uniqueId)
    }

}