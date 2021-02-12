package com.qardz.fpm.data.local

enum class Equality (
    val symbol: String
) {
    LESS_THAN("<"), LESS_THAN_EQUAL("<="), EQUAL("="), GREATER_THAN_EQUAL(">="), GREATER_THAN(">");

    companion object {
        fun forSymbol(symbol: String)= values().find { it.symbol == symbol }
    }

    override fun toString()=symbol
}
