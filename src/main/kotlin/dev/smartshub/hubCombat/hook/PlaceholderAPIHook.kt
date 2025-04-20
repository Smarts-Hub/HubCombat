package dev.smartshub.hubCombat.hook

import dev.smartshub.hubCombat.storage.data.Data
import dev.smartshub.hubCombat.task.Timer
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class PlaceholderAPIHook(
    private val timer: Timer
) : PlaceholderExpansion() {

    override fun getIdentifier(): String {
        return "hubcombat"
    }

    override fun getAuthor(): String {
        return "hhitt"
    }

    override fun getVersion(): String {
        return "1.0"
    }

    override fun onPlaceholderRequest(player: Player, identifier: String): String? {
        return when(identifier) {
            "kills" -> {
                Data.getHitsFromCache(player.uniqueId).toString()
            }

            "deaths" -> {
                Data.getDeathsFromCache(player.uniqueId).toString()
            }

            "hits" -> {
                Data.getHitsFromCache(player.uniqueId).toString()
            }

            "time_left" -> {
                timer.getTime(player.uniqueId).toString()
            }

            else -> {
                "Invalid placeholder"
            }
        }
    }

}