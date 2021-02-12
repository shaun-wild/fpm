package com.qardz.fpm.data.local

import java.nio.file.Path

data class LocalMod(
    val name: String,
    val version: Version,
    val path: Path,
)
