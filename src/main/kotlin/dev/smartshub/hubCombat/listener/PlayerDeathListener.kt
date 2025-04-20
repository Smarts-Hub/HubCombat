package dev.smartshub.hubCombat.listener

import dev.smartshub.hubCombat.combat.AllowCombatHandler
import dev.smartshub.hubCombat.service.PDCCheckService
import dev.smartshub.hubCombat.storage.data.Data
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeathListener(
    private val pdcCheckService: PDCCheckService,
    private val allowCombatHandler: AllowCombatHandler
) : Listener{

    @EventHandler (priority = EventPriority.HIGH)
    suspend fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.entity
        if(!allowCombatHandler.isInCombat(player.uniqueId)) return
        allowCombatHandler.disallowCombat(player.uniqueId)

        val killer = event.entity.killer
        killer?.let {
            val item = it.inventory.itemInMainHand
            if (!pdcCheckService.hasHubCombatTag(item)) return
            Data.increaseKills(it.uniqueId)
            Data.increaseKills(player.uniqueId)
        }

    }


}