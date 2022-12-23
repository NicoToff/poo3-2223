import com.fazecast.jSerialComm.SerialPort

fun main(args: Array<String>) {
    println("Hello Exam POO 3!")
    var availablePorts = SerialPort.getCommPorts()
    // Open the first available port
    var comPort = availablePorts[0]
    val isOpen = comPort.openPort()
    if(!isOpen) {
        println("Error opening port")
        return
    }
    // Set port parameters
    comPort.setComPortParameters(9600, 8, 1, 0)
    comPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0)
    // Read data
    try {
        while (true) {
            while (comPort.bytesAvailable() == 0)
                Thread.yield()
            // Read data
            val readBuffer = ByteArray(comPort.bytesAvailable())
            // Print data
            val numRead = comPort.readBytes(readBuffer, readBuffer.size.toLong())
            println("Read $numRead bytes.")
            println(String(readBuffer))
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    comPort.closePort()
}