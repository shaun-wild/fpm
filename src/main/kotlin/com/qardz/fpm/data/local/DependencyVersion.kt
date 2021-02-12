package com.qardz.fpm.data.local

data class DependencyVersion(
    val equality: Equality,
    val version: Version
) {
    override fun toString() = "${equality.symbol} $version"
}
