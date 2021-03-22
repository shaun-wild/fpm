package com.qardz.fpm.data.local

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.qardz.fpm.exception.FPMException

@JsonSerialize(using = ToStringSerializer::class)
data class Dependency(
    val prefix: Prefix? = null,
    val internalModName: String,
    val dependencyVersion: DependencyVersion? = null,
) {

    companion object {
        val PATTERN = "(?:(!|\\?|\\(\\?\\)) )?([\\w-]+)(?: (>|>=|=|<=|<) (\\d+(?:\\.\\d+){0,2}))?".toRegex()

        @JvmStatic
        @JsonCreator
        fun parse(text: String): Dependency {
            try {
                val (all, prefix, name, equality, version) = PATTERN.matchEntire(text)?.groupValues
                    ?: throw RuntimeException("No")

                val prefixSymbol = Prefix.forSymbol(prefix)
                val equalitySymbol = Equality.forSymbol(equality)

                val dependencyVersion = if (equalitySymbol != null) DependencyVersion(
                    equalitySymbol,
                    Version.parse(version)
                ) else null

                return Dependency(prefixSymbol, name, dependencyVersion)
            } catch (e: Exception) {
                throw FPMException("Could not parse dependency string: $text")
            }
        }
    }

    override fun toString(): String {
        var result = internalModName

        if (prefix != null) {
            result = "$prefix $result"
        }

        if (dependencyVersion != null) {
            result = "$result $dependencyVersion"
        }

        return result
    }
}
