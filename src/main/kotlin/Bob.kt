import server.Server

fun main(args: Array<String>) {
    val port: Int
    var encryption = false
    when (args.size) {
        1 -> {
            port = args[0].toIntOrNull() ?: throw IllegalArgumentException("Int required as second argument")
        }
        2 -> {
            port = args[0].toIntOrNull() ?: throw IllegalArgumentException("Int required as second argument")
            encryption = args[1] == "true"
        }
        else -> {
            port = 8080
        }
    }
    val server = Server(port, encryption)
    server.run()
}