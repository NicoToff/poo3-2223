package serialcom

import com.fazecast.jSerialComm.SerialPort
import java.util.*

class Reader(private val comPort: SerialPort) : Thread() {
    val history: Hashtable<Date, Double> = Hashtable(256, 0.75f)
    var lastElem: Map.Entry<Date, Double>? = null
    private var lastStorageDate: Date = Date()
    private fun connection() {
        comPort.closePort()
        while (!comPort.isOpen) {
            comPort.openPort()
            if (!comPort.isOpen) {
                println("Port ${comPort.systemPortName} is not open, retrying in 0.1 second...")
                Thread.sleep(100)
            }
        }
        println("Port ${comPort.systemPortName} is open!")
        // Set port parameters
        comPort.setComPortParameters(9600, 8, 1, 0)
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0)
    }

    override fun run() {
        connection()
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
                        lastElem = history.entries.last()
                        lastStorageDate = now
                        println("Reader: $number")
                    }
                } catch (nfex: NumberFormatException) {
                    // String parsing into a double sometimes fails
                    nfex.printStackTrace()
                    println("Moving on...")
                } catch (ex: NegativeArraySizeException) {
                    // Disconnecting the USB port throws this exception
                    ex.printStackTrace()
                    println("Reconnecting...")
                    connection()
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            comPort.closePort()
        }
    }
}