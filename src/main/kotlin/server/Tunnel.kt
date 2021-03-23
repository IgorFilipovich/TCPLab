package server

import util.printErr
import java.io.OutputStream
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.thread
import kotlin.system.exitProcess

data class ClientHandler(val socket: Socket, val name: String)

class Tunnel(selfPort: Int, private val dstAddress: String, private val dstPort: Int) {
    private val server: ServerSocket = ServerSocket(selfPort)

    fun run() {
        while (true) {
            val client = ClientHandler(server.accept(), "Alice")
            val anotherClient = registerConnection()
            runListening(client, anotherClient)
        }
    }

    private fun registerConnection(): ClientHandler {
        return ClientHandler(Socket(dstAddress, dstPort), "Bob")
    }

    private fun runListening(client: ClientHandler, another: ClientHandler) {
        thread { listenClient(client, another) }
        thread { listenClient(another, client) }
    }

    private fun listenClient(from: ClientHandler, to: ClientHandler) {
        val input = Scanner(from.socket.getInputStream())
        val output: OutputStream = to.socket.getOutputStream()
        while (true) {
            try {
                val message = input.nextLine() ?: ""
                println("Message from ${from.name} to ${to.name}:")
                println(message + '\n')
                output.write((message + '\n').toByteArray(Charset.defaultCharset()))
            } catch(e: Exception) {
                printErr("connection failed")
                exitProcess(-1)
            }
        }
    }
}