package com.github.satoshun.coroutine.autodispose.lifecycle

import androidx.activity.ComponentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class TestActivity : ComponentActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + autoDisposeInterceptor()
}

class AutoDisposeScopeTestActivity : ComponentActivity()
