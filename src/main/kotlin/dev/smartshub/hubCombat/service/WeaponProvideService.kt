package dev.smartshub.hubCombat.service

import dev.smartshub.hubCombat.HubCombat
import dev.smartshub.hubCombat.storage.file.FileManager
import dev.smartshub.hubCombat.util.Msg
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class WeaponProvideService(private val plugin: HubCombat) {

    fun giveWeapon(player: Player){
        // Check if the player is online
        val item = buildWeapon(player)
        val slot = FileManager.get("config")?.getInt("items.weapon.slot")
        player.inventory.setItem(slot!!, item)

    }

    private fun buildWeapon(player: Player) : ItemStack {
        // Getting values from config
        val name = FileManager.get("config")?.getString("items.weapon.name")
        val materialStr = FileManager.get("material")?.getString("items.weapon.material")
        val lore = FileManager.get("config")?.getStringList("items.weapon.lore")

        // Build raw item
        val material = Material.valueOf(materialStr!!.uppercase())
        val item = ItemStack(material)
        val meta = item.itemMeta

        // Apply name and lore
        meta.displayName(Msg.parse(name!!, player))
        meta.lore(Msg.parseList(lore!!, player))

        // Adding PersistentDataContainer to identify the item
        val key = NamespacedKey(plugin, "hubCombatWeapon")
        meta.persistentDataContainer.set(key, PersistentDataType.STRING, "ultra_sword")
        item.itemMeta = meta
        return item
    }



}