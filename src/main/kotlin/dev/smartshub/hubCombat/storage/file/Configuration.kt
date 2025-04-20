package dev.smartshub.hubCombat.storage.file

import dev.smartshub.hubCombat.HubCombat
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

/**
 * Configuration class for loading and saving YAML configuration files.
 *
 * @param file The file to load the configuration from. If null, the file will be created in the plugin's data folder.
 * @param fileName The name of the configuration file. Should not be null.
 */

open class Configuration(private val plugin: HubCombat, file: File?, fileName: String) : YamlConfiguration(){

    private val file: File = if (file != null && file.isDirectory) {
        File(file, if (fileName.endsWith(".yml")) fileName else "$fileName.yml")
    } else {
        File(plugin.dataFolder, if (fileName.endsWith(".yml")) fileName else "$fileName.yml")
    }


    init {
        saveDefault()
        loadFile()
    }

    private fun loadFile() {
        try {
            this.load(file)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
        }
    }

    private fun saveDefault() {
        if (!file.exists()) {
            plugin.saveResource(file.name, false)
        }
    }

    fun save() {
        try {
            this.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun reloadFile() {
        try {
            loadFile()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}