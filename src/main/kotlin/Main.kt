import com.fazecast.jSerialComm.SerialPort

fun main() {
    val availablePorts: Array<SerialPort> = SerialPort.getCommPorts()
    HomePage(availablePorts)


//    val readers: ArrayList<Reader> = ArrayList()

//    for (port in availablePorts) {
//        println("Found port: ${port.systemPortName}")
//        readers.add(Reader(port))
//    }


    // Start the threads
//    for (reader in readers) {
//        reader.start()
//    }
//    var lastReadingDate = Date()
//    while (true) {
//        if (Date().time - lastReadingDate.time >= 1000) {
//            lastReadingDate = Date()
//            for (reader in readers) {
//                if (reader.history.isNotEmpty()) {
//                    println("  (Main) ${reader.comPortName}: ${reader.history.entries.last().value}")
//                }
//            }
//        } else {
//            Thread.yield()
//        }
//    }
}
