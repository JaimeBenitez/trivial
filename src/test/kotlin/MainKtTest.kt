import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.FileNotFoundException

internal class MainKtTest {
    val fichero = "src/main/resources/preguntas.trivial"
    val fichero2 = "src/main/resources/preguntas.trivia"

    //Test que comprueba si el archivo existe o no
    @Test
    fun testTxtADiccionarios() {
        assertNotNull(txtADiccionarios(fichero))
        assertThrows(FileNotFoundException::class.java) {
            txtADiccionarios(fichero2)
        }

    }
    //Test que comprueba si la tabla se ha creado correctamente en la base de datos
    @Test
    fun testBaseDatos() {
        Database.connect("jdbc:h2:./src/main/kotlin/database.db", driver = "org.h2.Driver")
        transaction {
            addLogger(StdOutSqlLogger)
            var verTabla = SchemaUtils.checkCycle(ResultadosTrivial)
            assertNotNull(verTabla)
        }
    }
}

