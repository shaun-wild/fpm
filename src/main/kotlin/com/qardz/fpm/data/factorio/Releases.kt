package com.qardz.fpm.data.factorio

import com.qardz.fpm.data.local.Version

data class Releases(
    val experimental: Release,
    val stable: Release,
)

data class Release(
    val alpha: Version,
    val demo: Version,
    val headless: Version,
)
