
tasks.register("run") {
    println("Configuring $this (${System.identityHashCode(this)})")
    doLast {
        println("Running $this (${System.identityHashCode(this)})")
    }
}
