package com.github.satoshun.coroutine.autodispose.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.Job

@ExperimentalApi(message = "There is a possibility that it will change")
fun LifecycleOwner.addJob(job: Job) {
  lifecycle.addJob(job)
}

@ExperimentalApi(message = "There is a possibility that it will change")
fun Lifecycle.addJob(job: Job) {
  val state = this.currentState
  val event = when (state) {
    Lifecycle.State.DESTROYED -> Lifecycle.Event.ON_DESTROY
    Lifecycle.State.INITIALIZED -> Lifecycle.Event.ON_DESTROY
    Lifecycle.State.CREATED -> Lifecycle.Event.ON_DESTROY
    Lifecycle.State.STARTED -> Lifecycle.Event.ON_STOP
    Lifecycle.State.RESUMED -> Lifecycle.Event.ON_PAUSE
  }
  val observer = AnyObserver(job, event, this)
  this.addObserver(observer)
  job.invokeOnCompletion(observer)
}

private class AnyObserver(
  private val job: Job,
  private val event: Lifecycle.Event,
  private val lifecycle: Lifecycle
) : LifecycleObserver, CompletionHandler {
  @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
  fun onEvent(owner: LifecycleOwner, event: Lifecycle.Event) {
    if (event == this.event) {
      owner.lifecycle.removeObserver(this)
      job.cancel()
    }
  }

  override fun invoke(cause: Throwable?) {
    lifecycle.removeObserver(this)
  }
}
