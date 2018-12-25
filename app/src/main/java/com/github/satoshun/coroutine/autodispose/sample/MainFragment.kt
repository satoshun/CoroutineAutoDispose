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

    // auto release when onDestroyView
    val job = launch {
      while (true) {
        delay(1000)
        Log.d("fragment", "start onViewCreated")
      }
    }
    job.invokeOnCompletion {
      Log.d("fragment", "onViewCreated Job completed")
    }
  }

  override fun onResume() {
    super.onResume()
    // auto release when onPause
    val job = launch {
      while (true) {
        delay(1000)
        Log.d("fragment", "start onResume")
      }
    }
    job.invokeOnCompletion {
      Log.d("fragment", "onResume Job completed")
    }
  }
}
