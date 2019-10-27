package com.github.satoshun.coroutine.autodispose.sample

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.satoshun.coroutine.autodispose.lifecycle.autoDisposeInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus

abstract class BaseActivity : AppCompatActivity() {
  val autoDisposeScope: CoroutineScope
    get() = lifecycleScope + autoDisposeInterceptor()
}
