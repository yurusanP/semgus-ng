plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
}

dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
  testImplementation("io.kotest:kotest-runner-junit5:5.5.5")
  testImplementation("io.kotest:kotest-assertions-json:5.5.5")
  testImplementation("io.kotest:kotest-framework-datatest:5.5.5")
  testImplementation(project(":bench"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  // https://youtrack.jetbrains.com/issue/KT-49983/Implement-prototype-of-kotlinx.serialization-for-K2-compiler
  kotlinOptions.useK2 = false
}
