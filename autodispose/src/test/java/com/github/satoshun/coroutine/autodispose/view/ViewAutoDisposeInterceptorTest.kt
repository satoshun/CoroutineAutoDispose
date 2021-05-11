package com.github.satoshun.coroutine.autodispose.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.satoshun.coroutine.autodispose.lifecycle.AutoDisposeScopeTestActivity
import com.github.satoshun.coroutine.autodispose.lifecycle.JobSubject
import kotlinx.coroutines.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.coroutines.CoroutineContext

@RunWith(AndroidJUnit4::class)
class ViewAutoDisposeInterceptorTest {
    @get:Rule
    val scenarioRule = ActivityScenarioRule(AutoDisposeScopeTestActivity::class.java)

    @Test
    fun viewAutoDisposeInterceptor() {
        val scenario = scenarioRule.scenario

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

    @Test
    fun viewAutoDisposeInterceptor_parentJob_cancel() {
        val scenario = scenarioRule.scenario

        var job: Job? = null
        var view: TestView? = null
        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.onActivity {
            view = TestView(it)
            it.setContentView(view)

            job = view!!.launch { delay(100000) }
        }
        JobSubject.assertThat(job).isNotCanceled()

        view!!.job.cancel()
        JobSubject.assertThat(job).isCanceled()
    }
}

class TestView(context: Context) : View(context), CoroutineScope {
    val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main + ViewAutoDisposeInterceptor(this)
}
