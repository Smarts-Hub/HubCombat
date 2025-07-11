package dev.smartshub.hubCombat

import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import dev.smartshub.hubCombat.combat.AllowCombatHandler
import dev.smartshub.hubCombat.command.HubCombatCommand
import dev.smartshub.hubCombat.hook.PlaceholderAPIHook
import dev.smartshub.hubCombat.item.LoadOutManager
import dev.smartshub.hubCombat.listener.*
import dev.smartshub.hubCombat.service.CooldownService
import dev.smartshub.hubCombat.service.PDCCheckService
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
    private lateinit var allowCombatHandler: AllowCombatHandler
    private lateinit var loadOutManager: LoadOutManager
    private lateinit var pdcCheckService: PDCCheckService
    private lateinit var cooldownService: CooldownService

    override fun onEnable() {

        // Init managers, services and objects
        initObjects()

        // Start the timer task async
        timer.runTaskTimerAsynchronously(this, 0L, 20L)

        // Register commands and events
        registerCommands()
        registerEvents()

        // Hook other plugins
        hook()

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
        loadOutManager = LoadOutManager(this)
        allowCombatHandler = AllowCombatHandler(this)
        pdcCheckService = PDCCheckService(this)
        cooldownService = CooldownService()
        timer = Timer(this, allowCombatHandler, pdcCheckService, cooldownService)
    }

    private fun registerCommands() {
        val lamp: Lamp<BukkitCommandActor> = BukkitLamp.builder(this)
            .build()
        lamp.register(HubCombatCommand(allowCombatHandler))
    }

    private fun registerEvents() {
        server.pluginManager.registerSuspendingEvents(PlayerHitListener(pdcCheckService, allowCombatHandler), this)
        server.pluginManager.registerSuspendingEvents(PlayerDeathListener(pdcCheckService, allowCombatHandler), this)
        server.pluginManager.registerEvents(PlayerRespawnListener(loadOutManager, pdcCheckService, cooldownService), this)
        server.pluginManager.registerEvents(PlayerJoinListener(loadOutManager, pdcCheckService, cooldownService), this)
        server.pluginManager.registerEvents(ItemHoldListener(pdcCheckService, allowCombatHandler, cooldownService), this)
        server.pluginManager.registerEvents(PlayerLeaveListener(allowCombatHandler, cooldownService), this)
        server.pluginManager.registerEvents(ItemDamageListener(pdcCheckService), this)
        server.pluginManager.registerEvents(DropItemListener(pdcCheckService), this)
    }

    private fun hook(){
        if(server.pluginManager.getPlugin("PlaceholderAPI") != null) {
            PlaceholderAPIHook(cooldownService, allowCombatHandler).register()
        }
    }

}
