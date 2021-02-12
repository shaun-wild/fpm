package com.qardz.fpm.tasks

import com.qardz.fpm.http.FactorioDownloader
import com.qardz.fpm.io.FileManager
import com.qardz.fpm.util.assertInfoExists
import org.apache.commons.cli.CommandLine
import java.nio.file.Path

class Start: Task {

    override fun execute(workingDir: Path, cmd: CommandLine) {
        assertInfoExists(workingDir)
        val info = FileManager.parseInfo(workingDir)

        val releases = FactorioDownloader.getLatestReleases()

        FactorioDownloader.downloadFactorio(releases.experimental.alpha)
    }
}