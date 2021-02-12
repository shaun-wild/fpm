package com.qardz.fpm.http

import com.fasterxml.jackson.module.kotlin.readValue
import com.qardz.fpm.data.factorio.Releases
import com.qardz.fpm.data.local.Version
import com.qardz.fpm.exception.FPMException
import com.qardz.fpm.http.Build.ALPHA
import com.qardz.fpm.http.Distro.*
import com.qardz.fpm.io.FileManager
import kong.unirest.Unirest
import net.lingala.zip4j.ZipFile
import org.apache.commons.lang3.SystemUtils.*
import java.io.File
import java.net.URL
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.text.MessageFormat.format

class FactorioDownloader {

    companion object {
        val RELEASES_URL = URL("https://factorio.com/api/latest-releases")
        const val BASE_URL_TEMPLATE = "https://factorio.com/get-download/{0}/{1}/{2}"

        fun downloadFactorio(version: Version): Path {
            val localGame = getLocalGame(version)

            if (localGame.toFile().exists()) {
                println("Game already downloaded.")
                return localGame
            }

            val auth = FactorioHTTP.authenticate()
            val url = getDownloadURL(version)
            println("Downloading factorio version $version from $url")

            val tempFile = File.createTempFile("factorio", "$version.zip")
            tempFile.deleteOnExit()

            val response = Unirest.get(url)
                .queryString("username", auth.username)
                .queryString("token", auth.token)
                .asFile(tempFile.absolutePath, StandardCopyOption.REPLACE_EXISTING)

            if (response.isSuccess) {
                println("Download complete.")
            } else {
                val error =
                    response.parsingError.map { it.message }.orElse("${response.statusText} (${response.status})")
                throw FPMException("Download failed: $error")
            }

            println("Unzipping Factorio $version")
            val downloadDirectory = FileManager.getHomePath().resolve("downloads/game")
            downloadDirectory.toFile().mkdirs()
            ZipFile(tempFile).extractAll(downloadDirectory.toString())
            println("Done!")

            return getLocalGame(version)
        }

        fun getLocalGame(version: Version) =
            FileManager.getHomePath().resolve("downloads/game/Factorio_$version")

        fun getLatestReleases() = FactorioHTTP.client.readValue<Releases>(RELEASES_URL)

        private fun getDownloadURL(version: Version) = format(
            BASE_URL_TEMPLATE, version, ALPHA, when {
                IS_OS_WINDOWS -> WIN64_MANUAL
                IS_OS_MAC -> OSX
                IS_OS_LINUX -> LINUX64
                else -> throw FPMException("Unknown operating system.")
            }
        )
    }
}

enum class Build {
    ALPHA, DEMO, HEADLESS;

    override fun toString() = name.toLowerCase()
}

enum class Distro(val slug: String? = null) {
    WIN64, WIN64_MANUAL("win64-manual"), WIN32, WIN32_MANUAL("win32-manual"), OSX, LINUX64, LINUX32;

    override fun toString() = slug ?: name.toLowerCase()
}
