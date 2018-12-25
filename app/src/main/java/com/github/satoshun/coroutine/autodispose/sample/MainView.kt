package com.github.satoshun.coroutine.autodispose.sample

import android.content.Context
import android.util.Log
import android.view.View
import com.github.satoshun.coroutine.autodispose.view.ViewAutoDispose
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainView(context: Context) : View(context), CoroutineScope {
  private val job = Job()
  override val coroutineContext
    get() = job +
      Dispatchers.Main +
      ViewAutoDispose(this)

  init {
    val job = launch {
      while (true) {
        delay(2000)
        Log.d("View", "init")
      }
    }
    job.invokeOnCompletion {
      Log.d("View", "init job completed")
    }
  }
}
