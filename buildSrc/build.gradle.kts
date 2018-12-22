plugins {
  `kotlin-dsl`
}

apply {
  plugin("kotlin")
}

repositories {
  jcenter()
  google()
}

dependencies {
  implementation(gradleApi())
}
