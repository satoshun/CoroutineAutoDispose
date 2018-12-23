package com.github.satoshun.example.sample

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.Runnable
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : AppCompatActivity(),
  CoroutineScope {

  private val job = Job()
  override val coroutineContext: CoroutineContext get() = job + Dispatchers.Main + LifecycleContinuationInterceptor(this)

  override fun onDestroy() {
    super.onDestroy()
    job.cancel()
  }
}

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

class LifecycleContinuationInterceptor(
  private val lifecycleOwner: LifecycleOwner
) : ContinuationInterceptor {
  override val key: CoroutineContext.Key<*>
    get() = ContinuationInterceptor

  override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
    Log.d("hoge3", continuation.context[Job].toString())
    // todo nullable?
    lifecycleOwner.addJob(continuation.context[Job]!!)
    return continuation
  }
}

// todo more delegate and extends MainCoroutineDispatcher?
class WrapperDispatcher(
  private val original: CoroutineDispatcher
) : CoroutineDispatcher() {
  override fun dispatch(context: CoroutineContext, block: Runnable) =
    original.dispatch(context, block)

  @ExperimentalCoroutinesApi override fun isDispatchNeeded(context: CoroutineContext): Boolean {
    return original.isDispatchNeeded(context)
  }

  @InternalCoroutinesApi override fun dispatchYield(context: CoroutineContext, block: Runnable) {
    original.dispatchYield(context, block)
  }
}
