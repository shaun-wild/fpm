package com.qardz.fpm.tasks

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.qardz.fpm.data.local.ModList
import com.qardz.fpm.exception.FPMException
import com.qardz.fpm.io.FileManager.getFactorioHome
import org.apache.commons.cli.CommandLine
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.name
import kotlin.streams.toList

class Package : Task {

    private val objectMapper = jacksonObjectMapper().apply {
        enable(SerializationFeature.INDENT_OUTPUT)
    }

    /**
     * Zips up the enabled mod files in the Factorio home mod directory and stores the zip file in the working directory.
     *
     * @param workingDir the working directory to store the zip file in
     * @param cmd the command line arguments
     * @throws FPMException if a mod file is missing for an enabled mod
     */
    override fun execute(workingDir: Path, cmd: CommandLine) {
        val factorioHomeMods = getFactorioHome()
            .resolve(MODS)

        val modFiles = Files.list(factorioHomeMods)
            .toList()

        val modList = getModList(factorioHomeMods)

        val enabledModFiles = modList.mods
            .filter { it.name != "base" && it.enabled }
            .map { mod ->
                modFiles.find { it.name.contains(mod.name) }
                    ?: throw FPMException("Missing mod file for ${mod.name}")
            }
            .map(Path::toFile)

        val outputFile = workingDir
            .resolve("output.zip")

        zipFiles(modList, enabledModFiles, outputFile.toFile())
    }

    private fun getModList(factorioHomeMods: Path) =
        objectMapper.readValue<ModList>(
            factorioHomeMods
                .resolve(MOD_LIST_JSON)
                .toFile()
        )

    private fun zipFiles(modList: ModList, files: List<File>, zipFile: File) {
        val tempFile = Files.createTempFile("mod-list", ".json").toFile()
        objectMapper.writeValue(tempFile, modList.mods.filter { it.enabled })

        ZipOutputStream(zipFile.outputStream()).use { zipOut ->
            zipOut.putNextEntry(ZipEntry("mod-list.json"))
            tempFile.inputStream().use { input -> input.copyTo(zipOut) }

            files.forEach { file ->
                zipOut.putNextEntry(ZipEntry(file.name))

                file.inputStream().use { input ->
                    input.copyTo(zipOut)
                }

                zipOut.closeEntry()
            }
        }
    }

    companion object {
        private const val MODS = "mods"
        private const val MOD_LIST_JSON = "mod-list.json"
    }
}
