package serialcom

import com.fazecast.jSerialComm.SerialPort
import java.util.*

class Reader(private val comPort: SerialPort) : Thread() {
    val history: HashMap<Date, Double> = HashMap(256, 0.75f)
    var lastElem: Double = 0.0
    private var lastStorageDate: Date = Date()
    override fun run() {
        var isOpen = false;
        while (!isOpen) {
            isOpen = comPort.openPort()
        }
        // Set port parameters
        comPort.setComPortParameters(9600, 8, 1, 0)
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0)
        // Read data
        try {
            while (true) {
                try {
                    while (comPort.bytesAvailable() == 0) {
                        Thread.yield()
                    }
                    // Read data
                    val readBuffer = ByteArray(comPort.bytesAvailable())
                    comPort.readBytes(readBuffer, readBuffer.size.toLong())
                    // Parse the data
                    val data = String(readBuffer)
                    val number: Double = data.toDouble()
                    // Store the data every second at most
                    val now = Date()
                    if (now.time - lastStorageDate.time >= 1000) {
                        history[now] = number
                        lastElem = number
                        lastStorageDate = now
                        println("Reader: $number")
                    }
                } catch (nfex: NumberFormatException) {
                    // This is sometimes thrown when the String parsing into a double fails
                    nfex.printStackTrace()
                    println("Moving on...")
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            comPort.closePort()
        }
    }
}