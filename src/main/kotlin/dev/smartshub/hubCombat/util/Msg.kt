package dev.smartshub.hubCombat.util

import com.github.shynixn.mccoroutine.bukkit.launch
import dev.smartshub.hubCombat.HubCombat
import dev.smartshub.hubCombat.storage.file.FileManager
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.function.UnaryOperator

object Msg {

    private val miniMsg = MiniMessage.builder()
    .postProcessor(UnaryOperator { component: Component? ->
        component!!.decorationIfAbsent(
            TextDecoration.ITALIC,
            TextDecoration.State.FALSE
        )
    })
    .build()
    private lateinit var plugin: HubCombat


    fun init(plugin: HubCombat) {
        this.plugin = plugin
    }

    fun parse(msg : String, player: Player) : TextComponent {
        return parse(placeholder(msg, player))
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

    fun parseList(lore: List<String>) : List<TextComponent> {
        val components = mutableListOf<TextComponent>()
        lore.forEach {
            components.add(parse(it))
        }
        return components
    }

    fun send(player: Player, path: String){
        plugin.launch {
            player.sendMessage(parse(getMsg(path), player))
        }
    }

    fun send(sender: CommandSender, path: String){
        sender.sendMessage(parse(getMsg(path)))
    }

    private fun getMsg(path: String) : String {
        return FileManager.get("lang")?.getString("messages.$path") ?: "<red>Message not found"
    }

    private fun placeholder(msg: String, player: Player) : String {
        return PlaceholderAPI.setPlaceholders(player, msg)
    }


}