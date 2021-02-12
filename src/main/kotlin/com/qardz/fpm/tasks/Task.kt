package com.qardz.fpm.tasks

import org.apache.commons.cli.CommandLine
import java.nio.file.Path

interface Task {
    fun execute(workingDir: Path, cmd: CommandLine)
}
