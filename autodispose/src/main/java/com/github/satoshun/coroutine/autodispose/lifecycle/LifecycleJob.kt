package com.github.satoshun.coroutine.autodispose.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.Job

fun LifecycleOwner.autoDispose(job: Job) {
  lifecycle.autoDispose(job)
}

fun Lifecycle.autoDispose(job: Job) {
  val state = this.currentState
  val event = when (state) {
    Lifecycle.State.DESTROYED -> Lifecycle.Event.ON_DESTROY
    Lifecycle.State.INITIALIZED -> Lifecycle.Event.ON_DESTROY
    Lifecycle.State.CREATED -> Lifecycle.Event.ON_DESTROY
    Lifecycle.State.STARTED -> Lifecycle.Event.ON_STOP
    Lifecycle.State.RESUMED -> Lifecycle.Event.ON_PAUSE
  }
  val observer = LifecycleJobObserver(job, event, this)
  this.addObserver(observer)
  job.invokeOnCompletion(observer)
}

private class LifecycleJobObserver(
  private val job: Job,
  private val target: Lifecycle.Event,
  private val lifecycle: Lifecycle
) : LifecycleObserver, CompletionHandler {
  @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
  fun onEvent(owner: LifecycleOwner, event: Lifecycle.Event) {
    if (event == this.target) {
      owner.lifecycle.removeObserver(this)
      job.cancel()
    }
  }

  override fun invoke(cause: Throwable?) {
    lifecycle.removeObserver(this)
  }
}
