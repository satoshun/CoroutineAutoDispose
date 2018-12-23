package com.github.satoshun.coroutine.autodispose.sample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainFragment : BaseFragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return TextView(context)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val job = launch {
      while (true) {
        delay(1000)
        Log.d("fragment", "onViewCreated")
      }
    }
    job.invokeOnCompletion {
      Log.d("fragment", "onViewCreated Job completed")
    }
  }

  override fun onResume() {
    super.onResume()
    launch {
      while (true) {
        delay(1000)
        Log.d("fragment", "onResume Job completed")
      }
    }
  }
}
