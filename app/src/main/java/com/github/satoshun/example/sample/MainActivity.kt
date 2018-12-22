package com.github.satoshun.example.sample

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.github.satoshun.example.sample.databinding.MainActBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = DataBindingUtil.setContentView<MainActBinding>(this, R.layout.main_act)

    val childJob = launch { }
    addJob(childJob)
  }

  override fun onResume() {
    super.onResume()
    val childJob = launch { }
    addJob(childJob)
  }
}

class AnyObserver(
  private val job: Job,
  private val event: Lifecycle.Event
) : LifecycleObserver {
  @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
  fun onEvent(owner: LifecycleOwner, event: Lifecycle.Event) {
    if (event == this.event) {
      owner.lifecycle.removeObserver(this)
      job.cancel()
    }
  }
}
