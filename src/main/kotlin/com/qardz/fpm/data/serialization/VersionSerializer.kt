package com.qardz.fpm.data.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.qardz.fpm.data.local.Version
import com.qardz.fpm.exception.FPMException

class VersionDeserializer : JsonDeserializer<Version>() {
    companion object {
        fun deserialize(version: String): Version {
            val tokens = version
                .split(".")
                .map(String::toInt)

            if (tokens.size == 2) {
                val (major, minor) = version
                    .split(".")
                    .map(String::toInt)

                return Version(major, minor)
            }

            if (tokens.size == 3) {
                val (major, minor, patch) = version
                    .split(".")
                    .map(String::toInt)

                return Version(major, minor, patch)
            }

            throw FPMException("Could not parse version: $version")
        }
    }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Version {
        return deserialize(p.text)
    }
}
