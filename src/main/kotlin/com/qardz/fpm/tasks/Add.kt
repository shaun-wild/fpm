package com.qardz.fpm.tasks

import com.qardz.fpm.data.local.Dependency
import com.qardz.fpm.data.local.Info
import com.qardz.fpm.data.local.Prefix
import com.qardz.fpm.exception.FPMException
import com.qardz.fpm.http.FactorioModPortal
import com.qardz.fpm.io.FileManager
import com.qardz.fpm.util.assertInfoExists
import org.apache.commons.cli.CommandLine
import java.nio.file.Path

class Add : Task {
    override fun execute(workingDir: Path, cmd: CommandLine) {
        assertInfoExists(workingDir)

        val info = FileManager.parseInfo(workingDir)

        val dependency = cmd.args[1]
        val newInfo = addDependency(dependency, info)

        FileManager.saveInfo(workingDir, newInfo)
    }

    private fun addDependency(name: String, info: Info): Info {
        val newDependencies = info.dependencies?.toMutableList() ?: mutableListOf()

        if(info.getDependency(name) != null) {
            throw FPMException("Dependency already added.")
        }

        val factorioMod = FactorioModPortal.getMod(name)
            ?: throw FPMException("Mod not found on mod portal: $name.")

        newDependencies.add(Dependency(internalModName = name, prefix = Prefix.HIDDEN_OPTIONAL))
        println("Added mod ${factorioMod.title} - ${factorioMod.summary}")
        return info.copy(dependencies = newDependencies)
    }
}
