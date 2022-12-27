import com.fazecast.jSerialComm.SerialPort
import serialcom.Reader
import java.util.*

fun main(args: Array<String>) {
    println("Hello Exam POO 3!")
    var availablePorts = SerialPort.getCommPorts()
    val readers: ArrayList<Reader> = ArrayList()
    // Open the first available port
    for (port in availablePorts) {
        println("Found port: ${port.systemPortName}")
        readers.add(Reader(port))
    }

    HomePage()

    // Start the threads
    for (reader in readers) {
        reader.start()
    }
    var lastReadingDate = Date()
    while (true) {
        if (Date().time - lastReadingDate.time >= 1000) {
            lastReadingDate = Date()
            for (reader in readers) {
                if (reader.history.isNotEmpty()) {
                    println("  (Main) ${reader.comPortName}: ${reader.history.entries.last().value}")
                }
            }
        } else {
            Thread.yield()
        }
    }
}
