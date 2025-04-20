package dev.smartshub.hubCombat.storage.data

import dev.smartshub.hubCombat.HubCombat
import dev.smartshub.hubCombat.storage.data.cache.Cache
import dev.smartshub.hubCombat.storage.data.database.PlayerStatsDAO
import java.util.*

object Data {
    private lateinit var cache: Cache
    private lateinit var playerStatsDAO: PlayerStatsDAO

    fun init(plugin: HubCombat) {
        cache = Cache()
        playerStatsDAO = PlayerStatsDAO(plugin, cache)
    }

    suspend fun increaseKills(playerId: UUID) {
        val stats = getPlayerStats(playerId)
        updatePlayerStats(playerId, stats.copy(kills = stats.kills + 1))
    }

    suspend fun increaseDeaths(playerId: UUID) {
        val stats = getPlayerStats(playerId)
        updatePlayerStats(playerId, stats.copy(deaths = stats.deaths + 1))
    }

    suspend fun increaseHits(playerId: UUID) {
        val stats = getPlayerStats(playerId)
        updatePlayerStats(playerId, stats.copy(hits = stats.hits + 1))
    }

    fun getKillsFromCache(playerId: UUID): Int =
        cache.getPlayerStats(playerId)?.kills ?: 0

    fun getDeathsFromCache(playerId: UUID): Int =
        cache.getPlayerStats(playerId)?.deaths ?: 0

    fun getHitsFromCache(playerId: UUID): Int =
        cache.getPlayerStats(playerId)?.hits ?: 0

    suspend fun getKills(playerId: UUID): Int =
        getPlayerStats(playerId).kills

    suspend fun getDeaths(playerId: UUID): Int =
        getPlayerStats(playerId).deaths

    suspend fun getHits(playerId: UUID): Int =
        getPlayerStats(playerId).hits

    suspend fun loadPlayerAtCache(playerId: UUID) {
        cache.loadPlayerStats(playerId, getPlayerStats(playerId))
    }

    fun unloadPlayerFromCache(playerId: UUID) {
        cache.removePlayerStats(playerId)
    }

    suspend fun createPlayerStats(playerId: UUID) {
        playerStatsDAO.createPlayerStats(playerId)
    }

    suspend fun deletePlayerStats(playerId: UUID) {
        playerStatsDAO.deletePlayer(playerId)
    }

    private suspend fun getPlayerStats(playerId: UUID): PlayerStats =
        playerStatsDAO.getPlayerStats(playerId)

    private suspend fun updatePlayerStats(playerId: UUID, newStats: PlayerStats) {
        playerStatsDAO.updatePlayerStats(playerId, newStats)
    }
}
