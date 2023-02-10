plugins {
  kotlin("jvm")
}

dependencies {
  testImplementation("io.kotest:kotest-runner-junit5:5.5.5")
  testImplementation("io.kotest:kotest-framework-datatest:5.5.5")
  testImplementation(project(":bench"))
}
