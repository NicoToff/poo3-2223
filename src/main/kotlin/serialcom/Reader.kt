package serialcom

import com.fazecast.jSerialComm.SerialPort

class Reader(private val comPort : SerialPort) : Thread() {
    val portName: String? = comPort.systemPortName

    override fun run() {
        val isOpen = comPort.openPort()
        if(!isOpen) {
            println("Error while opening port")
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
                println("$portName -> ${String(readBuffer)}")
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        comPort.closePort()
    }
}