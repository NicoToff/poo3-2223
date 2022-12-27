package serialcom

import HomePage
import com.fazecast.jSerialComm.SerialPort

class PortSearch(private val homePage: HomePage) : Thread() {
    override fun run() {
        while (true) {
            if (homePage.availablePorts.isEmpty()) {
                println("Looking for ports...")
                homePage.availablePorts = SerialPort.getCommPorts()
                if (homePage.availablePorts.isNotEmpty()) {
                    println("Found port: ${homePage.availablePorts[0].systemPortName}")
                    println(homePage.availablePorts)
                    for (port in homePage.availablePorts) {
                        homePage.cmbPort.addItem(port.systemPortName)
                        homePage.cmbPort.selectedItem = port.systemPortName
                    }
                }
                Thread.sleep(100)
            }
        }
    }
}
