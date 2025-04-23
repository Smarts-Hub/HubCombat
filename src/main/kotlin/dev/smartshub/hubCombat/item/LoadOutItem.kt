package dev.smartshub.hubCombat.item

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag

data class LoadOutItem(
    val slot: Int,
    val material: Material,
    val name: String?,
    val lore: List<String>?,
    val enchantments: Map<Enchantment, Int>,
    val flags: List<ItemFlag>
)
