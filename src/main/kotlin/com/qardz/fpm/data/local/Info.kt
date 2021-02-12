package com.qardz.fpm.data.local

data class Info(
    val name: String,
    val version: Version,
    val title: String,
    val author: String,
    val contact: String? = null,
    val homepage: String?= null,
    val description: String? = null,
    val factorioVersion: String? = null,
    val dependencies: List<Dependency>? = null
) {
    fun getDependency(name: String): Dependency? {
        return dependencies?.find { it.internalModName == name }
    }
}
