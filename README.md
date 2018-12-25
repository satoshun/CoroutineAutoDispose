[![CircleCI](https://circleci.com/gh/satoshun/CoroutineAutoDispose.svg?style=svg)](https://circleci.com/gh/satoshun/CoroutineAutoDispose)

# Coroutine AutoDispose with Android Lifecycle

## How to use it?

register Job manually to Lifecycle

```kotlin
val job = launch { ... }

lifecycle.addJob(job) // register Job
```

or plus LifecycleContinuationInterceptor your CoroutineContext.

```kotlin
abstract class BaseActivity : AppCompatActivity(),
  CoroutineScope {

  private val job = Job()
  override val coroutineContext get() = job + Dispatchers.Main + LifecycleContinuationInterceptor(this)
}

class MainActivity : BaseActivity() {
    fun test() {
        launch { ... } // automatically dispose that corresponds lifecycle state
    }
}
```

## Install

not released yet
