package com.github.satoshun.coroutine.autodispose.lifecycle

import com.google.common.truth.Fact.simpleFact
import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject
import com.google.common.truth.Truth
import kotlinx.coroutines.Job

class JobSubject(
  metadata: FailureMetadata?,
  private val actual: Job?
) : Subject<JobSubject, Job>(metadata, actual) {
  companion object {
    fun assertThat(job: Job?): JobSubject {
      return Truth.assertAbout(::JobSubject).that(job)
    }
  }

  fun isCanceled() {
    if (!actual!!.isCancelled) {
      failWithoutActual(simpleFact("not canceled Job"))
    }
  }

  fun isNotCanceled() {
    if (actual!!.isCancelled) {
      failWithoutActual(simpleFact("already canceled Job"))
    }
  }
}
