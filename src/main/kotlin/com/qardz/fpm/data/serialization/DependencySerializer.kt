package com.qardz.fpm.data.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.qardz.fpm.data.local.Dependency
import com.qardz.fpm.data.local.DependencyVersion
import com.qardz.fpm.data.local.Equality
import com.qardz.fpm.data.local.Prefix
import com.qardz.fpm.exception.FPMException

class DependencyDeserializer : JsonDeserializer<Dependency>() {
    companion object {
        val PATTERN = "(?:(!|\\?|\\(\\?\\)) )?([\\w-]+)(?: (>|>=|=|<=|<) (\\d+(?:\\.\\d+){0,2}))?".toRegex()

        fun deserialize(text: String): Dependency {
            try {
                val (all, prefix, name, equality, version) = PATTERN.matchEntire(text)?.groupValues
                    ?: throw RuntimeException("No")

                val prefixSymbol = Prefix.forSymbol(prefix)
                val equalitySymbol = Equality.forSymbol(equality)

                val dependencyVersion = if (equalitySymbol != null) DependencyVersion(
                    equalitySymbol,
                    VersionDeserializer.deserialize(version)
                ) else null

                return Dependency(prefixSymbol, name, dependencyVersion)
            } catch (e: Exception) {
                throw FPMException("Could not parse dependency string: $text")
            }
        }
    }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Dependency {
        return deserialize(p.text)
    }
}
