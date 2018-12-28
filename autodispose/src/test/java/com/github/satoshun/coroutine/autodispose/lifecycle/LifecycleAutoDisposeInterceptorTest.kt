package com.github.satoshun.coroutine.autodispose.lifecycle

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
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
class LifecycleAutoDisposeInterceptorTest {
  @Test
  fun lifecycleAutoDisposeInterceptor_onCreated() {
    val scenario = ActivityScenario.launch(TestActivity::class.java)

    scenario.moveToState(Lifecycle.State.CREATED)
    var job: Job? = null
    scenario.onActivity {
      job = it.launch {
        delay(10000)
      }
    }
    JobSubject.assertThat(job).isNotCanceled()

    scenario.onActivity {
      (it.lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }
    JobSubject.assertThat(job).isNotCanceled()

    scenario.moveToState(Lifecycle.State.DESTROYED)
    JobSubject.assertThat(job).isCanceled()
  }

  @Test
  fun lifecycleAutoDisposeInterceptor_onResumed() {
    val scenario = ActivityScenario.launch(TestActivity::class.java)

    scenario.moveToState(Lifecycle.State.RESUMED)
    var job: Job? = null
    scenario.onActivity {
      job = it.launch {
        delay(10000)
      }
    }
    JobSubject.assertThat(job).isNotCanceled()
    scenario.onActivity {
      (it.lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }

    JobSubject.assertThat(job).isCanceled()
  }

  @Test
  fun lifecycleAutoDisposeInterceptor_nested() {
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


    JobSubject.assertThat(parentJob).isNotCanceled()
    JobSubject.assertThat(nestedJob1).isNotCanceled()
    JobSubject.assertThat(nestedJob2).isNotCanceled()

    scenario.onActivity {
      (it.lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }
    JobSubject.assertThat(parentJob).isNotCanceled()
    JobSubject.assertThat(nestedJob1).isNotCanceled()
    JobSubject.assertThat(nestedJob2).isNotCanceled()

    nestedJob2!!.cancel()
    JobSubject.assertThat(parentJob).isNotCanceled()
    JobSubject.assertThat(nestedJob1).isNotCanceled()
    JobSubject.assertThat(nestedJob2).isCanceled()

    scenario.moveToState(Lifecycle.State.DESTROYED)
    JobSubject.assertThat(parentJob).isCanceled()
    JobSubject.assertThat(nestedJob1).isCanceled()
    JobSubject.assertThat(nestedJob2).isCanceled()
  }
}

class TestActivity : ComponentActivity(), CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + autoDisposeInterceptor()
}
