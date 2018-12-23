package com.github.satoshun.coroutine.autodispose.sample

import androidx.fragment.app.Fragment
import com.github.satoshun.coroutine.autodispose.lifecycle.LifecycleContinuationInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseFragment : Fragment(),
  CoroutineScope {
  private val job = Job()
  override val coroutineContext
    get() = job +
      Dispatchers.Main +
      LifecycleContinuationInterceptor(viewLifecycleOwner)
}
