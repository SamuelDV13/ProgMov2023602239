package practicas.uno

import java.util.Scanner
import kotlin.system.exitProcess
import java.time.LocalDate
import java.time.Period
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main(){

    val sn = Scanner(System.`in`)

    println("Selecciona una opcion: ")
    println("1.- Sumar")
    println("2.- Nombre")
    println("3.- Fecha")
    println("4.- Salir")
    print("Ingresa una opcion: ")
    var respuesta = sn.nextInt()

    if(respuesta == 1){
        Sumar()
    } else if(respuesta == 2){
        Nombre()
    } else if(respuesta == 3) {
        Fecha()
    }else if(respuesta == 4){
            Salir()
    }else{
        print("Opcion no valida")
    }
}

fun Sumar() : Int{

    val sn = Scanner(System.`in`)
    var contador = 1
    var suma = 0

    while(contador <= 3){
        print("Ingresa un numero: ")
        suma += sn.nextInt()
        contador++
    }

    return suma
}

fun Nombre() : String{
    val sn = Scanner(System.`in`)
    print("Ingresa tu nombre")
    var nombre = sn.nextLine()
    return nombre
}

fun Fecha(){
    println("Ingrese su fecha de nacimiento (formato: dd/MM/yyyy):")
    val input = readLine()

    try {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val birthDate = LocalDate.parse(input, formatter)
        val currentDate = LocalDate.now()

        // Calcular período entre fechas
        val period = Period.between(birthDate, currentDate)
        val birthDateTime = birthDate.atStartOfDay()
        val currentDateTime = LocalDateTime.now()
        val duration = Duration.between(birthDateTime, currentDateTime)

        val totalDays = duration.toDays()
        val totalHours = duration.toHours()
        val totalMinutes = duration.toMinutes()
        val totalSeconds = duration.seconds
        val totalWeeks = totalDays / 7
        val totalMonths = period.years * 12 + period.months

        println("\nHas vivido hasta ahora:")
        println("Meses: $totalMonths")
        println("Semanas: $totalWeeks")
        println("Días: $totalDays")
        println("Horas: $totalHours")
        println("Minutos: $totalMinutes")
        println("Segundos: $totalSeconds")

    } catch (e: Exception) {
        println("Formato de fecha inválido. Usa el formato dd/MM/yyyy.")
    }
}

fun Salir(){
    kotlin.system.exitProcess(0)
}