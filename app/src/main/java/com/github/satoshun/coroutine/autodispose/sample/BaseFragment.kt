package com.github.satoshun.coroutine.autodispose.sample

import androidx.fragment.app.Fragment
import com.github.satoshun.coroutine.autodispose.lifecycle.autoDisposeInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class BaseFragment : Fragment(),
  CoroutineScope {
  override val coroutineContext
    get() = Dispatchers.Main +
      viewLifecycleOwner.autoDisposeInterceptor()
}
