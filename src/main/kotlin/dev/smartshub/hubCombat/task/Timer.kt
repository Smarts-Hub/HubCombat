package dev.smartshub.hubCombat.task

import dev.smartshub.hubCombat.HubCombat
import dev.smartshub.hubCombat.combat.AllowCombatHandler
import dev.smartshub.hubCombat.service.CooldownService
import dev.smartshub.hubCombat.service.PDCCheckService
import dev.smartshub.hubCombat.storage.file.FileManager
import dev.smartshub.hubCombat.util.Msg
import org.bukkit.scheduler.BukkitRunnable
import java.util.UUID

class Timer(
    private val plugin: HubCombat,
    private val allowCombatHandler: AllowCombatHandler,
    private val pdcCheckService: PDCCheckService,
    private val cooldownService: CooldownService
) : BukkitRunnable() {


    override fun run() {
        // Enable pvp
        cooldownService.getToEnable().keys.toList().forEach { uuid ->
            val player = plugin.server.getPlayer(uuid) ?: return@forEach
            val holding = pdcCheckService.hasHubCombatTag(player.inventory.itemInMainHand)

            if (!holding) {
                cooldownService.removePlayerFromEnable(uuid)
                cooldownService.addPlayerToDisable(uuid)
                return@forEach
            }

            cooldownService.decrementEnable(uuid)
            Msg.send(player, "enabling-combat-on")
            if (cooldownService.getTimeToEnable(uuid) <= 0) {
                allowCombatHandler.allowCombat(uuid)
                cooldownService.removePlayerFromEnable(uuid)
            }
        }

        // Disable PvP
        cooldownService.getToDisable().keys.toList().forEach { uuid ->

            val player = plugin.server.getPlayer(uuid) ?: return@forEach
            val holding = pdcCheckService.hasHubCombatTag(player.inventory.itemInMainHand)

            if (holding) {
                cooldownService.removePlayerFromDisable(uuid)
                return@forEach
            }

            cooldownService.decrementDisable(uuid)
            Msg.send(player, "disabling-combat-on")
            if (cooldownService.getTimeToDisable(uuid) <= 0) {
                allowCombatHandler.disallowCombat(uuid)
                cooldownService.removePlayerFromDisable(uuid)
            }
        }
    }



}