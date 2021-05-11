package com.github.satoshun.coroutine.autodispose.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.plus
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

/**
 * Create a [ContinuationInterceptor] that follows lifecycle of LifecycleOwner.
 */
fun LifecycleOwner.autoDisposeInterceptor(): ContinuationInterceptor =
    LifecycleAutoDisposeInterceptor(this)

/**
 * Create a [ContinuationInterceptor] that follows lifecycle of LifecycleOwner.
 */
@Suppress("FunctionName")
fun LifecycleAutoDisposeInterceptor(lifecycleOwner: LifecycleOwner): ContinuationInterceptor =
    LifecycleAutoDisposeInterceptor(lifecycleOwner.lifecycle)

/**
 * Create a [ContinuationInterceptor] that follows lifecycle.
 */
fun Lifecycle.autoDisposeInterceptor(): ContinuationInterceptor =
    LifecycleAutoDisposeInterceptor(this)

/**
 * Create a [ContinuationInterceptor] that follows lifecycle.
 */
@Suppress("FunctionName")
fun LifecycleAutoDisposeInterceptor(lifecycle: Lifecycle): ContinuationInterceptor =
    LifecycleAutoDisposeInterceptorImpl(lifecycle)

/**
 * * Create a [CoroutineScope] from [androidx.lifecycle.lifecycleScope] that follows lifecycle.
 */
val LifecycleOwner.autoDisposeScope: CoroutineScope
    get() = lifecycleScope + LifecycleAutoDisposeInterceptor(this)

private class LifecycleAutoDisposeInterceptorImpl(
    private val lifecycle: Lifecycle
) : ContinuationInterceptor {
    override val key: CoroutineContext.Key<*>
        get() = ContinuationInterceptor

    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        val job = continuation.context[Job]
        if (job != null) {
            lifecycle.autoDispose(job)
        }
        return continuation
    }
}
