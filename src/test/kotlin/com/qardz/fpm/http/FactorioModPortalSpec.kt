package com.qardz.fpm.http

import org.junit.jupiter.api.Test

class FactorioModPortalSpec {

    @Test
    fun `factorio mod portal hit`() {
        val t = FactorioModPortal.getMod("statorio")
        println(t)
    }

}