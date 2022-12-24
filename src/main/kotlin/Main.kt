import com.fazecast.jSerialComm.SerialPort
import serialcom.Reader

fun main(args: Array<String>) {
    println("Hello Exam POO 3!")
    var availablePorts = SerialPort.getCommPorts()
    // Open the first available port
    var comPort = availablePorts[0]
    val r = Reader(comPort)
    r.start()
}