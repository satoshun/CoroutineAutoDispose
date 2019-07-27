package com.github.satoshun.coroutine.autodispose.lifecycle

import com.google.common.truth.Fact.simpleFact
import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject
import com.google.common.truth.Truth
import kotlinx.coroutines.Job

class JobSubject(
  metadata: FailureMetadata?,
  private val actual: Job?
) : Subject(metadata, actual) {
  companion object {
    fun assertThat(job: Job?): JobSubject {
      return Truth.assertAbout(::JobSubject).that(job)
    }
  }

  fun isCanceled() {
    Truth.assertThat(actual).isNotNull()
    if (!actual!!.isCancelled) {
      failWithoutActual(simpleFact("not canceled Job"))
    }
  }

  fun isNotCanceled() {
    Truth.assertThat(actual).isNotNull()
    if (actual!!.isCancelled) {
      failWithoutActual(simpleFact("already canceled Job"))
    }
  }
}
