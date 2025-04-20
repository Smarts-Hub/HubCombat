package dev.smartshub.hubCombat.listener

import dev.smartshub.hubCombat.service.WeaponProvideService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(
    private val weaponProvideService: WeaponProvideService
) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        weaponProvideService.giveWeapon(player)
    }

}