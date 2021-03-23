import server.Client
import java.net.Socket

fun main(args: Array<String>) {
    var address = "localhost"
    var port = 8080
    var encryption = false
    when (args.size) {
        1 -> {
            address = args[0]
        }
        2 -> {
            address = args[0]
            port = args[1].toIntOrNull() ?: throw IllegalArgumentException("Int required as second argument")
        }
        3 -> {
            address = args[0]
            port = args[1].toIntOrNull() ?: throw IllegalArgumentException("Int required as second argument")
            encryption = args[2] == "true"
        }
    }
    val client = Client(Socket(address, port), encryption)
    client.run()
}