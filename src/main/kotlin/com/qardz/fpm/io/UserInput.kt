package com.qardz.fpm.io

import java.io.Console
import java.util.*

class UserInput {
    companion object {
        val scanner = Scanner(System.`in`)

        tailrec fun promptUser(message: String, default: String? = null, accept: (String) -> Boolean = {true}): String {
            if(default != null) {
                println("$message ($default):")
            } else {
                println(message)
            }

            var response = scanner.nextLine()

            if(response.isBlank()) {
               response = default
            }

            return if(response != null && accept.invoke(response)) {
                response
            } else {
                promptUser(message, default, accept)
            }
        }


        fun promptSecure(message: String)= System.console().readPassword(message)
    }
}
