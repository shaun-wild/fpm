package com.qardz.fpm.data.local

data class ModList(
    val mods: List<ModListItem>
)

data class ModListItem(
    val name: String,
    val enabled: Boolean
)
