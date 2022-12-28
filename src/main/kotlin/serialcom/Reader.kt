package serialcom

import HomePage
import com.fazecast.jSerialComm.SerialPort
import java.util.*

class Reader(private val homePage: HomePage, private val comPort: SerialPort) : Thread() {
    val history: TreeMap<Date, Double> = TreeMap()

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

    init {
        portConnection(comPort)
    }

    override fun run() {
        try {
            while (!this.isInterrupted) {
                try {
                    while (comPort.bytesAvailable() == 0) {
                        Thread.yield()
                    }
                    // Read data
                    val readBuffer = ByteArray(comPort.bytesAvailable())
                    comPort.readBytes(readBuffer, readBuffer.size.toLong())
                    // Store the data every second at most
                    val now = Date()
                    var timeDiff = 1000L // Base number for the first iteration
                    if (history.isNotEmpty()) timeDiff = now.time - history.keys.last().time
                    if (timeDiff >= 1000) {
                        // Parse the data
                        val data = String(readBuffer)
                        val number: Double = data.toDouble()
                        // Store the data
                        history[now] = number
                        // Update the UI
                        homePage.lblLastValue.text = number.toString()
                        homePage.lblSampleNumber.text = history.size.toString()
                    }
                } catch (nfex: NumberFormatException) {
                    // String parsing into a double sometimes fails
                    // This is not a problem, we can just ignore it and go to the next iteration
                    nfex.printStackTrace()
                    println("Moving on...")
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
}
