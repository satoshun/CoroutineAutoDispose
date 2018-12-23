package com.github.satoshun.coroutine.autodispose.sample

import androidx.appcompat.app.AppCompatActivity
import com.github.satoshun.coroutine.autodispose.lifecycle.LifecycleContinuationInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
