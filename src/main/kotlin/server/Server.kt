package server

import HomePage
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintStream
import java.net.ServerSocket
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class Server(private val homePage: HomePage, port: Int = 42042) : Thread() {
    private val serverSocket = ServerSocket(port)

    init {
        start()
    }

    override fun run() {
        println("Server started on port ${serverSocket.localPort}")

        while (true) {
            val socket = serverSocket.accept() // Wait for a connection

            val input = BufferedReader(InputStreamReader(socket.getInputStream()))
            val output = PrintStream(socket.getOutputStream())
            var route = ""

            // Read the whole request, line by line. The end is marked by an empty line.
            while (true) {
                val line = input.readLine() ?: break

                // Check if the request is GET or POST
                if (line.startsWith("GET") || line.startsWith("POST")) {
                    route = line.split(" ")[1] // example line: GET / HTTP/1.1
                }

                // Once the line is blank, the HTTP request is over,
                // so we can stop reading and start sending a response.
                if (line.isBlank()) {
                    when (route) {
                        "/" -> {
                            val html = Files.readString(Paths.get("src", "main", "resources", "index.html"))
                            output.println("HTTP/1.1 200 OK")
                            output.println("Content-Type: text/html")
                            output.println()
                            output.println(html)
                        }

                        "/initial-data" -> {
                            val badChars = Regex("[}{><)(\\]\\[\"'`|&/\\\\:~=!.;]")
                            val operator = homePage.getOperator().replace(badChars, "")
                            val comment = homePage.getComment().replace(badChars, "")
                            println("Sending initial data to client: $operator, $comment")
                            output.println("HTTP/1.1 200 OK")
                            output.println("Content-Type: application/json")
                            output.println()
                            output.println(
                                """
                                {
                                    "operator": "$operator",
                                    "comment": "$comment"
                                }
                            """
                            )
                        }

                        "/data" -> {
                            var data = "{}" // Empty JSON
                            if (homePage.reader != null) {
                                // Clone de l'historique pour éviter les ConcurrentModificationException
                                val treeMapClone = TreeMap(homePage.reader!!.history)
                                if (treeMapClone.isNotEmpty()) {
                                    // Make JSON
                                    data = "{\n"
                                    for ((key, value) in treeMapClone) {
                                        data += "\t\"${key}\": ${value},\n"
                                    }
                                    data = data.substring(0, data.length - 2) // Remove the last comma (and newline)
                                    data += "\n}"
                                }
                            }
                            output.println("HTTP/1.1 200 OK")
                            output.println("Content-Type: application/json")
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
                            output.println("404 - Page Not Found")
                        }
                    }
                    route = ""
                    break
                }
            }
            socket.close()
        }
    }
}
