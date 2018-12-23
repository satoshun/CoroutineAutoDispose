package com.github.satoshun.coroutine.autodispose.sample

import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_act)

    val childJob = launch {
      Log.d("hoge", coroutineContext[Job].toString())
      while (true) {
        delay(1200)
        Log.d("hoge", "delayed")
      }
    }
    childJob.invokeOnCompletion {
      Log.d("hoge", "completed")
    }
  }

  override fun onResume() {
    super.onResume()
    val childJob = launch {
      Log.d("hoge2", coroutineContext[Job].toString())
      while (true) {
        delay(3000)
        Log.d("hoge2", "delayed")
      }
    }
    childJob.invokeOnCompletion {
      Log.d("hoge2", "completed")
    }
  }
}
