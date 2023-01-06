import com.fazecast.jSerialComm.SerialPort
import java.awt.Desktop
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintStream
import java.net.ServerSocket
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val availablePorts: Array<SerialPort> = SerialPort.getCommPorts()
    val homePage = HomePage(availablePorts)

    val serverSocket = ServerSocket(42042)
    println("Listening for connections on port 42042...")
    openWebpage("http://localhost:42042")
    while (true) {
        val socket = serverSocket.accept()
        println("New client connected")

        val input = BufferedReader(InputStreamReader(socket.getInputStream()))
        val output = PrintStream(socket.getOutputStream())
        var route = ""
        while (true) {
            val line = input.readLine() ?: break
            println(line)

            if (line.startsWith("GET")) {
                route = line.split(" ")[1]
            }

            if (line.isBlank()) {
                when (route) {
                    "/" -> {
                        val html = Files.readString(Paths.get("src", "main", "resources", "index.html"))
                        output.println("HTTP/1.1 200 OK")
                        output.println("Content-Type: text/html")
                        output.println()
                        output.println(html)
                    }

                    "/data" -> {
                        var data = "{}"
                        if (homePage.reader != null && homePage.reader!!.history.isNotEmpty()) {
                            // Make JSON
                            data = "{\n"
                            for ((key, value) in homePage.reader!!.history) {
                                data += "\t\"${key}\": ${value},\n"
                            }
                            data = data.substring(0, data.length - 2); // Remove the last comma
                            data += "\n}"
                        }

                        output.println("HTTP/1.1 200 OK")
                        output.println("Content-Type: text/plain")
                        output.println()
                        output.println(data)
                    }

                    "/favicon.ico" -> {
                        val favicon = Files.readAllBytes(Paths.get("src", "main", "resources", "favicon.ico"))
                        output.println("HTTP/1.1 200 OK")
                        output.println("Content-Type: image/x-icon")
                        output.println("Content-Length: ${favicon.size}")
                        output.println()
                        output.write(favicon)
                    }

                    else -> {
                        output.println("HTTP/1.1 404 Not Found")
                        output.println("Content-Type: text/plain")
                        output.println()
                        output.println("Not Found")
                    }
                }
                break
            }
        }

        socket.close()
    }
}

fun openWebpage(url: String) {
    val desktop = Desktop.getDesktop()
    desktop.browse(URI(url))
}