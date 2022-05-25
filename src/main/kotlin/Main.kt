import ResultadosTrivial.numeroAciertos
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun main() {
    val fichero = "src/main/resources/preguntas.trivial"
    val csv = File("src/main/kotlin/datos.csv")
    val fileWriter = FileWriter(csv) //Esta y las siguientes 3 lineas son las que nos permiten escribir en el csv
    val bufferedWriter = BufferedWriter(fileWriter)
    val datos = txtADiccionarios(fichero)
    var nombre: String?
    println("Introduce nombre de jugador")
    nombre = readLine()
    val aciertos = juego(nombre,datos)
    //Las 6 lineas siguientes nos permiten sacar la fecha y hora en un formato legible
    val formato = SimpleDateFormat("dd.MM.yyyy")
    val fechaActual  = Date()
    val fechaFormateada = formato.format(fechaActual)
    var horaActual = LocalDateTime.now()
    var formatoHora = DateTimeFormatter.ofPattern("HH:mm")
    var horaFormateada = horaActual.format(formatoHora)
    //Realizamos la conexión con la base de datos
    Database.connect("jdbc:h2:./src/main/kotlin/database.db", driver = "org.h2.Driver")
    transaction {
        addLogger(StdOutSqlLogger)
        //Creamos la tabla
        SchemaUtils.create (ResultadosTrivial)
        //Insertamos los datos que nos interesan en la base de datos
        ResultadosTrivial.insert{
            it[fechaYHora] = "$fechaFormateada $horaFormateada"
            it[nombreJugador] = "$nombre"
            it[numeroAciertos] = aciertos
        } get ResultadosTrivial.id


        ResultadosTrivial.selectAll().orderBy(numeroAciertos).forEach {
            // Imprimimos los valores de la tabla que hemos creado
            println("${it[ResultadosTrivial.fechaYHora]},${it[ResultadosTrivial.nombreJugador]},${it[numeroAciertos]}")
            //Los pasamos al CSV
            bufferedWriter.write("${it[ResultadosTrivial.fechaYHora]},${it[ResultadosTrivial.nombreJugador]},${it[numeroAciertos]}\n")
        }

    }

 bufferedWriter.close() //Cerramos la escritura del CSV
}
//Aqui creamos la tabla
object ResultadosTrivial: IntIdTable(){
    val fechaYHora = varchar("Fecha y hora",60)
    val nombreJugador = varchar("Nombre",30)
    val numeroAciertos = integer("Preguntas Acertadas")
}
//Función que convierte el contenido de un txt en una lista de diccionarios
fun txtADiccionarios(archivo:String):MutableList<Map<String,String>>{
    val file = File(archivo).readLines()
    var listaDiccionarios = mutableListOf<Map<String,String>>()
    var keys = mutableListOf<String>("Pregunta","Correcta","A","B","C","D")
    var diccionario = mutableMapOf<String,String>()
    var contador = 0
    for(line in file){
        contador += 1 //Este contador nos permite saber en que linea estamos
        when(contador){
            1 -> diccionario.put(keys[0],line)
            2 -> diccionario.put(keys[1],line)
            3 -> diccionario.put(keys[2],line)
            4 -> diccionario.put(keys[3],line)
            5 -> diccionario.put(keys[4],line)
            6 -> diccionario.put(keys[5],line)
        }
        if(contador == 6){ //Cada 6 lineas añadimos el diccionario a la lista y vaciamos la variable y el contador
            listaDiccionarios.add(diccionario)
            contador = 0
            diccionario = mutableMapOf<String,String>()
        }
    }
    return listaDiccionarios
}
//Funcion que selecciona preguntas al azar
fun seleccionarPregunta(datos:MutableList<Map<String,String>>): Map<String,String>{
    return datos.random()
}
/*Función que contiene el proceso de juego. Crea 10 preguntas al azar y da la bienvenida al jugador.
Le pide una respuesta a cada pregunta y le comunica su resultado, al final devuelve el numero de respuestas
acertadas
 */
fun juego(jugador:String?, datos:MutableList<Map<String,String>>):Int{

    var preguntaEscogida = mutableListOf<Map<String,String>>()
    var eleccionJugador: String?
    var preguntaUno = seleccionarPregunta(datos)
    var preguntasAcertadas = 0
    preguntaEscogida.add(preguntaUno)
    // A partir de aqui comprobamos que las preguntas no sean repetidas
    var preguntaDos = seleccionarPregunta(datos)
    do {
        preguntaDos = seleccionarPregunta(datos)
    }while(preguntaDos in preguntaEscogida)
    preguntaEscogida.add(preguntaDos)
    var preguntaTres = seleccionarPregunta(datos)
    do {
        preguntaTres = seleccionarPregunta(datos)
    }while(preguntaTres in preguntaEscogida)
    preguntaEscogida.add(preguntaTres)
    var preguntaCuatro = seleccionarPregunta(datos)
    do {
        preguntaCuatro = seleccionarPregunta(datos)
    }while(preguntaCuatro in preguntaEscogida)
    preguntaEscogida.add(preguntaCuatro)
    var preguntaCinco = seleccionarPregunta(datos)
    do {
        preguntaCinco = seleccionarPregunta(datos)
    }while(preguntaCinco in preguntaEscogida)
    preguntaEscogida.add(preguntaCinco)
    var preguntaSeis = seleccionarPregunta(datos)
    do {
        preguntaSeis = seleccionarPregunta(datos)
    }while(preguntaSeis in preguntaEscogida)
    preguntaEscogida.add(preguntaSeis)
    var preguntaSiete = seleccionarPregunta(datos)
    do {
        preguntaSiete = seleccionarPregunta(datos)
    }while(preguntaSiete in preguntaEscogida)
    preguntaEscogida.add(preguntaSiete)
    var preguntaOcho = seleccionarPregunta(datos)
    do {
        preguntaOcho = seleccionarPregunta(datos)
    }while(preguntaOcho in preguntaEscogida)
    preguntaEscogida.add(preguntaOcho)
    var preguntaNueve = seleccionarPregunta(datos)
    do {
        preguntaNueve = seleccionarPregunta(datos)
    }while(preguntaNueve in preguntaEscogida)
    preguntaEscogida.add(preguntaNueve)
    var preguntaDiez = seleccionarPregunta(datos)
    do {
        preguntaDiez = seleccionarPregunta(datos)
    }while(preguntaDiez in preguntaEscogida)
    preguntaEscogida.add(preguntaDiez)

    println("Bienvenido $jugador")
    //Bucle de juego principal
    for(pregunta in preguntaEscogida) {
        println(pregunta["Pregunta"])
        println("A: ${pregunta["A"]},B: ${pregunta["B"]},C: ${pregunta["C"]},D: ${pregunta["D"]}")
        do {
            println("Elige tu respuesta")
            eleccionJugador = readLine().toString()
        }while(eleccionJugador != "A" && eleccionJugador != "B" && eleccionJugador != "C" && eleccionJugador != "D")
        if (eleccionJugador == pregunta["Correcta"]) {
            println("¡¡Acertaste!!")
            preguntasAcertadas += 1
        } else {
            println("Derp, fallaste")
        }
    }
    println("Acertaste un total de $preguntasAcertadas")
    return preguntasAcertadas
    }





