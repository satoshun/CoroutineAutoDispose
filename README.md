# Coroutine AutoDispose

[![CircleCI](https://circleci.com/gh/satoshun/CoroutineAutoDispose.svg?style=svg)](https://circleci.com/gh/satoshun/CoroutineAutoDispose)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.satoshun.coroutine.autodispose/autodispose/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.satoshun.coroutine.autodispose/autodispose)
[![codecov](https://codecov.io/gh/satoshun/CoroutineAutoDispose/branch/master/graph/badge.svg)](https://codecov.io/gh/satoshun/CoroutineAutoDispose)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-CoroutineAutoDispose-green.svg?style=flat)](https://android-arsenal.com/details/1/7406)

Coroutine AutoDispose is an Kotlin Coroutine library for automatically disposal.

## Overview

Often, Coroutine subscriptions need to stop in response to some event (like a Activity#onStop()).
In order to support this common scenario in Coroutine.

### autoDisposeScope

This library provide a autoDisposeScope extension method.
It can be used like a [lifecycleScope](https://developer.android.com/reference/kotlin/androidx/lifecycle/package-summary#(androidx.lifecycle.LifecycleOwner).lifecycleScope:androidx.lifecycle.LifecycleCoroutineScope).

It create a [CoroutineScope](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-scope/) for automatically disposal with Android Architecture Component Lifecycle events.

```kotlin
class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    // automatically dispose when onDestroy
    autoDisposeScope.launch {
      ...
    }
  }

  override fun onResume() {
    // automatically dispose when onPause
    autoDisposeScope.launch {
      ...
    }
  }
}
```

It can also be uses with Fragment and View.

### Lifecycle.autoDispose(Job)

This Job an automatically disposal with Android Lifecycle events.

```kotlin
val job = launch { ... }
lifecycle.autoDispose(job)
```

### Use with RecyclerView

CoroutineScope can be used from a itemView of RecyclerView.ViewHolder.

```kotlin
override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
  holder.itemView.autoDisposeScope.launch {
    ...
  }
}
```

## Download

```groovy
implementation 'com.github.satoshun.coroutine.autodispose:autodispose:${version}'
```

## etc

This library referred [uber/AutoDispose](https://github.com/uber/AutoDispose).
