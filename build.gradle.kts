plugins {
    id("java-library")
    id("maven-publish")
}

group = project.property("group") as String
version = project.property("version") as String

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
        name = "spigotmc-repo"
    }
    maven("https://central.sonatype.com/repository/maven-snapshots/") {
        name = "central-snapshots"
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
    languageVersion = JavaLanguageVersion.of(25)
  }
}

tasks.withType<JavaCompile>().configureEach {
  options.encoding = "UTF-8"
}
