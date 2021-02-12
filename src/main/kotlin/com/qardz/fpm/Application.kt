package com.qardz.fpm

import com.qardz.fpm.exception.FPMException
import com.qardz.fpm.tasks.TaskRunner
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import kotlin.system.exitProcess

class Application {
    private val options = Options()
    private val taskRunner = TaskRunner()

    init {
        options.addOption("task", "The task to execute (init)")
    }

    fun start(args: Array<String>) {
        val parser = DefaultParser()
        val cmd = parser.parse(options, args)

        val task = if (cmd.args.isNotEmpty()) cmd.args[0] else ""

        try {
            taskRunner.runTask(task, cmd)
        } catch (e: FPMException) {
            println("An error occurred: ${e.message}")
            exitProcess(1)
        } catch (e: Exception) {
            println("An unexpected error occurred: ${e.message}")
            throw e
        }
    }
}

fun main(args: Array<String>) {
    Application().start(args)
}
