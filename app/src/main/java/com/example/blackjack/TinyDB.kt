package com.example.blackjack

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class TinyDB(context: Context) {
    private val preferencias = context.getSharedPreferences("blackjack", Context.MODE_PRIVATE)

    fun guardarScores(nuevos: List<PartidaScore>) {
        val todos = nuevos.reversed() + obtenerScores()
        val arreglo = JSONArray()

        for (score in todos) {
            val objeto = JSONObject()
            objeto.put("ganador", score.ganador)
            objeto.put("perdedor", score.perdedor)
            objeto.put("fecha", score.fecha)
            objeto.put("objetivo", score.objetivo)

            val cartas = JSONArray()

            for (carta in score.cartas) {
                val cartaJson = JSONObject()
                cartaJson.put("numero", carta.numero)
                cartaJson.put("palo", carta.palo)
                cartas.put(cartaJson)
            }

            objeto.put("cartas", cartas)
            arreglo.put(objeto)
        }

        preferencias.edit().putString("scores", arreglo.toString()).apply()
    }

    fun obtenerScores(): List<PartidaScore> {
        val lista = mutableListOf<PartidaScore>()
        val texto = preferencias.getString("scores", "[]")
        val arreglo = JSONArray(texto)

        for (i in 0 until arreglo.length()) {
            val objeto = arreglo.getJSONObject(i)
            val cartasJson = objeto.getJSONArray("cartas")
            val cartas = mutableListOf<Carta>()

            for (j in 0 until cartasJson.length()) {
                val carta = cartasJson.getJSONObject(j)
                cartas.add(Carta(carta.getString("numero"), carta.getString("palo")))
            }

            lista.add(PartidaScore(
                objeto.getString("ganador"),
                objeto.getString("perdedor"),
                objeto.getString("fecha"),
                cartas,
                objeto.getInt("objetivo")
            ))
        }

        return lista
    }
}