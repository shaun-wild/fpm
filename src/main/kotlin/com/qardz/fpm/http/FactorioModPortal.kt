package com.qardz.fpm.http

import com.fasterxml.jackson.module.kotlin.readValue
import com.qardz.fpm.data.factorio.Mod
import java.net.URL

class FactorioModPortal {

    companion object {
        private const val FACTORIO_ENDPOINT = "https://mods.factorio.com/api/mods"

        fun getMod(modName: String) =
            try {
                FactorioHTTP.client.readValue<Mod>(URL("$FACTORIO_ENDPOINT/$modName"))
            } catch (e: Exception) {
                null
            }
    }
}
