package com.github.satoshun.coroutine.autodispose.sample

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.commit
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_act)

    // auto dispose when onDestroy
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

    findViewById<ViewGroup>(R.id.root).addView(MainView(this))
  }

  override fun onResume() {
    super.onResume()
    // auto dispose when onPause
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
