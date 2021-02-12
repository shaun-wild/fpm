package com.qardz.fpm.data.serialization

import com.fasterxml.jackson.module.kotlin.readValue
import com.qardz.fpm.data.local.*
import com.qardz.fpm.io.FileManager
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InfoSerialization {

    val info = Info(
        "my-mod",
        Version(15, 99, 203),
        "My Mod",
        "Shaun",
        "1.1",
        dependencies = listOf(
            Dependency(
                Prefix.INCOMPATIBLE,
                "other-mod",
                DependencyVersion(Equality.GREATER_THAN, Version(1, 11, 0))
            )
        )
    )

    val json = "{\n" +
            "  \"name\" : \"my-mod\",\n" +
            "  \"version\" : \"15.99.203\",\n" +
            "  \"title\" : \"My Mod\",\n" +
            "  \"author\" : \"Shaun\",\n" +
            "  \"factorio_version\" : \"1.1\",\n" +
            "  \"dependencies\" : [ \"! other-mod > 1.11.0\" ]\n" +
            "}"

    @Test
    fun `info is serialized`() {
        val res = FileManager.objectMapper.writeValueAsString(info)
        assertTrue(res.contains("! other-mod > 1.11.0"))
    }

    @Test
    fun `info is deserialized`() {
        val res = FileManager.objectMapper.readValue<Info>(json)
        assertEquals(res, info)
    }
}
