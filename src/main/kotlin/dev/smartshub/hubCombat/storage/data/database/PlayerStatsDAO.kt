package dev.smartshub.hubCombat.storage.data.database

import dev.smartshub.hubCombat.HubCombat
import dev.smartshub.hubCombat.storage.data.PlayerStats
import dev.smartshub.hubCombat.storage.data.cache.Cache
import dev.smartshub.hubCombat.storage.file.FileManager
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class PlayerStatsDAO(
    private val plugin: HubCombat,
    private val cache: Cache
){

    init {
        val config = FileManager.get("config") ?: throw IllegalStateException("Config file not found!")
        val dbType = config.getString("database.driver")?.lowercase() ?: throw IllegalArgumentException("Database driver not specified in config")

        when (dbType) {
            "mysql" -> {
                val host = config.getString("database.host") ?: "localhost"
                val port = config.getInt("database.port") ?: 3306
                val databaseName = config.getString("database.db-name") ?: "database"
                val user = config.getString("database.user") ?: "root"
                val password = config.getString("database.password") ?: ""

                val url = "jdbc:mysql://$host:$port/$databaseName"
                Database.connect(
                    url = url,
                    driver = "com.mysql.cj.jdbc.Driver",
                    user = user,
                    password = password
                )
            }
            "h2" -> {
                plugin.logger.info("Loading H2 database...")
                val databaseName = config.getString("database.db-name") ?: "database"
                val url = "jdbc:h2:file:./data/$databaseName;DB_CLOSE_DELAY=-1;"
                Database.connect(
                    url = url,
                    driver = "org.h2.Driver",
                    user = "root",
                    password = ""
                )
            }
            else -> throw IllegalArgumentException("Database '$dbType' type is not supported! Available: 'H2', 'MySQL'.")
        }

        transaction {
            plugin.logger.info("Creating table if not exists...")
            SchemaUtils.create(Players)
        }
    }

    suspend fun getPlayerStats(playerId: UUID): PlayerStats {
        cache.getPlayerStats(playerId)?.let { return it }

        var stats = newSuspendedTransaction(Dispatchers.IO) {
            Players.selectAll().where { Players.id eq playerId }.singleOrNull()?.let {
                PlayerStats(
                    kills = it[Players.kills],
                    deaths = it[Players.deaths],
                    hits = it[Players.hits]
                )
            }
        }

        stats?.let {
            cache.updatePlayerStats(playerId, it)
        } ?: run {
            stats = PlayerStats(0, 0, 0)
        }

        return stats!!
    }

    suspend fun updatePlayerStats(playerId: UUID, newStats: PlayerStats) {
        newSuspendedTransaction(Dispatchers.IO) {
            Players.update({ Players.id eq playerId }) {
                it[kills] = newStats.kills
                it[deaths] = newStats.deaths
                it[hits] = newStats.hits
            }
        }
        cache.updatePlayerStats(playerId, newStats)
    }

    suspend fun createPlayerStats(playerId: UUID) {
        if (hasStats(playerId)) return
        newSuspendedTransaction(Dispatchers.IO) {
            Players.insert {
                it[id] = playerId
                it[kills] = 0
                it[deaths] = 0
                it[hits] = 0
            }
        }
        cache.updatePlayerStats(playerId, PlayerStats(0, 0, 0))
    }

    suspend fun deletePlayer(playerId: UUID) {
        newSuspendedTransaction(Dispatchers.IO) {
            Players.deleteWhere { id eq playerId }
        }
        cache.removePlayerStats(playerId)
    }

    private suspend fun hasStats(playerId: UUID): Boolean {
        cache.getPlayerStats(playerId)?.let { return true }

        val exists = newSuspendedTransaction(Dispatchers.IO) {
            Players.selectAll().where { Players.id eq playerId }.empty().not()
        }

        return exists
    }
}
