import com.fazecast.jSerialComm.SerialPort
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintStream
import java.net.ServerSocket
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

fun main() {
    val availablePorts: Array<SerialPort> = SerialPort.getCommPorts()
    val homePage = HomePage(availablePorts)

    val serverSocket = ServerSocket(42042)
    println("Listening for connections on port 42042...")

    while (true) {
        val socket = serverSocket.accept() // Wait for a connection

        val input = BufferedReader(InputStreamReader(socket.getInputStream()))
        val output = PrintStream(socket.getOutputStream())
        var route = ""

        // Read the request line by line until the end of the request (empty line)
        while (true) {
            val line = input.readLine() ?: break

            // Check if the request is GET or POST
            if (line.startsWith("GET") || line.startsWith("POST")) {
                route = line.split(" ")[1] // example line: GET / HTTP/1.1
            }

            // Once the line is blank, the HTTP request is over
            // We can stop reading the request and send a response
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
                        if (homePage.reader != null) {
                            // Copie de l'historique pour éviter les ConcurrentModificationException
                            val treeMapCopy = TreeMap(homePage.reader!!.history)
                            if (treeMapCopy.isNotEmpty()) {
                                // Make JSON
                                data = "{\n"
                                for ((key, value) in treeMapCopy) {
                                    data += "\t\"${key}\": ${value},\n"
                                }
                                data = data.substring(0, data.length - 2); // Remove the last comma (and newline)
                                data += "\n}"
                            }
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
                route = ""
                break
            }
        }

        socket.close()
    }
}

