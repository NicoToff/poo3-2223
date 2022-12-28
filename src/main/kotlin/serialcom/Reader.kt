package serialcom

import HomePage
import com.fazecast.jSerialComm.SerialPort
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

class Reader(private val homePage: HomePage, private val comPort: SerialPort) : Thread() {
    val history: TreeMap<LocalDateTime, Double> = TreeMap()

    init {
        portConnection(comPort)
    }

    override fun run() {
        try {
            readData() // Clears the buffer
            while (!this.isInterrupted) {
                try {
                    while (comPort.bytesAvailable() == 0) {
                        Thread.yield()
                    }
                    val readBuffer = readData() // Read data
                    // Store the data every second at most
                    val now = LocalDateTime.now()
                    var timeDiff = 1000L // Base number top trigger the first iteration
                    if (history.isNotEmpty()) timeDiff = Duration.between(history.lastKey(), now).toMillis()
                    if (timeDiff >= 1000) {
                        val number: Double = parseData(readBuffer) // Parse the data
                        history[now] = number // Store the data
                        // Update the UI
                        homePage.lblLastValue.text = number.toString()
                        homePage.lblSampleNumber.text = history.size.toString()
                    }
                } catch (ex: NegativeArraySizeException) {
                    // Disconnecting the USB port throws this exception
                    ex.printStackTrace()
                    println("Reconnecting...")
                    portConnection(this.comPort) // We need to try and reconnect
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            comPort.closePort()
        }
    }

    /**
     * Handles the connection to the serial port.
     * @param comPort The serial port to connect to.
     */
    private fun portConnection(comPort: SerialPort) {
        comPort.closePort() // Reset the port if it was opened before
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

    /** Reads the data from the serial port and returns it as a `ByteArray`.
     *
     *  By reading the data, the serial port buffer is emptied,
     *  therefore this function can be called to only flush the buffer too.
     *
     *  @return A `ByteArray` with the data from port buffer. */
    private fun readData(): ByteArray {
        val byteArray = ByteArray(comPort.bytesAvailable())
        comPort.readBytes(byteArray, byteArray.size.toLong())
        return byteArray
    }

    /** Parses the data from a `ByteArray` and returns it as a `Double`.
     * @param byteArray The ByteArray to parse.
     * @return A `Double` */
    private fun parseData(byteArray: ByteArray): Double {
        try {
            val data = String(byteArray)
            return data.toDouble()
        } catch (nfex: NumberFormatException) {
            // String parsing into a double can fail if the buffer data isn't a number
            // This is not a problem, we can just ignore it and go to the next iteration
            nfex.printStackTrace()
            return 0.0
        }
    }
}
