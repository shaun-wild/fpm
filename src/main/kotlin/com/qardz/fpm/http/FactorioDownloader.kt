package com.qardz.fpm.http

import com.fasterxml.jackson.module.kotlin.readValue
import com.qardz.fpm.data.factorio.Releases
import com.qardz.fpm.data.local.Version
import com.qardz.fpm.exception.FPMException
import com.qardz.fpm.http.Build.ALPHA
import com.qardz.fpm.http.Distro.*
import com.qardz.fpm.io.FileManager
import kong.unirest.Unirest
import org.apache.commons.lang3.SystemUtils.*
import java.net.URL
import java.text.MessageFormat.format

class FactorioDownloader {

    companion object {
        val RELEASES_URL = URL("https://factorio.com/api/latest-releases")
        const val BASE_URL_TEMPLATE = "https://factorio.com/get-download/{0}/{1}/{2}"

        fun downloadFactorio(version: Version) {
            val auth = FactorioHTTP.authenticate()
            val url = getDownloadURL(version)
            println("Downloading factorio version $version from $url")

            val response = Unirest.get(url)
                .queryString("username", auth.username)
                .queryString("token", auth.token)
                .asEmpty()

            val downloadUrl = response.headers["Location"][0]

            val download = Unirest.get(downloadUrl)
                .downloadMonitor { field, fileName, bytesWritten, totalBytes -> println("Downloading ($bytesWritten/$totalBytes)") }
                .asFile(FileManager.getHomePath().resolve("$version/factorio-$version.zip").toAbsolutePath().toString())

            if(download.isSuccess) {
                println("Download complete")
            } else {
                throw FPMException("Download failed: ${response.statusText} (${response.status})")
            }
        }

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
