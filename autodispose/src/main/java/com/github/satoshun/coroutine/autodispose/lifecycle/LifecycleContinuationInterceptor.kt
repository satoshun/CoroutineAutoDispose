package com.github.satoshun.coroutine.autodispose.lifecycle

import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Job
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

@Suppress("FunctionName")
fun LifecycleContinuationInterceptor(lifecycleOwner: LifecycleOwner): ContinuationInterceptor =
  LifecycleContinuationInterceptorImpl(lifecycleOwner)

internal class LifecycleContinuationInterceptorImpl(
  private val lifecycleOwner: LifecycleOwner
) : ContinuationInterceptor {
  override val key: CoroutineContext.Key<*>
    get() = ContinuationInterceptor

  override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
    val job = continuation.context[Job]
    if (job != null) {
      lifecycleOwner.addJob(job)
    }
    return continuation
  }
}
