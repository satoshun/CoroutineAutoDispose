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
fun ViewAutoDisposeInterceptor(view: View): ContinuationInterceptor =
  ViewAutoDisposeInterceptorImpl(view)

/**
 * Create a ContinuationInterceptor that follows attach/detach lifecycle of [View].
 */
fun View.autoDisposeInterceptor(): ContinuationInterceptor =
  ViewAutoDisposeInterceptor(this)

private class ViewAutoDisposeInterceptorImpl(
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
