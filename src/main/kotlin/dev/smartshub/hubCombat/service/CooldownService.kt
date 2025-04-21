package dev.smartshub.hubCombat.service

import dev.smartshub.hubCombat.storage.file.FileManager
import java.util.*

class CooldownService {

    private val toEnable: MutableMap<UUID, Int> = mutableMapOf()
    private val toDisable: MutableMap<UUID, Int> = mutableMapOf()
    private val enableTime = FileManager.get("config")?.getInt("timing.give-weapon") ?: 5
    private val disableTime = FileManager.get("config")?.getInt("timing.remove-weapon") ?: 5

    fun addPlayerToEnable(uuid: UUID) {
        toEnable[uuid] = enableTime
    }

    fun addPlayerToDisable(uuid: UUID) {
        toDisable[uuid] = disableTime
    }

    fun removePlayerFromEnable(uuid: UUID) {
        toEnable.remove(uuid)
    }

    fun removePlayerFromDisable(uuid: UUID) {
        toDisable.remove(uuid)
    }

    fun isPlayerInEnable(uuid: UUID): Boolean {
        return toEnable.containsKey(uuid)
    }

    fun isPlayerInDisable(uuid: UUID): Boolean {
        return toDisable.containsKey(uuid)
    }

    fun getTime(uuid: UUID): Int {
        return toEnable[uuid] ?: toDisable[uuid] ?: 0
    }

    fun getToEnable(): Map<UUID, Int> {
        return toEnable
    }

    fun getToDisable(): Map<UUID, Int> {
        return toDisable
    }

    fun decrementEnable(uuid: UUID) {
        toEnable[uuid]?.let {
            toEnable[uuid] = it - 1
        }
    }

    fun getTimeToEnable(uuid: UUID): Int {
        return toEnable[uuid] ?: 0
    }

    fun getTimeToDisable(uuid: UUID): Int {
        return toDisable[uuid] ?: 0
    }

    fun decrementDisable(uuid: UUID) {
        toDisable[uuid]?.let {
            toDisable[uuid] = it - 1
        }
    }

}