class LargeThing(sizeMb: Int) {
    val buffer = ByteArray(sizeMb * 1024 * 1024)
}

tasks.register("heap") {
    val configTime = LargeThing(30)
    project.ext.set("large", configTime)
    doLast {
        val execTime = LargeThing(70)
        println("exec time = $execTime")
    }
}