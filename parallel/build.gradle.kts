
// Register 4 "slow" tasks
val slowTasks = (1..4).map { i ->
    tasks.register("slow${i}") {
        doLast {
            Thread.sleep(2000)
        }
    }
}

// Register a lifecycle task that depends on the "slow" tasks
tasks.register("slow") {
    dependsOn(slowTasks)
}
