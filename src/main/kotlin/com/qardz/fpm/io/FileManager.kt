package com.qardz.fpm.io

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.qardz.fpm.data.local.Info
import org.apache.commons.lang3.SystemUtils
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

class FileManager {
    companion object {
        private const val MOD_INFO_FILE = "mod-info.json"
        private const val FPM_HOME = ".fpm/"
        private const val CONFIG_FILE = "config.properties"

        val objectMapper = jacksonObjectMapper()

        init {
            objectMapper.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT)

            getConfigPath().toFile().let {
                it.parentFile.mkdirs()

                if(!it.exists()) {
                    it.createNewFile()
                }
            }
        }

        fun infoExists(path: Path) = Files.exists(path.resolve(MOD_INFO_FILE))
        fun parseInfo(path: Path) = objectMapper.readValue<Info>(path.resolve(MOD_INFO_FILE).toFile())
        fun saveInfo(path: Path, info: Info) = objectMapper.writeValue(path.resolve(MOD_INFO_FILE).toFile(), info)

        fun getConfig(): Properties {
            val properties = Properties()
            properties.load(getConfigPath().toFile().inputStream())
            return properties
        }

        fun saveConfig(properties: Properties) = properties.store(getConfigPath().toFile().outputStream(), null)

        fun getConfigPath() = Paths.get(SystemUtils.getUserHome().path).resolve(FPM_HOME).resolve(CONFIG_FILE)
        fun getHomePath() = Paths.get(SystemUtils.getUserHome().path).resolve(FPM_HOME)
    }
}
