package dev.smartshub.hubCombat.command

import dev.smartshub.hubCombat.combat.AllowCombatHandler
import dev.smartshub.hubCombat.storage.file.FileManager
import dev.smartshub.hubCombat.util.Msg
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.CommandPermission

@Command("hubcombat")
@CommandPermission("hubcombat.admin")
class HubCombatCommand(
    private val allowCombatHandler: AllowCombatHandler
) {

    @Subcommand("reload")
    fun reload(actor: BukkitCommandActor) {
        FileManager.get("config")?.reloadFile()
        FileManager.get("lang")?.reloadFile()
        allowCombatHandler.clearCombatAllowed()
        Msg.send(actor.sender(), "config-reloaded")
    }

}