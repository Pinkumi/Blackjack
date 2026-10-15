package com.example.blackjack

class Juego {
    private var mazo = mutableListOf<Carta>()
    var jugador = mutableListOf<Carta>()
    var cpu = mutableListOf<Carta>()
    var objetivo = 21
    var resultado = ""

    fun barajearMazo() {
        mazo.clear()
        val palos = listOf("♠", "♥", "♣", "♦")
        val numeros = listOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")

        for (palo in palos) {
            for (numero in numeros) {
                mazo.add(Carta(numero, palo))
            }
        }
        mazo.shuffle()
    }

    fun sacarCarta(): Carta {
        return mazo.removeAt(0)
    }

    fun calcularPuntos(cartas: List<Carta>): Int {
        var total = 0
        var ases = 0

        for (carta in cartas) {
            total += carta.valor()
            if (carta.numero == "A") ases++
        }

        while (total > objetivo && ases > 0) {
            total -= 10
            ases--
        }
        return total
    }

    fun iniciar(numero: Int) {
        objetivo = numero
        resultado = ""
        jugador.clear()
        cpu.clear()
        barajearMazo()

        jugador.add(sacarCarta())
        cpu.add(sacarCarta())
        jugador.add(sacarCarta())
        cpu.add(sacarCarta())

        val puntosJugador = calcularPuntos(jugador)
        val puntosCpu = calcularPuntos(cpu)

        if (puntosJugador > objetivo) {
            resultado = "PERDISTE"
        } else if (puntosCpu > objetivo) {
            resultado = "GANASTE"
        } else if (puntosJugador == objetivo && puntosCpu == objetivo) {
            resultado = "EMPATE"
        } else if (puntosJugador == objetivo) {
            resultado = "GANASTE"
        } else if (puntosCpu == objetivo) {
            resultado = "PERDISTE"
        }
    }

    fun pedir() {
        if (resultado != "") return

        jugador.add(sacarCarta())
        val puntos = calcularPuntos(jugador)

        if (puntos > objetivo) {
            resultado = "PERDISTE"
        } else if (puntos == objetivo) {
            turnoCpu()
        }
    }

    fun plantarse() {
        if (resultado != "") return
        turnoCpu()
    }

    fun turnoCpu() {
        val min = objetivo-4

        while (calcularPuntos(cpu) < min && mazo.isNotEmpty()) {
            cpu.add(sacarCarta())
        }

        val puntosJugador = calcularPuntos(jugador)
        val puntosCpu = calcularPuntos(cpu)

        if (puntosCpu > objetivo) {
            resultado = "GANASTE"
        } else if (puntosJugador > puntosCpu) {
            resultado = "GANASTE"
        } else if (puntosCpu > puntosJugador) {
            resultado = "PERDISTE"
        } else {
            resultado = "EMPATE"
        }
    }
}