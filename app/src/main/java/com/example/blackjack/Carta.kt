package com.example.blackjack

data class Carta(val numero: String, val palo: String) {
    fun valor(): Int {
        if (numero == "A") return 11
        if (numero == "J" || numero == "Q" || numero == "K") return 10
        return numero.toInt()
    }
}