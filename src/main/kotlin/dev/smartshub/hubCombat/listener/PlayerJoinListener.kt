package dev.smartshub.hubCombat.listener

import dev.smartshub.hubCombat.service.CooldownService
import dev.smartshub.hubCombat.service.PDCCheckService
import dev.smartshub.hubCombat.service.WeaponProvideService
import dev.smartshub.hubCombat.task.Timer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(
    private val weaponProvideService: WeaponProvideService,
    private val pdcCheckService: PDCCheckService,
    private val cooldownService: CooldownService
) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        weaponProvideService.giveWeapon(player)

        // If player join with the slot of the weapon, it will not work
        // So we need to check if the player has the item in the inventory
        player.inventory.itemInMainHand?.let { item ->
            if (!pdcCheckService.hasHubCombatTag(item)) {
                return
            }
            cooldownService.addPlayerToEnable(player.uniqueId)
        }

    }

}