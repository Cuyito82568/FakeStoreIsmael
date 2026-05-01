package com.example.lib

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(){
    println("Corrutinas")
    // 1. Corrutina!!!
    // LAS CORRUTINAS NECESITAN UN ESPACIO DONDE EJCUTARSE, ESE ESPACIO SE LLAMA SCOPE / CONTEXTO
    // 1. runBlocking XXXXXXXXXX NO SE DEBE USAR EN PRODCCION
//    runBlocking {
//       println("Corriendo dentro de corrutina")
//       delay(4000)
//       println("FIn de ejecucion")
//    }
    cLaunch()
    println("Proceso terminado")
}

fun cLaunch(){
    runBlocking {
        println("Cargando la interfaz grafica...")
        launch(Dispatchers.IO) {
          println("Haciendo consulta a una API")
          delay(6000)
          println("El resultado de la consulta a la API fue exitoso")
        }
        println("Interfaz sigue cargando")
    }
}

fun cAsync(){
    runBlocking {
        println("Cargando la interfaz grafica...")
        val result = async(Dispatchers.IO) {
            println("Haciendo consulta a DB")
            delay(6000)
            "Hola"
        }
        val asyncResult = result.await()
        println("El resultado fue $asyncResult")
        println("Interfaz sigue cargando")
    }
}

suspend fun MiCorrutina(){
    delay(5000)
}