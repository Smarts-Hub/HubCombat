package dev.smartshub.hubCombat.task

import dev.smartshub.hubCombat.HubCombat
import dev.smartshub.hubCombat.combat.AllowCombatHandler
import dev.smartshub.hubCombat.service.PDCCheckService
import dev.smartshub.hubCombat.storage.file.FileManager
import dev.smartshub.hubCombat.util.Msg
import org.bukkit.scheduler.BukkitRunnable
import java.util.UUID

class Timer(
    private val plugin: HubCombat,
    private val allowCombatHandler: AllowCombatHandler,
    private val pdcCheckService: PDCCheckService
) : BukkitRunnable() {

    private val toEnable: MutableMap<UUID, Int> = mutableMapOf()
    private val toDisable: MutableMap<UUID, Int> = mutableMapOf()
    private val enableTime = FileManager.get("config")?.getInt("timing.give-weapon") ?: 5
    private val disableTime = FileManager.get("config")?.getInt("timing.remove-weapon") ?: 5

    fun addPlayerToEnable(player: UUID) {
        toEnable[player] = enableTime
    }

    fun addPlayerToDisable(player: UUID) {
        toDisable[player] = disableTime
    }

    fun attemptToDisable(player: UUID) {
        if (!allowCombatHandler.isInCombat(player)) return
        addPlayerToDisable(player)
    }

    fun getTime(player: UUID): Int {
        when(player) {
            in toEnable -> return toEnable[player] ?: 0
            in toDisable -> return toDisable[player] ?: 0
        }
        return 0
    }

    override fun run() {
        // Enable pvp
        toEnable.keys.toList().forEach { uuid ->
            val player = plugin.server.getPlayer(uuid) ?: return@forEach
            val holding = pdcCheckService.hasHubCombatTag(player.inventory.itemInMainHand)

            if (!holding) {
                toEnable.remove(uuid)
                addPlayerToDisable(uuid)
                return@forEach
            }

            toEnable[uuid] = toEnable[uuid]!! - 1
            Msg.send(player, "enabling-combat-on")
            if (toEnable[uuid]!! <= 0) {
                allowCombatHandler.allowCombat(uuid)
                toEnable.remove(uuid)
            }
        }

        // Disable PvP
        toDisable.keys.toList().forEach { uuid ->

            val player = plugin.server.getPlayer(uuid) ?: return@forEach
            val holding = pdcCheckService.hasHubCombatTag(player.inventory.itemInMainHand)

            if (holding) {
                toDisable.remove(uuid)
                return@forEach
            }

            toDisable[uuid] = toDisable[uuid]!! - 1
            Msg.send(player, "disabling-combat-on")
            if (toDisable[uuid]!! <= 0) {
                allowCombatHandler.disallowCombat(uuid)
                toDisable.remove(uuid)
            }
        }
    }



}