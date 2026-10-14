package com.example.blackjack

data class PartidaScore(
    val ganador: String,
    val perdedor: String,
    val fecha: String,
    val cartas: List<Carta>,
    val objetivo: Int
)