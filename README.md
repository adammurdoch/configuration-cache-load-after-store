# Load work graph from configuration cache after storing

This repository contains some samples to try out the changes to load the work graph from cache on a cache miss build.
A "cache miss" build is a build where there is no matching configuration cache entry available, and so Gradle runs the 
configuration phase and stores the work graph in a new cache entry. A "cache hit" build is a build where Gradle
reuses an existing configuration cache entry.

To try out the new behaviour, you can use the [`run`](run/build.gradle.kts) task.
The logging output should show that for a cache miss build, different instances of the task are configured and run.
The instance that runs is loaded from the cache and is isolated from the configuration state (more on this below).

```shell
> ./gradlew run
```

If you run the same task with Gradle 7.6, you will see that the same instance of the task is configured and run:

```shell
> alias gradle76=... # point this to a Gradle 7.6 installation (or any recent 7.x version)
> gradle76 run
```

Note: after running these tasks, if you want to run them again to see the behaviour for cache miss builds, you will 
need to remove the configuration cache to force a cache miss:

```shell
> rm -rf .gradle/configuration-cache
```

There are several benefits provided by the changed behaviour: 

## Parallel task execution for cache miss builds

To try this out, use the [`slow`](parallel/build.gradle.kts) task.
The logging output should show that for cache miss and cache hit builds, several "slow" tasks from the same project run in parallel.

```shell
> ./gradlew slow
> ./gradlew slow
```

If you run the same task with Gradle 7.6, you will see that these tasks do not run in parallel for cache miss builds:

```shell
# tasks do not run in parallel
> gradle76 slow

# tasks do not run in parallel
> gradle76 slow
```

## Deserialization problems are reported for cache miss builds

To try this out, use the [`broken`](broken/build.gradle) task.
The build should fail for cache miss and cache hit builds and report a configuration cache problem in the Groovy closure.

```shell
> ./gradlew broken
> ./gradlew broken
```

If you run the same task with Gradle 7.6, you will see that the cache miss build succeeds and the problem is only 
reported for the cache hit build.

```shell
# succeeds but should fail
> gradle76 broken

# fails
> gradle76 broken
```

## Reduced heap usage

To try this out, use the [`heap`](heap/build.gradle.kts) task. The build should succeed for cache miss and cache hit builds.

Note that this task can be flaky as it relies on GC behaviour. It seems to be most reliable after restarting the daemon.
It was tested using Java 11.

```shell
> ./gradlew --stop
> ./gradlew heap
> ./gradlew heap
```

When this task is run, a large amount of heap is allocated and attached to the `Project` instance at configuration time,
to simulate a large amount of configuration state (plugins, extensions, dependency resolution data, tooling model builders, etc).
The `Project` instances are discarded after storing the work graph and this heap is released.
The task also allocates a large amount of heap when it executes, and this should succeed.

The task should fail with 'out of heap space' on the cache miss build with Gradle 7.6, as it does not discard the configuration state
before executing tasks. The task should succeed for the cache hit build. 

```shell
> gradle76 --stop
> gradle76 heap
> gradle76 heap
```
