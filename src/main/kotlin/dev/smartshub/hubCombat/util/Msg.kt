package dev.smartshub.hubCombat.util

import com.github.shynixn.mccoroutine.bukkit.launch
import dev.smartshub.hubCombat.HubCombat
import dev.smartshub.hubCombat.storage.file.FileManager
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player

object Msg {

    private val miniMsg = MiniMessage.miniMessage()
    private lateinit var plugin: HubCombat

    fun init(plugin: HubCombat) {
        this.plugin = plugin
    }

    fun parse(msg : String, player: Player) : TextComponent {
        return miniMsg.deserialize(placeholder(msg, player)) as TextComponent
    }

    fun parse(msg : String) : TextComponent {
        return miniMsg.deserialize(msg) as TextComponent
    }

    fun parseList(lore: List<String>, player: Player) : List<TextComponent> {
        val components = mutableListOf<TextComponent>()
        lore.forEach {
            components.add(parse(it, player))
        }
        return components
    }

    fun send(player: Player, path: String){
        plugin.launch {
            player.sendMessage(parse(getMsg(path), player))
        }
    }

    private fun getMsg(path: String) : String {
        return FileManager.get("lang")?.getString("messages.$path") ?: "<red>Message not found"
    }

    private fun placeholder(msg: String, player: Player) : String {
        return PlaceholderAPI.setPlaceholders(player, msg)
    }


}