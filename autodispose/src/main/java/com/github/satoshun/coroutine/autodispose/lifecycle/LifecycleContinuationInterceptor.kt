package com.github.satoshun.coroutine.autodispose.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Job
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

@Suppress("FunctionName")
fun LifecycleContinuationInterceptor(lifecycleOwner: LifecycleOwner): ContinuationInterceptor =
  LifecycleContinuationInterceptorImpl(lifecycleOwner.lifecycle)

@Suppress("FunctionName")
fun LifecycleContinuationInterceptor(lifecycle: Lifecycle): ContinuationInterceptor =
  LifecycleContinuationInterceptorImpl(lifecycle)

internal class LifecycleContinuationInterceptorImpl(
  private val lifecycle: Lifecycle
) : ContinuationInterceptor {
  override val key: CoroutineContext.Key<*>
    get() = ContinuationInterceptor

  override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
    val job = continuation.context[Job]
    if (job != null) {
      lifecycle.addJob(job)
    }
    return continuation
  }
}
