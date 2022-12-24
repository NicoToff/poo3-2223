import com.fazecast.jSerialComm.SerialPort
import serialcom.Reader

fun main(args: Array<String>) {
    println("Hello Exam POO 3!")
    var availablePorts = SerialPort.getCommPorts()
    // Open the first available port
    var comPort = availablePorts[0]
    val r = Reader(comPort)
    r.start()
    while (true) {
        Thread.sleep(1000)
        if (r.history.isNotEmpty()) {
            println("From Main: ${r.lastElem}")
        }
    }
}