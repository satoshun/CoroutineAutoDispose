package com.github.satoshun.coroutine.autodispose.sample

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_act)

    val childJob = launch {
      while (true) {
        delay(1200)
        Log.d("activity", "onCreate")
      }
    }
    childJob.invokeOnCompletion {
      Log.d("activity", "onCreate job completed")
    }

    supportFragmentManager.commit {
      add(R.id.frame, MainFragment())
    }
  }

  override fun onResume() {
    super.onResume()
    val childJob = launch {
      while (true) {
        delay(3000)
        Log.d("activity", "onResume")
      }
    }
    childJob.invokeOnCompletion {
      Log.d("activity", "onResume job completed")
    }
  }
}
