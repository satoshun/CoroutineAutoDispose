# Coroutine AutoDispose

[![CircleCI](https://circleci.com/gh/satoshun/CoroutineAutoDispose.svg?style=svg)](https://circleci.com/gh/satoshun/CoroutineAutoDispose)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.satoshun.coroutine.autodispose/autodispose/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.satoshun.coroutine.autodispose/autodispose)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-CoroutineAutoDispose-green.svg?style=flat)](https://android-arsenal.com/details/1/7406)

Coroutine AutoDispose is an Kotlin Coroutine library for automatically disposal.

## Overview

Often, Coroutine subscriptions need to stop in response to some event (like a Activity#onStop()).
In order to support this common scenario in Coroutine.

### LifecycleAutoDisposeInterceptor(Lifecycle)

LifecycleAutoDisposeInterceptor can use with [CoroutineScope](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-scope/).
It create a CoroutineInterceptor for automatically disposal with AAC lifecycle events.

```kotlin
abstract class BaseActivity : AppCompatActivity(),
  CoroutineScope {

  private val job = Job()
  override val coroutineContext get() = job +
      Dispatchers.Main +
      LifecycleAutoDisposeInterceptor(this) // or autoDisposeInterceptor()
}

class MainActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    // automatically dispose when onDestroy
    launch {
      ...
    }
  }

  override fun onResume() {
    // automatically dispose when onPause
    launch {
      ...
    }
  }
}
```

### Lifecycle.autoDispose(Job)

This Job an automatically disposal with Android Lifecycle events.

```kotlin
val job = launch { ... }
lifecycle.autoDispose(job)
```

### ViewAutoDisposeInterceptor(Lifecycle)

ViewAutoDisposeInterceptor can use with [CoroutineScope](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-scope/).
It create a CoroutineInterceptor for automatically disposal with View attach/detach events.

```kotlin
class MainView(context: Context) : View(context), CoroutineScope {
  private val job = Job()
  override val coroutineContext
    get() = job +
      Dispatchers.Main +
      ViewAutoDisposeInterceptor(this) // or autoDisposeInterceptor()
  ...
}
```

## Donwload

```groovy
implementation 'com.github.satoshun.coroutine.autodispose:autodispose:${version}'
```

## etc

This library referred [uber/AutoDispose](https://github.com/uber/AutoDispose).
