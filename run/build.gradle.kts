
tasks.register("run") {
    println("Configuring $this (instance = ${System.identityHashCode(this)})")
    doLast {
        println("Running $this (instance = ${System.identityHashCode(this)})")
    }
}
