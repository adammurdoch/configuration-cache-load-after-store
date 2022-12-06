val slowTasks = (1..4).map { i ->
    tasks.register("slow${i}") {
        doLast {
            Thread.sleep(2000)
        }
    }
}

tasks.register("slow") {
    dependsOn(slowTasks)
}
