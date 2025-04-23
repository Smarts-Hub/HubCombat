package dev.smartshub.hubCombat.listener

import dev.smartshub.hubCombat.item.LoadoutManager
import dev.smartshub.hubCombat.service.CooldownService
import dev.smartshub.hubCombat.service.PDCCheckService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

class PlayerRespawnListener(
    private val loadoutManager: LoadoutManager,
    private val pdcCheckService: PDCCheckService,
    private val cooldownService: CooldownService
) : Listener {

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val player = event.player

        loadoutManager.giveTo(player)

        // If player respawn with the slot of the weapon, it will not work
        // So we need to check if the player has the item in the inventory
        player.inventory.itemInMainHand.let { item ->
            if (!pdcCheckService.hasHubCombatTag(item)) {
                return
            }
            cooldownService.addPlayerToEnable(player.uniqueId)
        }

    }

}