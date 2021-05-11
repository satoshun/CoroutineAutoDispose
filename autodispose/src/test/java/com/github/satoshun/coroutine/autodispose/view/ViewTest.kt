package com.github.satoshun.coroutine.autodispose.view

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.satoshun.coroutine.autodispose.lifecycle.AutoDisposeScopeTestActivity
import com.github.satoshun.coroutine.autodispose.lifecycle.JobSubject
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewTest {
    @get:Rule
    val scenarioRule = ActivityScenarioRule(AutoDisposeScopeTestActivity::class.java)

    private val Activity.contentView get() = findViewById<ViewGroup>(android.R.id.content)

    @Test
    fun `cancel job when view is detached`() {
        scenarioRule.scenario.onActivity {
            val view = View(it)
            it.contentView.addView(view)

            // simulate too too long task
            val job = view.autoDisposeScope.launch { delay(1000000) }

            JobSubject.assertThat(job).isNotCanceled()

            it.contentView.removeView(view)

            JobSubject.assertThat(job).isCanceled()
        }
    }

    @Test
    fun `cancel job`() {
        scenarioRule.scenario.onActivity {
            val view = View(it)
            it.contentView.addView(view)

            // simulate too too long task
            val job = view.autoDisposeScope.launch { delay(1000000) }
            JobSubject.assertThat(job).isNotCanceled()

            job.cancel()
            JobSubject.assertThat(job).isCanceled()

            it.contentView.removeView(view)
            JobSubject.assertThat(job).isCanceled()
        }
    }

    @Test
    fun `multi subscribe`() {
        scenarioRule.scenario.onActivity {
            val view = View(it)
            it.contentView.addView(view)

            // simulate too too long task
            val job1 = view.autoDisposeScope.launch { delay(1000000) }
            JobSubject.assertThat(job1).isNotCanceled()
            job1.cancel()
            JobSubject.assertThat(job1).isCanceled()

            val job2 = view.autoDisposeScope.launch { delay(1000000) }
            JobSubject.assertThat(job2).isNotCanceled()
            job2.cancel()
            JobSubject.assertThat(job2).isCanceled()

            // same coroutine scope
            assertThat(view.autoDisposeScope).isSameInstanceAs(view.autoDisposeScope)

            it.contentView.removeView(view)
            JobSubject.assertThat(job1).isCanceled()
            JobSubject.assertThat(job2).isCanceled()
        }
    }

    @Test
    fun `view is attach and detach`() {
        scenarioRule.scenario.onActivity {
            val view = View(it)
            it.contentView.addView(view)

            // simulate too too long task
            val job1 = view.autoDisposeScope.launch { delay(1000000) }
            JobSubject.assertThat(job1).isNotCanceled()

            it.contentView.removeView(view)
            JobSubject.assertThat(job1).isCanceled()

            val job2 = view.autoDisposeScope.launch { delay(1000000) }
            JobSubject.assertThat(job2).isNotCanceled()

            it.contentView.addView(view)
            JobSubject.assertThat(job2).isNotCanceled()

            val job3 = view.autoDisposeScope.launch { delay(1000000) }
            JobSubject.assertThat(job3).isNotCanceled()

            it.contentView.removeView(view)
            JobSubject.assertThat(job2).isCanceled()
            JobSubject.assertThat(job3).isCanceled()
        }
    }
}
