package com.qardz.fpm.util

import org.apache.commons.io.IOUtils
import java.nio.file.Path

class Factorio(val gameExecutable: Path, val arguments: Map<Argument, String>) {

    fun launch(): Process {
        val args = arguments
            .map { "${it.key} ${it.value}" }
            .toTypedArray()

        val process = Runtime.getRuntime().exec(arrayOf(gameExecutable.toString(), *args))
        IOUtils.copy(process.inputStream, System.out)
        return process
    }

}

enum class Argument {
    MOD_DIRECTORY;

    override fun toString() = "--${name.replace("_", "-").toLowerCase()}"
}