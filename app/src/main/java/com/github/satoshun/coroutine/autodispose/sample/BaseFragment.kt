package com.github.satoshun.coroutine.autodispose.sample

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.satoshun.coroutine.autodispose.lifecycle.autoDisposeInterceptor
import kotlinx.coroutines.plus

abstract class BaseFragment : Fragment() {
  val autoDisposeScope
    get() = viewLifecycleOwner.lifecycleScope + viewLifecycleOwner.autoDisposeInterceptor()
}
