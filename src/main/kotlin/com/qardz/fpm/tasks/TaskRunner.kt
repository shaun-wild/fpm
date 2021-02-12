package com.qardz.fpm.tasks

import com.qardz.fpm.exception.FPMException
import org.apache.commons.cli.CommandLine
import java.nio.file.Paths

class TaskRunner {
    val tasks = mapOf(
        "init" to Init(),
        "add" to Add(),
        "start" to Start(),
        "login" to Login()
    )

    fun runTask(task: String?, cmd: CommandLine) {
        if (task == null || task.length == 0) {
            throw FPMException("Must specify task to execute.")
        }

        val workingDir = Paths.get("").toAbsolutePath()
        tasks[task]?.execute(workingDir, cmd) ?: throw FPMException("Task not found ($task)")
    }
}