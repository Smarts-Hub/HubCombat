package dev.smartshub.hubCombat.service

import dev.smartshub.hubCombat.HubCombat
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class PDCCheckService(private val plugin: HubCombat) {

    // As obvious, this method checks if the item has the PersistentDataContainer tag
    fun hasHubCombatTag(item: ItemStack): Boolean {
        if (!item.hasItemMeta()) return false
        val meta = item.itemMeta ?: return false

        val key = NamespacedKey(plugin, "hubCombatWeapon")
        return meta.persistentDataContainer.has(key, PersistentDataType.STRING)
    }

}