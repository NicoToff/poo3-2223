import com.fazecast.jSerialComm.SerialPort
import server.Server

fun main() {
    val availablePorts: Array<SerialPort> = SerialPort.getCommPorts()
    val homePage = HomePage(availablePorts)
    Server(homePage)
}