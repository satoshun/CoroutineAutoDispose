package dependencies

const val COMPILE_SDK = 30
const val MIN_SDK = 14
const val TARGET_SDK = 30

const val VKOTLIN = "1.4.32"

const val ANDROID_PLUGIN = "com.android.tools.build:gradle:4.2.1"
const val KOTLIN_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:$VKOTLIN"
const val PUBLISH_PLUGIN = "com.vanniktech:gradle-maven-publish-plugin:0.15.1"

private const val VCOUROUTINE = "1.4.3"
const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib:$VKOTLIN"
const val COROUTINE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$VCOUROUTINE"
const val UI_COROUTINE = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$VCOUROUTINE"

const val LIFECYCLE = "androidx.lifecycle:lifecycle-common:2.2.0"
const val LIFECYCLE_RUNTIME = "androidx.lifecycle:lifecycle-runtime-ktx:2.2.0"

// use from sample
const val APPCOMPAT = "androidx.appcompat:appcompat:1.1.0"
const val CONSTRAINTLAYOUT = "androidx.constraintlayout:constraintlayout:2.0.4"
const val RECYCLER_VIEW = "androidx.recyclerview:recyclerview:1.0.0"
const val FRAGMENTX = "androidx.fragment:fragment-ktx:1.2.0"
const val KTX = "androidx.core:core-ktx:1.1.0"

// use from test
const val JUNIT = "junit:junit:4.12"
const val TRUTH = "com.google.truth:truth:1.0"
const val TEST_RULES = "androidx.test:rules:1.1.1"
const val TEST_RUNNER = "androidx.test:runner:1.1.1"
const val TEST_CORE = "androidx.test:core-ktx:1.1.0"
const val TEST_JUNIT_RULES = "androidx.test.ext:junit-ktx:1.1.0"
const val ESPRESSO = "androidx.test.espresso:espresso-core:3.1.1"
const val ROBOLECTRIC = "org.robolectric:robolectric:4.1"
const val ACTIVITYX = "androidx.activity:activity-ktx:1.1.0"
