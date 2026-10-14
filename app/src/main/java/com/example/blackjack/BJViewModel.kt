package com.example.blackjack

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BJViewModel : ViewModel() {
    private val juego = Juego()

    var nombre by mutableStateOf("Jugador")
    var objetivo by mutableIntStateOf(21)
    var cartasJugador by mutableStateOf<List<Carta>>(emptyList())
    var cartasCpu by mutableStateOf<List<Carta>>(emptyList())
    var resultado by mutableStateOf("")
    var tema by mutableIntStateOf(0)
    private val pendientes = mutableListOf<PartidaScore>()
    private var registrada = false
    val puntosJugador: Int get() = juego.calcularPuntos(cartasJugador)

    val puntosCpu: Int get() = juego.calcularPuntos(cartasCpu)

    fun iniciar(nombreJugador: String, numero: Int) {
        nombre = nombreJugador
        objetivo = numero
        pendientes.clear()
        nuevaRonda()
    }

    fun nuevaRonda() {
        registrada = false
        juego.iniciar(objetivo)
        actualizar()
    }

    fun pedir() {
        juego.pedir()
        actualizar()
    }

    fun plantarse() {
        juego.plantarse()
        actualizar()
    }

    fun actualizar() {
        cartasJugador = juego.jugador.toList()
        cartasCpu = juego.cpu.toList()
        resultado = juego.resultado

        if (resultado != "" && !registrada) {
            if (resultado != "EMPATE") {
                var ganador = nombre
                var perdedor = "CPU"
                var cartasGanadoras = cartasJugador

                if (resultado == "PERDISTE") {
                    ganador = "CPU"
                    perdedor = nombre
                    cartasGanadoras = cartasCpu
                }

                val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                pendientes.add(PartidaScore(ganador, perdedor, fecha, cartasGanadoras.toList(), objetivo))
            }
            registrada = true
        }
    }

    fun salir(tinyDB: TinyDB) {
        if (pendientes.isNotEmpty()) {
            tinyDB.guardarScores(pendientes)
            pendientes.clear()
        }
    }
}