package dev.smartshub.hubCombat.storage.data.cache

import dev.smartshub.hubCombat.storage.data.PlayerStats
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class Cache {
    private val playerStatsCache = ConcurrentHashMap<UUID, PlayerStats>()

    fun getPlayerStats(playerId: UUID): PlayerStats? =
        playerStatsCache[playerId]

    fun updatePlayerStats(playerId: UUID, stats: PlayerStats) {
        playerStatsCache[playerId] = stats
    }

    fun loadPlayerStats(playerId: UUID, stats: PlayerStats) {
        playerStatsCache[playerId] = stats
    }

    fun removePlayerStats(playerId: UUID) {
        playerStatsCache.remove(playerId)
    }
}
