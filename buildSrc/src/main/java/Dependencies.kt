object Vers {
  const val compile_sdk = 28
  const val min_sdk = 24
  const val target_sdk = 28

  const val kotlin = "1.3.11"
  const val couroutine = "1.0.1"
}

object Libs {
  const val android_plugin = "com.android.tools.build:gradle:3.4.0-alpha09"
  const val kotlin_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Vers.kotlin}"
  const val versions_plugin = "com.github.ben-manes:gradle-versions-plugin:0.20.0"

  const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Vers.kotlin}"
  const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Vers.couroutine}"
  const val ui_coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Vers.couroutine}"

  const val ktx = "androidx.core:core-ktx:1.0.0"

  const val appcompat = "androidx.appcompat:appcompat:1.0.2"
  const val recyclerview = "androidx.recyclerview:recyclerview:1.1.0-alpha01"
  const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.0-alpha2"
  const val cardview = "androidx.cardview:cardview:1.0.0"

  const val activityx = "androidx.activity:activity-ktx:1.0.0-alpha03"
  const val fragmentx = "androidx.fragment:fragment-ktx:1.1.0-alpha03"

  const val material = "com.google.android.material:material:1.0.0"
  const val coordinatorlayout = "androidx.coordinatorlayout:coordinatorlayout:1.0.0"

  const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.0.0"
  const val livedata = "androidx.lifecycle:lifecycle-livedata:2.0.0"
  const val lifecycle = "androidx.lifecycle:lifecycle-common:2.0.0"
  const val lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:2.0.0"
  const val lifecycle_extensions = "androidx.lifecycle:lifecycle-extensions:2.0.0"

  private const val vdagger = "2.19"
  const val dagger = "com.google.dagger:dagger:$vdagger"
  const val dagger_processor = "com.google.dagger:dagger-compiler:$vdagger"
  const val dagger_android = "com.google.dagger:dagger-android:$vdagger"
  const val dagger_android_processor = "com.google.dagger:dagger-android-processor:$vdagger"
  const val dagger_android_support = "com.google.dagger:dagger-android-support:$vdagger"

  const val junit = "junit:junit:4.12"
  const val truth = "com.google.truth:truth:0.42"
  const val test_rules = "androidx.test:rules:1.1.1"
  const val test_runner = "androidx.test:runner:1.1.1"
  const val test_core = "androidx.test:core-ktx:1.1.0"
  const val test_junit_rules = "androidx.test.ext:junit:1.0.0"
  const val mockito_kotlin = "com.nhaarman:mockito-kotlin-kt1.1:1.5.0"
  const val espresso = "androidx.test.espresso:espresso-core:3.1.0"
  const val robolectric = "org.robolectric:robolectric:4.1"
}
