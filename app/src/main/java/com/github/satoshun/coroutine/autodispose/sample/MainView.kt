package com.github.satoshun.coroutine.autodispose.sample

import android.content.Context
import android.util.Log
import android.view.View
import com.github.satoshun.coroutine.autodispose.view.autoDisposeScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
