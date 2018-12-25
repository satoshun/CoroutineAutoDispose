package com.github.satoshun.coroutine.autodispose.view

import android.view.View
import kotlinx.coroutines.Job
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

/**
 * Create a ContinuationInterceptor that follows attach/detach lifecycle of [View].
 */
@Suppress("FunctionName")
fun ViewAutoDispose(view: View): ContinuationInterceptor =
  ViewAutoDisposeImpl(view)

private class ViewAutoDisposeImpl(
  private val view: View
) : ContinuationInterceptor {
  override val key: CoroutineContext.Key<*>
    get() = ContinuationInterceptor

  override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
    val job = continuation.context[Job]
    if (job != null) {
      view.autoDispose(job)
    }
    return continuation
  }
}
