# Coroutine AutoDispose Example (in progress)

Goal: Job work with Android lifecycle.

## first approach: use LifecycleObserver

```kotlin
fun LifecycleOwner.addJob(job: Job) {
  val state = lifecycle.currentState
  when (state) {
    Lifecycle.State.DESTROYED -> TODO()
    Lifecycle.State.INITIALIZED -> lifecycle.addObserver(AnyObserver(job, Lifecycle.Event.ON_DESTROY))
    Lifecycle.State.CREATED -> lifecycle.addObserver(AnyObserver(job, Lifecycle.Event.ON_DESTROY))
    Lifecycle.State.STARTED -> lifecycle.addObserver(AnyObserver(job, Lifecycle.Event.ON_STOP))
    Lifecycle.State.RESUMED -> lifecycle.addObserver(AnyObserver(job, Lifecycle.Event.ON_PAUSE))
  }
}

private class AnyObserver(
  private val job: Job,
  private val event: Lifecycle.Event
) : LifecycleObserver {
  @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
  fun onEvent(owner: LifecycleOwner, event: Lifecycle.Event) {
    if (event == this.event) {
      owner.lifecycle.removeObserver(this)
      job.cancel()
    }
  }
}
```
