package com.qardz.fpm.data.local

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.qardz.fpm.data.serialization.DependencyDeserializer

@JsonSerialize(using = ToStringSerializer::class)
@JsonDeserialize(using = DependencyDeserializer::class)
data class Dependency(
    val prefix: Prefix? = null,
    val internalModName: String,
    val dependencyVersion: DependencyVersion? = null,
) {
    override fun toString(): String {
        var result = internalModName

        if(prefix != null) {
            result = "$prefix $result"
        }

        if(dependencyVersion != null) {
            result = "$result $dependencyVersion"
        }

        return result
    }
}
