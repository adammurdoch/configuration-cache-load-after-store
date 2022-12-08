# Load work graph from configuration cache after storing

This repository contains some samples to try out the 'load-after-store' configuration cache feature.

To try this out, you can use the [`run`](run/build.gradle.kts) task:

```
> ./gradlew run
> ./gradlew run
```

The logging output should show that for a cache miss build, different instances of the task are configured and run.
If you run the same task with Gradle 7.6, you will see that the same instance of the task is configured and run:

```
> alias gradle76=... # point this to a Gradle 7.6 installation (or any recent 7.x version)
> gradle76 run
> gradle76 run
```

## Parallel task execution for cache miss build

To try this out, use the [`slow`](parallel/build.gradle.kts) task:

```
> ./gradlew slow
> ./gradlew slow
```

The logging output should show that for both cache miss and cache hit builds, several tasks from the same project run in parallel.
If you run the same task with Gradle 7.6, you will see that these tasks do not run in parallel for a cache miss build:

```
> gradle76 slow
> gradle76 slow
```

## Report deserialization problems

To try this out, use the [`broken`](broken/build.gradle) task:

```
> ./gradlew broken
> ./gradlew broken
```

The build should fail for both cache miss and cache hit builds with a configuration cache problem in the Groovy closure.
If you run the same task with Gradle 7.6, you will see that the cache miss build succeeds and the problem is only 
reported for a cache hit build.

```
> gradle76 broken
> gradle76 broken
```

## Reduced heap usage

To try this out, use the [`heap`](heap/build.gradle.kts) task:

```
> ./gradlew --stop
> ./gradlew heap
> ./gradlew heap
```

The build should succeed for both cache miss and cache hit builds. It should fail on a cache miss build with Gradle 7.6.

```
> gradle76 --stop
> gradle76 heap
> gradle76 heap
```
