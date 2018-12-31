package com.github.satoshun.coroutine.autodispose.lifecycle

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LifecycleOwnerJobTest {
  @get:Rule val scenarioRule = ActivityScenarioRule(ComponentActivity::class.java)

  @Test
  fun addJob_onCreated() {
    val job = GlobalScope.launch { delay(100000) }

    val scenario = scenarioRule.scenario

    scenario.moveToState(Lifecycle.State.CREATED)
    scenario.onActivity {
      it.autoDispose(job)
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

    val scenario = scenarioRule.scenario

    scenario.moveToState(Lifecycle.State.RESUMED)
    scenario.onActivity {
      it.autoDispose(job)
    }
    JobSubject.assertThat(job).isNotCanceled()

    scenario.onActivity {
      (it.lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }
    JobSubject.assertThat(job).isCanceled()
  }

  @Test
  fun addJob_onDestroyed() {
    val job = GlobalScope.launch { delay(100000) }

    val lifecycle = object : Lifecycle() {
      override fun addObserver(observer: LifecycleObserver) {
      }

      override fun removeObserver(observer: LifecycleObserver) {
      }

      override fun getCurrentState(): State {
        return State.DESTROYED
      }
    }

    val result = runCatching { lifecycle.autoDispose(job) }
    assertThat(result.exceptionOrNull()).isInstanceOf(LifecycleFinishedException::class.java)
  }

  @Test
  fun completionHandler() {
    val fixedObserverSize = 1
    val job = GlobalScope.launch { delay(100000) }

    val scenario = scenarioRule.scenario
    scenario.moveToState(Lifecycle.State.CREATED)
    scenario.onActivity { it.autoDispose(job) }

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
