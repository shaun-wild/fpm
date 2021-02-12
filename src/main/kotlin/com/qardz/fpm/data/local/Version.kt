package com.qardz.fpm.data.local

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.qardz.fpm.data.serialization.VersionDeserializer

@JsonSerialize(using = ToStringSerializer::class)
@JsonDeserialize(using = VersionDeserializer::class)
data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int? = null,
) {
    override fun toString() =
        if (patch != null) {
            "$major.$minor.$patch"
        } else {
            "$major.$minor"
        }
}
