package com.github.satoshun.coroutine.autodispose.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.Job

fun LifecycleOwner.addJob(job: Job) {
  val state = lifecycle.currentState
  val event = when (state) {
    Lifecycle.State.DESTROYED -> Lifecycle.Event.ON_DESTROY // ON_DESTROY?
    Lifecycle.State.INITIALIZED -> Lifecycle.Event.ON_DESTROY
    Lifecycle.State.CREATED -> Lifecycle.Event.ON_DESTROY
    Lifecycle.State.STARTED -> Lifecycle.Event.ON_STOP
    Lifecycle.State.RESUMED -> Lifecycle.Event.ON_PAUSE
  }
  lifecycle.addObserver(AnyObserver(job, event))
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
