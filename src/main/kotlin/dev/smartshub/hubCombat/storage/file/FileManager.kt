package dev.smartshub.hubCombat.storage.file

import dev.smartshub.hubCombat.HubCombat
import java.io.File

/**
 * FileManager is responsible for managing configuration files in the plugin.
 * It loads, saves, and reloads configuration files as needed.
 *
 * @property configs A map of configuration file names to their corresponding Configuration objects.
 * @property plugin The instance of the JavaPlugin that this FileManager is associated with.
 */

object FileManager {

    private val configs = mutableMapOf<String, Configuration>()
    private  lateinit var plugin: HubCombat

    fun init(plugin: HubCombat) {
        FileManager.plugin = plugin
        load("config")
        load("lang")
    }

    private fun load(name: String) {
        val fileName = fixName(name)
        val file = File(plugin.dataFolder, fileName)
        val config = Configuration(plugin, file, fileName)

        config.load(file)
        configs[fileName] = config
    }

    fun reload(name: String) {
        val fileName = fixName(name)
        val config = configs[fileName] ?: return
        config.reloadFile()
    }

    fun save(name: String) {
        val fileName = fixName(name)
        val config = configs[fileName] ?: return
        val file = File(plugin.dataFolder, fileName)
        config.save(file)
    }

    fun get(name: String): Configuration? {
        return configs[fixName(name)]
    }

    // Make sure that the file name ends with .yml
    private fun fixName(name: String): String {
        return if (name.endsWith(".yml")) name else "$name.yml"
    }

}