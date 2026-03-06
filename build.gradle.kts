plugins {
    id("java-library")
    id("maven-publish")
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
    compileOnly(libs.adventureBukkit) // Adventure Platform Bukkit
    compileOnly(libs.miniMessage) // MiniMessage API

    implementation(libs.bStats) // bStats API
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

tasks.withType<JavaCompile>().configureEach {
  options.encoding = "UTF-8"
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])
    }
  }
}
