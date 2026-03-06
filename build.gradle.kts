plugins {
    id("java-library")
    id("maven-publish")
    alias(libs.plugins.shadowJar) // ShadowJar
}

group = providers.gradleProperty("group").get()
version = providers.gradleProperty("version").get()

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
        name = "spigotmc-repo"
    }
    maven("https://repo.extendedclip.com/releases/") {
        name = "placeholder-api"
    }
}

dependencies {
    compileOnly(libs.spigot) // Spigot API
    compileOnly(libs.placeholderApi) // Placeholder API

    implementation(libs.adventureBukkit) // Adventure Platform Bukkit
    implementation(libs.miniMessage) // MiniMessage API
    implementation(libs.bStats) // bStats API
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

tasks.jar {
  enabled = false
}

tasks.shadowJar {
// Instructs ShadowJar to bundle all dependencies marked as "implementation"
  // (like bStats and Kyori) into our final API JAR.
  configurations = listOf(project.configurations.runtimeClasspath.get())

  // Removes the default "-all" suffix from the generated JAR file name.
  archiveClassifier.set("")

  // Relocation: Moves external libraries into our API's internal package structure.
  // This is CRITICAL to prevent ClassNotFoundException or NoSuchMethodError conflicts
  // if a plugin using this API also has another version of these libraries.
  relocate("net.kyori", "dev.nukecraft5419.nukelexicon.libs.kyori")
  relocate("org.bstats", "dev.nukecraft5419.nukelexicon.libs.bstats")
}

tasks.build {
  dependsOn("shadowJar")
}

tasks.withType<JavaCompile>().configureEach {
  options.encoding = "UTF-8"
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      artifact(tasks.shadowJar)
    }
  }
}
