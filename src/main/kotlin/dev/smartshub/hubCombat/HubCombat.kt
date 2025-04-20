package dev.smartshub.hubCombat

import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import dev.smartshub.hubCombat.combat.AllowCombatHandler
import dev.smartshub.hubCombat.command.HubCombatCommand
import dev.smartshub.hubCombat.hook.PlaceholderAPIHook
import dev.smartshub.hubCombat.listener.*
import dev.smartshub.hubCombat.service.PDCCheckService
import dev.smartshub.hubCombat.service.WeaponProvideService
import dev.smartshub.hubCombat.storage.data.Data
import dev.smartshub.hubCombat.storage.file.FileManager
import dev.smartshub.hubCombat.task.Timer
import dev.smartshub.hubCombat.util.Msg
import revxrsal.commands.Lamp
import revxrsal.commands.bukkit.BukkitLamp
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import revxrsal.zapper.ZapperJavaPlugin

class HubCombat : ZapperJavaPlugin() {

    private lateinit var timer: Timer
    private lateinit var weaponProvideService: WeaponProvideService
    private lateinit var allowCombatHandler: AllowCombatHandler
    private lateinit var pdcCheckService: PDCCheckService

    override fun onEnable() {

        // Init managers, services and objects
        initObjects()

        // Start the timer task async
        timer.runTaskTimerAsynchronously(this, 0L, 20L)

        // Register commands and events
        registerCommands()
        registerEvents()

        // Hook other plugins

    }

    override fun onDisable() {
        if(!timer.isCancelled){
            timer.cancel()
        }
    }

    private fun initObjects(){
        // Firstly "static" access
        FileManager.init(this)
        Data.init(this)
        Msg.init(this)

        // Then init the objects
        weaponProvideService = WeaponProvideService(this)
        allowCombatHandler = AllowCombatHandler(this)
        pdcCheckService = PDCCheckService(this)
        timer = Timer(this, allowCombatHandler, pdcCheckService)
    }

    private fun registerCommands() {
        val lamp: Lamp<BukkitCommandActor> = BukkitLamp.builder(this)
            .build()
        lamp.register(HubCombatCommand(allowCombatHandler))
    }

    private fun registerEvents() {
        server.pluginManager.registerSuspendingEvents(PlayerHitListener(pdcCheckService, allowCombatHandler), this)
        server.pluginManager.registerSuspendingEvents(PlayerDeathListener(pdcCheckService, allowCombatHandler), this)
        server.pluginManager.registerEvents(ItemHoldListener(pdcCheckService, allowCombatHandler, timer), this)
        server.pluginManager.registerEvents(PlayerLeaveListener(timer, allowCombatHandler), this)
        server.pluginManager.registerEvents(PlayerJoinListener(weaponProvideService), this)
    }

    private fun hook(){
        server.pluginManager.getPlugin("PlaceholderAPI")
            ?.let {
                if (it.isEnabled) {
                    PlaceholderAPIHook(timer).register()
                }
            } ?: run {
            logger.warning("PlaceholderAPI is not enabled. Some features may not work.")
        }
    }

}
