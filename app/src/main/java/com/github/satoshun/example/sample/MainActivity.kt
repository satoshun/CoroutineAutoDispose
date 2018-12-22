package com.github.satoshun.example.sample

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.github.satoshun.example.sample.databinding.MainActBinding
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
