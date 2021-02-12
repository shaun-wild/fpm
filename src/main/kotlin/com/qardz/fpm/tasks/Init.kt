package com.qardz.fpm.tasks

import com.qardz.fpm.data.local.Info
import com.qardz.fpm.data.serialization.VersionDeserializer
import com.qardz.fpm.exception.FPMException
import com.qardz.fpm.io.FileManager
import com.qardz.fpm.io.UserInput
import com.qardz.fpm.util.assertInfoNotExists
import org.apache.commons.cli.CommandLine
import java.nio.file.Path

class Init: Task {
    override fun execute(workingDir: Path, cmd: CommandLine) {
        assertInfoNotExists(workingDir)

        val defaultName = workingDir.fileName.toString()
        val defaultVersion = "1.0.0"
        val defaultAuthor = System.getProperty("user.name")

        val name = UserInput.promptUser("Enter an internal mod name", defaultName)
        val version = VersionDeserializer.deserialize(UserInput.promptUser("Enter a version", defaultVersion))
        val author = UserInput.promptUser("Enter an author name", defaultAuthor)
        val title = UserInput.promptUser("Enter a mod title", defaultName)

        val info = Info(name, version, title, author)
        FileManager.saveInfo(workingDir, info)
    }
}
