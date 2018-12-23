package com.github.satoshun.coroutine.autodispose.lifecycle

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

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
private class WrapperDispatcher(
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
