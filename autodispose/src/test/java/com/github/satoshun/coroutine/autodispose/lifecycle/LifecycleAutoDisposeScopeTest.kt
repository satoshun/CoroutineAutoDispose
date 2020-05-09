package com.github.satoshun.coroutine.autodispose.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LifecycleAutoDisposeScopeTest {
  @get:Rule val activity = ActivityScenarioRule(AutoDisposeScopeTestActivity::class.java)

  @Test
  fun autoDisposeScope_Created_to_Destroy() {
    val scenario = activity.scenario

    scenario.moveToState(Lifecycle.State.CREATED)

    var job1: Job? = null
    scenario.onActivity {
      job1 = it.autoDisposeScope.launch { delay(Long.MAX_VALUE) }
    }
    var job2: Job? = null
    scenario.onActivity {
      job2 = it.autoDisposeScope.launch { delay(Long.MAX_VALUE) }
    }
    JobSubject.assertThat(job1).isNotCanceled()
    JobSubject.assertThat(job2).isNotCanceled()

    scenario.moveToState(Lifecycle.State.RESUMED)
    JobSubject.assertThat(job1).isNotCanceled()
    JobSubject.assertThat(job2).isNotCanceled()

    scenario.onActivity {
      (it.lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }
    JobSubject.assertThat(job1).isNotCanceled()
    JobSubject.assertThat(job2).isNotCanceled()

    scenario.moveToState(Lifecycle.State.DESTROYED)
    JobSubject.assertThat(job1).isCanceled()
    JobSubject.assertThat(job2).isCanceled()
  }

  @Test
  fun autoDisposeScope_Resume_to_Pause() {
    val scenario = activity.scenario

    scenario.moveToState(Lifecycle.State.RESUMED)

    var job1: Job? = null
    scenario.onActivity {
      job1 = it.autoDisposeScope.launch { delay(Long.MAX_VALUE) }
    }
    var job2: Job? = null
    scenario.onActivity {
      job2 = it.autoDisposeScope.launch { delay(Long.MAX_VALUE) }
    }
    JobSubject.assertThat(job1).isNotCanceled()
    JobSubject.assertThat(job2).isNotCanceled()

    scenario.onActivity {
      (it.lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }
    JobSubject.assertThat(job1).isCanceled()
    JobSubject.assertThat(job2).isCanceled()

    scenario.moveToState(Lifecycle.State.DESTROYED)
    JobSubject.assertThat(job1).isCanceled()
    JobSubject.assertThat(job2).isCanceled()
  }

  @Test
  fun autoDisposeScope_Start_to_Stop() {
    val scenario = activity.scenario

    scenario.moveToState(Lifecycle.State.STARTED)

    var job1: Job? = null
    scenario.onActivity {
      job1 = it.autoDisposeScope.launch { delay(Long.MAX_VALUE) }
    }
    var job2: Job? = null
    scenario.onActivity {
      job2 = it.autoDisposeScope.launch { delay(Long.MAX_VALUE) }
    }
    JobSubject.assertThat(job1).isNotCanceled()
    JobSubject.assertThat(job2).isNotCanceled()

    scenario.onActivity {
      (it.lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }
    JobSubject.assertThat(job1).isNotCanceled()
    JobSubject.assertThat(job2).isNotCanceled()

    scenario.onActivity {
      (it.lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }
    JobSubject.assertThat(job1).isCanceled()
    JobSubject.assertThat(job2).isCanceled()

    scenario.moveToState(Lifecycle.State.DESTROYED)
    JobSubject.assertThat(job1).isCanceled()
    JobSubject.assertThat(job2).isCanceled()
  }
}
