package com.github.satoshun.coroutine.autodispose.sample

import android.content.Context
import android.util.Log
import android.view.View
import com.github.satoshun.coroutine.autodispose.view.autoDisposeInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainView(context: Context) : View(context),
  CoroutineScope {

  override val coroutineContext
    get() = Dispatchers.Main + autoDisposeInterceptor()

  init {
    val job = launch {
      while (true) {
        delay(1000)
        Log.d("MainView", "init loop")
      }
    }
    job.invokeOnCompletion {
      Log.d("MainView", "init completed")
    }
  }
}
