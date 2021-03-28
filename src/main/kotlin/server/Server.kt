package server

import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class Server(port: Int, private val encryption: Boolean = false) {
    private val server: ServerSocket = ServerSocket(port)

    fun run() {
        while (true) {
            val client: Socket = server.accept()
            thread { Client(client, encryption).run() }
        }
    }
}