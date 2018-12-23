package com.github.satoshun.coroutine.autodispose.lifecycle

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LifecycleOwnerJobTest {
  @Test
  fun addJob_onCreated() {
    val job = GlobalScope.launch { delay(100000) }

    val scenario = ActivityScenario.launch(ComponentActivity::class.java)

    scenario.moveToState(Lifecycle.State.CREATED)
    scenario.onActivity {
      it.addJob(job)
    }
    JobSubject.assertThat(job).isNotCanceled()

    scenario.moveToState(Lifecycle.State.RESUMED)
    JobSubject.assertThat(job).isNotCanceled()

    scenario.onActivity {
      (it.lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }
    JobSubject.assertThat(job).isNotCanceled()

    scenario.moveToState(Lifecycle.State.DESTROYED)
    JobSubject.assertThat(job).isCanceled()
  }

  @Test
  fun addJob_onResumed() {
    val job = GlobalScope.launch { delay(100000) }

    val scenario = ActivityScenario.launch(ComponentActivity::class.java)

    scenario.moveToState(Lifecycle.State.RESUMED)
    scenario.onActivity {
      it.addJob(job)
    }
    JobSubject.assertThat(job).isNotCanceled()

    scenario.onActivity {
      (it.lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }
    JobSubject.assertThat(job).isCanceled()
  }

  @Test
  fun completionHandler() {
    val fixedObserverSize = 1
    val job = GlobalScope.launch { delay(100000) }

    val scenario = ActivityScenario.launch(ComponentActivity::class.java)
    scenario.moveToState(Lifecycle.State.CREATED)
    scenario.onActivity { it.addJob(job) }

    scenario.onActivity {
      assertThat((it.lifecycle as LifecycleRegistry).observerCount)
        .isEqualTo(1 + fixedObserverSize)
    }

    runBlocking { job.cancelAndJoin() }
    scenario.onActivity {
      assertThat((it.lifecycle as LifecycleRegistry).observerCount)
        .isEqualTo(0 + fixedObserverSize)
    }
  }
}
