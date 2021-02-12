package com.qardz.fpm.data.serialization

import com.qardz.fpm.data.local.DependencyVersion
import com.qardz.fpm.data.local.Equality
import com.qardz.fpm.data.local.Prefix
import com.qardz.fpm.data.local.Version
import com.qardz.fpm.exception.FPMException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DependencySerialization {

    @Test
    fun `just mod name deserialize`() {
        val text = "mod-name"
        val dependency = DependencyDeserializer.deserialize(text)

        assertNull(dependency.prefix)
        assertEquals(text, dependency.internalModName)
        assertNull(dependency.dependencyVersion)
    }

    @Test
    fun `mod name and prefix deserialize`() {
        val text = "(?) mod-name"
        val dependency = DependencyDeserializer.deserialize(text)

        assertEquals(Prefix.HIDDEN_OPTIONAL, dependency.prefix)
        assertEquals("mod-name", dependency.internalModName)
        assertNull(dependency.dependencyVersion)
    }

    @Test
    fun `mod name, prefix and version deserialize`() {
        val text = "! mod-name >= 1.1.2"
        val dependency = DependencyDeserializer.deserialize(text)

        assertEquals(Prefix.INCOMPATIBLE, dependency.prefix)
        assertEquals("mod-name", dependency.internalModName)
        assertEquals(DependencyVersion(Equality.GREATER_THAN_EQUAL, Version(1, 1, 2)), dependency.dependencyVersion)
    }

    @Test
    fun `mod name, prefix and version deserialize 2`() {
        val text = "(?) mod-name = 2.25"
        val dependency = DependencyDeserializer.deserialize(text)

        assertEquals(Prefix.HIDDEN_OPTIONAL, dependency.prefix)
        assertEquals("mod-name", dependency.internalModName)
        assertEquals(DependencyVersion(Equality.EQUAL, Version(2, 25)), dependency.dependencyVersion)
    }

    @Test
    fun `mod name and version deserialize`() {
        val text = "mod-name < 1.0"
        val dependency = DependencyDeserializer.deserialize(text)

        assertNull(dependency.prefix)
        assertEquals("mod-name", dependency.internalModName)
        assertEquals(DependencyVersion(Equality.LESS_THAN, Version(1, 0)), dependency.dependencyVersion)
    }

    @Test
    fun `bad version fails`() {
        val text = "mod-name < test"
        assertThrows<FPMException> { DependencyDeserializer.deserialize(text) }
    }

    @Test
    fun `bad version fails 2`() {
        val text = "mod-name < 23.2.2.2"
        assertThrows<FPMException> { DependencyDeserializer.deserialize(text) }
    }
}
