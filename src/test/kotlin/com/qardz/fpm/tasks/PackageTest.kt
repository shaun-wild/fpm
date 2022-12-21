package com.qardz.fpm.tasks

import io.mockk.mockk
import org.apache.commons.cli.CommandLine
import org.junit.jupiter.api.Test
import java.nio.file.Path

internal class PackageTest {

    val commandLine = mockk<CommandLine>()

    @Test
    fun `Package print mods`() {
        val pack = Package()

        pack.execute(Path.of(""), commandLine)
    }
}
