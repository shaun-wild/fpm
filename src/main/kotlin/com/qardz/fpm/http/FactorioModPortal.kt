package com.qardz.fpm.http

import com.fasterxml.jackson.module.kotlin.readValue
import com.qardz.fpm.data.factorio.Mod
import com.qardz.fpm.data.local.LocalMod
import com.qardz.fpm.data.local.Version
import com.qardz.fpm.exception.FPMException
import com.qardz.fpm.io.FileManager
import kong.unirest.Unirest
import java.net.URL
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class FactorioModPortal {

    companion object {
        private const val FACTORIO_ENDPOINT = "https://mods.factorio.com"
        private const val MODS_PATH = "api/mods"

        fun downloadMod(name: String, version: Version? = null): LocalMod {
            val mod = getMod(name)  ?: throw FPMException("Could not find $name")

            val download = if (version != null) {
                mod.releases.find { it.version == version } ?: throw FPMException("Could not find $mod ($version)")
            } else {
                mod.releases.last()
            }

            val modsFile = FileManager.getHomePath().resolve("downloads/mods/${mod.name}/${download.version}")
            modsFile.toFile().mkdirs()

            val modZip = modsFile.resolve(download.fileName)

            if(modZip.toFile().exists()) {
                println("Mod already exists")
                return LocalMod(download.fileName, download.version, modZip)
            }

            val auth = FactorioHTTP.authenticate()
            val response = Unirest.get("${FACTORIO_ENDPOINT}${download.downloadUrl}")
                .queryString("username", auth.username)
                .queryString("token", auth.token)
                .asFile(modZip.toAbsolutePath().toString(), StandardCopyOption.REPLACE_EXISTING)

            if (response.isSuccess) {
                return LocalMod(download.fileName, download.version, modZip)
            } else {
                val error =
                    response.parsingError.map { it.message }.orElse("${response.statusText} (${response.status})")
                throw FPMException("Download failed: $error")
            }
        }

        fun getMod(modName: String) =
            try {
                FactorioHTTP.client.readValue<Mod>(URL("$FACTORIO_ENDPOINT/$MODS_PATH/$modName"))
            } catch (e: Exception) {
                null
            }
    }
}
