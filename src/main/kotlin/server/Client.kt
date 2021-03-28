package server

import kotlinx.coroutines.*
import security.DiffieHellmanEncryptor
import util.printErr
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class Client(client: Socket, private val encryptionMode: Boolean = false) {
    private val connection: Socket = client
    private val reader: Scanner = Scanner(connection.getInputStream())
    private val writer: OutputStream = connection.getOutputStream()
    private val encryptor: DiffieHellmanEncryptor = DiffieHellmanEncryptor()

    init {
        printErr("connection on ${connection.localAddress} address and ${connection.port} port")
    }

    private fun isAvailable(): Boolean {
        return connection.isConnected && !connection.isClosed
    }

    private fun setReceiverPublicKey(key: String) {
        encryptor.setReceiverPublicKey(key)
    }

    fun run() {
        if (encryptionMode) {
            val pubKey: ByteArray = (encryptor.getPublicKeyString() + '\n').toByteArray(Charset.defaultCharset())
            writer.write(pubKey)
            runBlocking {
                val job = GlobalScope.launch(Dispatchers.Default) {
                    delay(1000L)
                    printErr("connection failed")
                    exitProcess(-1)
                }
                val message = read()
                job.cancel()
                setReceiverPublicKey(message)
            }
        }
        thread {
            while (isAvailable()) {
                var message = read()
                if (encryptionMode) {
                    message = encryptor.decrypt(message)
                }
                println(message)
            }
        }
        while (isAvailable()) {
            var input = readLine() ?: ""
            if (encryptionMode) {
                input = encryptor.encrypt(input)
            }
            write(input)
        }
    }

    private fun read(): String {
        if (!reader.hasNextLine()) {
            connection.close()
            printErr("connection closed")
            return ""
        }
        return reader.nextLine()
    }

    private fun write(input: String) {
        if (!connection.isClosed) {
            writer.write((input + '\n').toByteArray(Charset.defaultCharset()))
        }
    }
}