<div align="center">
  
  # NukeLexicon
  A modern, lightweight i18n and MiniMessage parsing API for Spigot plugins.
  
  <br>
  
  ![Java 25](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk)
  ![Platform](https://img.shields.io/badge/Platform-Spigot%20%2F%20Paper-blue?style=for-the-badge)
  
</div>

---

## ✨ Features
* **Modern Formatting**: Native support for [MiniMessage](https://docs.papermc.io/adventure/minimessage/format/) (RGB, Gradients, Click Events).
* **Smart i18n**: Multi-language support with automatic player locale detection and fallback systems.
* **PlaceholderAPI Integration**: Seamlessly converts `%placeholders%` into MiniMessage-compatible tags.
* **Zero Boilerplate**: Optimized utility classes to dispatch formatted components in a single line.
* **Config Management**: Includes a robust YAML configuration updater and custom config handlers.

## 📦 Installation (Gradle Kotlin DSL)

Add the JitPack repository and the dependency to your `build.gradle.kts`:

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    // Replace '1.0.0' with the latest release version
    implementation("com.github.nukecraft5419:NukeLexicon:1.0.0")
}
```

### **Option A: Quick Initialization (Recommended)**

Automatically detects your plugin's name, prefix, and uses `en_US` as the default language.
    
```java
@Override
public void onEnable() {
    // Essential for Adventure Audiences and internal managers
    NukeLexicon.init(this);
}
```

### Option B: Advanced Initialization

Manually define the fallback language and the default prefix.

```java
@Override
public void onEnable() {
    // Parameters: Plugin instance, Fallback language, Default Prefix
    NukeLexicon.init(this, "en_GB", "&8[&6MyPlugin&8]");
}
```

### 1.5 Clean Shutdown

To prevent memory leaks, remember to close the API in your onDisable:
```java
@Override
public void onDisable() {
    NukeLexicon.close();
}
```

### Send Formatted Messages

Use SendUtils to deliver MiniMessage-formatted strings to any CommandSender:

```java
// Supports MiniMessage tags, hex colors, and PAPI placeholders
SendUtils.sendMessage(player, "<gradient:aqua:blue>Welcome back, %player_name%!</gradient>");

// Log formatted messages to console
SendUtils.log("<red>[Alert]</red> System core initialized.");
```


### Localization (i18n)

Retrieve and send messages based on the player's client language automatically:

```java
// Fetches the path 'messages.welcome' from the player's locale file
SendUtils.sendTranslation(player, "messages.welcome");
```

### 📊 Metrics

NukeLexicon uses bStats to track anonymous usage data. View global statistics here:

[![https://bstats.org/signatures/bukkit/NukeLexicon.svg](https://bstats.org/signatures/bukkit/NukeLexicon.svg)](https://bstats.org/plugin/bukkit/NukeLexicon/29948)

### 📄 License

This project is licensed under the MIT License.

<div align="center">
  <sub>Built with ❤️ by Nukecraft5419</sub>
</div>
