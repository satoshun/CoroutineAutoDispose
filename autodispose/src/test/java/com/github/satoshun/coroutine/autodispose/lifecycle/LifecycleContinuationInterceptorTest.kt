package com.github.satoshun.coroutine.autodispose.lifecycle

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.coroutines.CoroutineContext

@RunWith(AndroidJUnit4::class)
class LifecycleContinuationInterceptorTest {
  @Test
  fun lifecycleContinuationInterceptor_onCreated() {
    val scenario = ActivityScenario.launch(TestActivity::class.java)

    scenario.moveToState(Lifecycle.State.CREATED)
    var job: Job? = null
    scenario.onActivity {
      job = it.launch {
        delay(10000)
      }
    }
    assertThat(job!!.isCancelled).isFalse()

    scenario.onActivity {
      (it.lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }
    assertThat(job!!.isCancelled).isFalse()

    scenario.moveToState(Lifecycle.State.DESTROYED)
    assertThat(job!!.isCancelled).isTrue()
  }

  @Test
  fun lifecycleContinuationInterceptor_onResumed() {
    val scenario = ActivityScenario.launch(TestActivity::class.java)

    scenario.moveToState(Lifecycle.State.RESUMED)
    var job: Job? = null
    scenario.onActivity {
      job = it.launch {
        delay(10000)
      }
    }
    assertThat(job!!.isCancelled).isFalse()
    scenario.onActivity {
      (it.lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }

    assertThat(job!!.isCancelled).isTrue()
  }

  @Test
  fun lifecycleContinuationInterceptor_nested() {
    val scenario = ActivityScenario.launch(TestActivity::class.java)

    scenario.moveToState(Lifecycle.State.CREATED)
    var parentJob: Job? = null
    var nestedJob1: Job? = null
    var nestedJob2: Job? = null
    scenario.onActivity {
      parentJob = it.launch {
        nestedJob1 = launch { delay(10000) }
        nestedJob2 = async { delay(10000) }
        delay(10000)
      }
    }
    assertThat(parentJob!!.isCancelled).isFalse()
    assertThat(nestedJob1!!.isCancelled).isFalse()
    assertThat(nestedJob2!!.isCancelled).isFalse()

    scenario.onActivity {
      (it.lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }
    assertThat(parentJob!!.isCancelled).isFalse()
    assertThat(nestedJob1!!.isCancelled).isFalse()
    assertThat(nestedJob2!!.isCancelled).isFalse()

    nestedJob2!!.cancel()
    assertThat(parentJob!!.isCancelled).isFalse()
    assertThat(nestedJob1!!.isCancelled).isFalse()
    assertThat(nestedJob2!!.isCancelled).isTrue()

    scenario.moveToState(Lifecycle.State.DESTROYED)
    assertThat(parentJob!!.isCancelled).isTrue()
    assertThat(nestedJob1!!.isCancelled).isTrue()
    assertThat(nestedJob2!!.isCancelled).isTrue()
  }
}

class TestActivity : ComponentActivity(), CoroutineScope {
  private val job = Job()
  override val coroutineContext: CoroutineContext
    get() = job + Dispatchers.Main + LifecycleContinuationInterceptor(this)
}
