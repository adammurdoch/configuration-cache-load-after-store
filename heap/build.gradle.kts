tasks.register("heap") {
    val size = (70 * 1024 * 1024).toInt()
    val configTime = ByteArray(size)
    doLast {
        val execTime = ByteArray(size)
    }
}