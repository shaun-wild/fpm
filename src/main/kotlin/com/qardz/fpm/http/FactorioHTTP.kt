package com.qardz.fpm.http

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.qardz.fpm.data.factorio.Authentication
import com.qardz.fpm.exception.FPMException
import com.qardz.fpm.io.FileManager
import kong.unirest.ObjectMapper
import kong.unirest.Unirest

class FactorioHTTP {
    companion object {
        const val FACTORIO_LOGIN_URL = "https://auth.factorio.com/api-login"

        val client = jacksonObjectMapper()

        init {
            client.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
            client.registerModule(JavaTimeModule())

            Unirest.config().objectMapper = JacksonObjectMapper(client)
        }

        fun authenticate(): Authentication {
            val config = FileManager.getConfig()

            val token = config.getProperty("token")
            val username = config.getProperty("username")

            if (username != null && token != null) {
                return Authentication(username, token)
            }

            throw FPMException("Not authenticated! run fpm login")
        }

        fun authenticate(username: String, password: String): Authentication {
            val config = FileManager.getConfig()

            val response = Unirest.post(FACTORIO_LOGIN_URL)
                .field("username", username)
                .field("password", password)
                .asJson().body

            response.`object`?.getString("error")?.let { throw FPMException(it) }
            val token = response.array.getString(0)

            config.setProperty("token", token)
            config.setProperty("username", username)
            FileManager.saveConfig(config)

            return Authentication(username, token)
        }
    }
}

class JacksonObjectMapper(val mapper: com.fasterxml.jackson.databind.ObjectMapper) : ObjectMapper {
    override fun <T : Any?> readValue(value: String?, valueType: Class<T>?): T = mapper.readValue(value, valueType)
    override fun writeValue(value: Any?): String = mapper.writeValueAsString(value)
}
