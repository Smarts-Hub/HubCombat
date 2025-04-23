package dev.smartshub.hubCombat.item

import dev.smartshub.hubCombat.util.Msg
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

data class LoadOutItem(
    val slot: Int,
    val material: Material,
    val name: String?,
    val lore: List<String>?,
    val enchantments: Map<Enchantment, Int>,
    val flags: List<ItemFlag>
){

    fun toItemStack(): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta ?: return item

        name?.let {
            meta.displayName(Msg.parse(it))
        }

        lore?.let {
            meta.lore(Msg.parseList(it))
        }

        enchantments.forEach { (enchant, level) ->
            meta.addEnchant(enchant, level, true)
        }

        flags.forEach {
            meta.addItemFlags(it)
        }

        item.itemMeta = meta
        return item
    }
}

