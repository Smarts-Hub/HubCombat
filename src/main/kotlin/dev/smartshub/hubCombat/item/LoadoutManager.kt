package dev.smartshub.hubCombat.item

import dev.smartshub.hubCombat.storage.file.Configuration
import dev.smartshub.hubCombat.storage.file.FileManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

class LoadoutManager() {

    private var config: Configuration = FileManager.get("config")!!
    private val loadouts: Map<String, LoadOut> = loadLoadouts()


    // Give the load out to a player
    fun giveTo(player: Player) {
        val loadout = loadouts.values.firstOrNull { player.hasPermission(it.permission) }
            ?: loadouts["default"] ?: return

        val inventory = player.inventory

        loadout.items.forEach { item ->
            val itemStack = item.toItemStack()

            when (item.slot) {
                36 -> inventory.boots = itemStack
                37 -> inventory.leggings = itemStack
                38 -> inventory.chestplate = itemStack
                39 -> inventory.helmet = itemStack
                in 0..35 -> inventory.setItem(item.slot, itemStack)
                else -> Bukkit.getLogger().warning("Invalid slot ${item.slot} in loadout for ${player.name}")
            }
        }
    }

    // Charge the load outs from the config
    private fun loadLoadouts(): Map<String, LoadOut> {
        val result = mutableMapOf<String, LoadOut>()
        val section = config.getConfigurationSection("loadouts") ?: return result

        for (key in section.getKeys(false)) {
            val loadoutSection = section.getConfigurationSection(key) ?: continue
            val permission = loadoutSection.getString("permission") ?: continue
            val itemsList = mutableListOf<LoadOutItem>()

            val items = loadoutSection.getMapList("items")
            for (item in items) {
                val slot = item["slot"] as? Int ?: continue
                val material = Material.getMaterial((item["material"] as? String)?.uppercase() ?: "") ?: continue
                val name = item["name"] as? String
                val lore = item["lore"] as? List<String>

                val enchantments = mutableMapOf<Enchantment, Int>()
                val enchantMap = item["enchantments"] as? Map<*, *>
                enchantMap?.forEach { (k, v) ->
                    val ench = Enchantment.getByName(k.toString().uppercase()) ?: return@forEach
                    enchantments[ench] = (v as? Int) ?: 1
                }

                val flags = (item["flags"] as? List<*>)?.mapNotNull {
                    runCatching { ItemFlag.valueOf(it.toString().uppercase()) }.getOrNull()
                } ?: emptyList()

                itemsList.add(LoadOutItem(slot, material, name, lore, enchantments, flags))
            }

            result[key] = LoadOut(permission, itemsList)
        }

        return result
    }

}