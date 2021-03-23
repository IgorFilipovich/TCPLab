import server.Tunnel

fun main(args: Array<String>) {
    var selfPort = 8081
    var dstAddress = "127.0.0.1"
    var dstPort = 8080
    when (args.size) {
        1 -> {
            selfPort = args[0].toIntOrNull() ?: throw IllegalArgumentException("Int required as second argument")
        }
        2 -> {
            selfPort = args[0].toIntOrNull() ?: throw IllegalArgumentException("Int required as second argument")
            dstAddress = args[1]
        }
        3 -> {
            selfPort = args[0].toIntOrNull() ?: throw IllegalArgumentException("Int required as second argument")
            dstAddress = args[1]
            dstPort = args[2].toIntOrNull() ?: throw IllegalArgumentException("Int required as second argument")
        }
    }
    val tunnel = Tunnel(selfPort, dstAddress, dstPort)
    tunnel.run()
}