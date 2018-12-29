package com.github.satoshun.coroutine.autodispose.sample

import androidx.appcompat.app.AppCompatActivity
import com.github.satoshun.coroutine.autodispose.lifecycle.autoDisposeInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class BaseActivity : AppCompatActivity(),
  CoroutineScope {

  override val coroutineContext
    get() = Dispatchers.Main + autoDisposeInterceptor()
}
