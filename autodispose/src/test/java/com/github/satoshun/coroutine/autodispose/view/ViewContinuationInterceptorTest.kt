package com.github.satoshun.coroutine.autodispose.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.satoshun.coroutine.autodispose.lifecycle.JobSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.coroutines.CoroutineContext

@RunWith(AndroidJUnit4::class)
class ViewContinuationInterceptorTest {
  @Test
  fun viewContinuationInterceptor() {
    val scenario = ActivityScenario.launch(ComponentActivity::class.java)

    var job: Job? = null
    var view: TestView? = null
    scenario.moveToState(Lifecycle.State.CREATED)
    scenario.onActivity {
      view = TestView(it)
      it.setContentView(view)

      job = view!!.launch { delay(100000) }
    }
    JobSubject.assertThat(job).isNotCanceled()

    scenario.onActivity {
      // detach view
      it.findViewById<ViewGroup>(android.R.id.content).removeView(view)
    }
    JobSubject.assertThat(job).isCanceled()
  }
}

class TestView(context: Context) : View(context), CoroutineScope {
  private val job = Job()
  override val coroutineContext: CoroutineContext
    get() = job + Dispatchers.Main + ViewContinuationInterceptor(this)
}
