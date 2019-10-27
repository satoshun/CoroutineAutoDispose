package com.github.satoshun.coroutine.autodispose.sample

import android.content.Context
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.github.satoshun.coroutine.autodispose.view.autoDisposeInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class MainView(context: Context) : View(context) {
  init {
    val job = autoDisposeScope.launch {
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

val View.autoDisposeScope: CoroutineScope
  get() = (context as ComponentActivity).lifecycleScope + autoDisposeInterceptor()
