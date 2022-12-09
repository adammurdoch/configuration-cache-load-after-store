class LargeThing(sizeMb: Int) {
    val buffer = ByteArray(sizeMb * 1024 * 1024)
}

tasks.register("heap") {
    // Attach a large amount of heap to the `Project` instance
    val configTime = LargeThing(30)
    project.ext.set("large", configTime)
    doLast {
        // Allocate a large amount of heap at execution time
        val execTime = LargeThing(70)
        println("exec time = $execTime")
    }
}