import com.fazecast.jSerialComm.SerialPort

fun main() {
    val availablePorts: Array<SerialPort> = SerialPort.getCommPorts()
    HomePage(availablePorts)
}
