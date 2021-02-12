package com.qardz.fpm.tasks

import com.qardz.fpm.http.FactorioDownloader
import com.qardz.fpm.io.FileManager
import com.qardz.fpm.util.Argument
import com.qardz.fpm.util.Factorio
import com.qardz.fpm.util.assertInfoExists
import org.apache.commons.cli.CommandLine
import java.nio.file.Path

class Start: Task {

    override fun execute(workingDir: Path, cmd: CommandLine) {
        assertInfoExists(workingDir)
        val info = FileManager.parseInfo(workingDir)

        val releases = FactorioDownloader.getLatestReleases()

        val game = FactorioDownloader.downloadFactorio(releases.experimental.alpha)
        val executable = game.resolve("bin/x64/factorio.exe")

        println("Starting game")

        val factorio = Factorio(executable, mapOf(Argument.MOD_DIRECTORY to workingDir.toString()))
        val process = factorio.launch()

        println("Game quit (${process.exitValue()})")
    }
}
