package com.qardz.fpm.data.local

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.qardz.fpm.exception.FPMException

@JsonSerialize(using = ToStringSerializer::class)
data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int? = null,
) {

    companion object {

        @JvmStatic
        @JsonCreator
        fun parse(version: String): Version {
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

    override fun toString() =
        if (patch != null) {
            "$major.$minor.$patch"
        } else {
            "$major.$minor"
        }
}
