package com.qardz.fpm.tasks

import com.qardz.fpm.http.FactorioHTTP
import com.qardz.fpm.io.UserInput
import org.apache.commons.cli.CommandLine
import java.nio.file.Path

class Login: Task {
    override fun execute(workingDir: Path, cmd: CommandLine) {
        val username = cmd.args.getOrNull(1) ?: UserInput.promptUser("Please enter your username/email address:")
        val password = cmd.args.getOrNull(2) ?: String(UserInput.promptSecure("Please enter your password:"))

        FactorioHTTP.authenticate(username, password)
        println("Authenticated successfully.")
    }
}