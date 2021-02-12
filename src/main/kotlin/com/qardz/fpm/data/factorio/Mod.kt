package com.qardz.fpm.data.factorio

import com.qardz.fpm.data.local.Version
import java.math.BigDecimal
import java.time.ZonedDateTime

data class Mod(
    val category: String,
    val downloadsCount: Int,
    val name: String,
    val owner: String,
    val releases: List<ModRelease>,
    val score: BigDecimal,
    val summary: String,
    val thumbnail: String,
    val title: String,
)

data class ModRelease(
    val downloadUrl: String,
    val fileName: String,
    val infoJson: ModInfo,
    val releasedAt: ZonedDateTime,
    val sha1: String,
    val version: Version
)

data class ModInfo(
    val factorioVersion: String,
)