
package com.example.blackjack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.card.ui.theme.CardTheme

val fuente = FontFamily(Font(R.font.londrina_solid))

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: BJViewModel = viewModel()

            CardTheme(tema = vm.tema) {
                AppBlackjack(vm)
            }
        }
    }
}

@Composable
fun AppBlackjack(vm: BJViewModel) {
    val contexto = LocalContext.current
    val tinyDB = remember { TinyDB(contexto) }

    var pantalla by rememberSaveable { mutableStateOf("menu") }
    var mostrarVentana by remember { mutableStateOf(false) }
    var scores by remember { mutableStateOf<List<PartidaScore>>(emptyList()) }

    if (pantalla == "menu") {
        MenuScreen(
            temaActual = vm.tema,
            onJugar = { mostrarVentana = true },
            onScore = {
                scores = tinyDB.obtenerScores()
                pantalla = "score"
            },
            onInstrucciones = { pantalla = "instrucciones" },
            onTema = { vm.tema = it }
        )
    }

    if (pantalla == "score") {
        ScoreScreen(
            scores = scores,
            onSalir = { pantalla = "menu" }
        )
    }

    if (pantalla == "instrucciones") {
        InstruccionesScreen(
            onSalir = { pantalla = "menu" }
        )
    }

    if (pantalla == "juego") {
        JuegoScreen(
            vm = vm,
            onSalir = {
                vm.salir(tinyDB)
                pantalla = "menu"
            }
        )
    }

    if (mostrarVentana) {
        VentanaInicio(
            onCerrar = { mostrarVentana = false },
            onIniciar = { nombre, numero ->
                vm.iniciar(nombre, numero)
                mostrarVentana = false
                pantalla = "juego"
            }
        )
    }
}

@Composable
fun PantallaBase(onTocar: (() -> Unit)? = null, contenido: @Composable ColumnScope.() -> Unit) {
    val fondo = MaterialTheme.colorScheme.background

    var modificador = Modifier
        .fillMaxSize()
        .background(Color(0xFF222222))
        .padding(10.dp)
        .clip(RoundedCornerShape(26.dp))
        .background(fondo)

    if (onTocar != null) {
        modificador = modificador.clickable { onTocar() }
    }

    Column(
        modifier = modificador.padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = contenido
    )
}

@Composable
fun BotonSimple(texto: String, onClick: () -> Unit, tamano: Int = 22, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(texto, fontSize = tamano.sp, fontFamily = fuente, textAlign = TextAlign.Center)
    }
}

@Composable
fun TituloPantalla(texto: String) {
    Text(
        text = texto,
        fontSize = 34.sp,
        fontFamily = fuente,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .padding(horizontal = 22.dp, vertical = 4.dp)
    )
}

@Composable
fun BotonSalir(onSalir: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        BotonSimple("SALIR", onSalir)
    }
}

@Composable
fun MenuScreen(temaActual: Int, onJugar: () -> Unit, onScore: () -> Unit, onInstrucciones: () -> Unit, onTema: (Int) -> Unit) {
    PantallaBase {
        Spacer(modifier = Modifier.height(75.dp))

        Text("BLACKJACK", fontFamily = fuente, fontSize = 48.sp, color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(25.dp))

        Image(
            painter = painterResource(R.drawable.cartas),
            contentDescription = "Cartas",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        BotonSimple("JUGAR", onJugar, 32, Modifier.width(185.dp))

        Spacer(modifier = Modifier.height(18.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BotonSimple("SCORE", onScore, 20, Modifier.weight(1f))
            BotonSimple("INSTRUCCIONES", onInstrucciones, 20, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.weight(1f))

        Text("Temas", fontFamily = fuente, fontSize = 20.sp, color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(8.dp))

        val fondos = listOf(Color(0xFF124B25), Color(0xFF071936), Color(0xFF651825))

        Row {
            for (i in 0..2) {
                var borde = Color.Transparent
                if (temaActual == i) borde = Color.White

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(38.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(fondos[i])
                        .border(2.dp, borde, RoundedCornerShape(9.dp))
                        .clickable { onTema(i) }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Composable
fun VentanaInicio(onCerrar: () -> Unit, onIniciar: (String, Int) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("21") }
    var error by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onCerrar,
        text = {
            Column {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    placeholder = { Text("Nombre") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = numero,
                    onValueChange = { numero = it },
                    placeholder = { Text("Número") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                if (error) {
                    Text("Escribe un nombre y un número entre 2 y 99", color = Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val objetivo = numero.toIntOrNull()
                if (nombre.isNotBlank() && objetivo != null && objetivo in 2..99) {
                    onIniciar(nombre.trim(), objetivo)
                } else {
                    error = true
                }
            }) {
                Text("INICIAR")
            }
        }
    )
}

@Composable
fun InstruccionesScreen(onSalir: () -> Unit) {
    val instrucciones = listOf(
        "1.- El objetivo es acercarse al número elegido sin pasarse.",
        "2.- Las cartas del 2 al 10 valen su número. J, Q y K valen 10. El As vale 1 u 11.",
        "3.- El jugador y la CPU reciben dos cartas. Una carta de la CPU estará oculta.",
        "4.- Presiona PEDIR para recibir una carta o PLANTARSE para terminar tu turno.",
        "5.- Si te pasas del objetivo, pierdes. Si no, gana quien tenga más puntos sin pasarse.",
        "6.- Si los dos tienen los mismos puntos, es un empate."
    )

    PantallaBase {
        BotonSalir(onSalir)
        Spacer(modifier = Modifier.height(5.dp))
        TituloPantalla("INSTRUCCIONES")
        Spacer(modifier = Modifier.height(25.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (instruccion in instrucciones) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .border(2.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(22.dp))
                        .padding(14.dp)
                ) {
                    Text(instruccion, fontSize = 21.sp, fontFamily = fuente, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
}

@Composable
fun ScoreScreen(scores: List<PartidaScore>, onSalir: () -> Unit) {
    PantallaBase {
        BotonSalir(onSalir)
        Spacer(modifier = Modifier.height(5.dp))
        TituloPantalla("SCORE")
        Spacer(modifier = Modifier.height(25.dp))

        if (scores.isEmpty()) {
            Text("Todavía no hay partidas guardadas", fontFamily = fuente, fontSize = 22.sp, color = MaterialTheme.colorScheme.onBackground)
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(scores) { score ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                        .border(2.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(23.dp))
                        .padding(12.dp)
                ) {
                    Text("GANADOR: ${score.ganador}", fontFamily = fuente, fontSize = 23.sp, color = Color(0xFF24FF00))
                    Text("PERDEDOR: ${score.perdedor}", fontFamily = fuente, fontSize = 23.sp, color = Color.Red)
                    Text("FECHA: ${score.fecha}", fontFamily = fuente, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)
                    Text("OBJETIVO: ${score.objetivo}", fontFamily = fuente, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("CARTAS GANADORAS:", fontFamily = fuente, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(5.dp))

                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        for (carta in score.cartas) {
                            Text(
                                text = "${carta.numero}${carta.palo}",
                                fontSize = 26.sp,
                                fontFamily = fuente,
                                color = if (carta.palo == "♥" || carta.palo == "♦") Color.Red else MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JuegoScreen(vm: BJViewModel, onSalir: () -> Unit) {
    PantallaBase(
        onTocar = if (vm.resultado != "") ({ vm.nuevaRonda() }) else null
    ) {
        BotonSalir(onSalir)
        Spacer(modifier = Modifier.height(10.dp))

        Text("CPU", fontFamily = fuente, fontSize = 25.sp, color = MaterialTheme.colorScheme.onBackground)

        var valorCpu = "?"
        if (vm.resultado != "") valorCpu = vm.puntosCpu.toString()

        Text("Valor: $valorCpu", fontFamily = fuente, fontSize = 23.sp, color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            for (i in vm.cartasCpu.indices) {
                if (i == 0 && vm.resultado == "") {
                    CartaGrafica(null)
                } else {
                    CartaGrafica(vm.cartasCpu[i])
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (vm.resultado != "") {
            Text(vm.resultado, fontFamily = fuente, fontSize = 38.sp, color = Color.Red, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        Text("Objetivo: ${vm.objetivo}", fontFamily = fuente, fontSize = 22.sp, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(5.dp))
        Text(vm.nombre, fontFamily = fuente, fontSize = 26.sp, color = MaterialTheme.colorScheme.onBackground)
        Text("Puntos: ${vm.puntosJugador}", fontFamily = fuente, fontSize = 24.sp, color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            for (carta in vm.cartasJugador) {
                CartaGrafica(carta)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (vm.resultado == "") {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                BotonSimple("PEDIR", { vm.pedir() })
                BotonSimple("PLANTARSE", { vm.plantarse() })
            }
        } else {
            Text(
                "Toca la pantalla para jugar otra vez",
                fontFamily = fuente,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(25.dp))
    }
}

@Composable
fun CartaGrafica(carta: Carta?, ancho: Dp = 65.dp, alto: Dp = 92.dp) {
    val forma = RoundedCornerShape(7.dp)

    if (carta == null) {
        Box(
            modifier = Modifier
                .size(width = ancho, height = alto)
                .clip(forma)
                .background(MaterialTheme.colorScheme.tertiary)
                .border(2.dp, Color.White, forma),
            contentAlignment = Alignment.Center
        ) {
            Text("♠", fontSize = 35.sp, color = Color.White)
        }
    } else {
        var colorCarta = Color.Black
        if (carta.palo == "♥" || carta.palo == "♦") colorCarta = Color.Red

        var tamanoNumero = 17.sp
        if (ancho < 55.dp) tamanoNumero = 12.sp

        var tamanoPalo = 34.sp
        if (ancho < 55.dp) tamanoPalo = 22.sp

        Box(
            modifier = Modifier
                .size(width = ancho, height = alto)
                .clip(forma)
                .background(Color.White)
                .border(2.dp, Color.Gray, forma)
                .padding(4.dp)
        ) {
            Row(
                modifier = Modifier.align(Alignment.TopStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(carta.numero, fontSize = tamanoNumero, color = colorCarta)
                Text(carta.palo, fontSize = tamanoNumero, color = colorCarta)
            }

            Text(
                carta.palo,
                fontSize = tamanoPalo,
                color = colorCarta,
                modifier = Modifier.align(Alignment.Center)
            )

            Row(
                modifier = Modifier.align(Alignment.BottomEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(carta.palo, fontSize = tamanoNumero, color = colorCarta)
                Text(carta.numero, fontSize = tamanoNumero, color = colorCarta)
            }
        }
    }
}
