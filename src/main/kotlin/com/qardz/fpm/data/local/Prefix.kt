package com.qardz.fpm.data.local

enum class Prefix(
    val symbol: String,
) {
    INCOMPATIBLE("!"), OPTIONAL("?"), HIDDEN_OPTIONAL("(?)");

    companion object {
        fun forSymbol(symbol: String)= values().find { it.symbol == symbol }
    }

    override fun toString()= symbol
}
