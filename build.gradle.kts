plugins {
  id("idea")
  id("java-library")
  kotlin("jvm") version "1.8.10" apply false
  kotlin("plugin.serialization") version "1.8.10" apply false
}

val javaVersion = 17

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(javaVersion))
  }
}

allprojects {
  group = "org.semgusng"
  version = "0.1.0.0"

  repositories {
    mavenCentral()
  }
}

subprojects {
  tasks.withType<JavaCompile>().configureEach {
    modularity.inferModulePath.set(true)
    options.apply {
      encoding = "UTF-8"
      isDeprecation = true
      release.set(javaVersion)
      compilerArgs.addAll(listOf("-Xlint:unchecked", "--enable-preview"))
    }
  }

  tasks.withType<Test>().configureEach {
    jvmArgs = listOf("--enable-preview")
    useJUnitPlatform()
    enableAssertions = true
    reports.junitXml.mergeReruns.set(true)
    testLogging.showStandardStreams = true
    testLogging.showCauses = true
  }

  tasks.withType<JavaExec>().configureEach {
    jvmArgs = listOf("--enable-preview")
    enableAssertions = true
  }

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
    kotlinOptions.useK2 = true
  }
}
