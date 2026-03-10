<div align="center">

  <img src="assets/logo.png" alt="NukeLexicon Logo" height="256" />
  
  <h1>📚 NukeLexicon API</h1>
  
  <em>A powerful, centralized core utility API for Spigot/Paper plugins, specializing in native multi-language (i18n) support and modern text formatting.</em>
  
  <br>

  <p>
    <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/built-with/java_vector.svg" alt="Java" height="64" style="margin-right: 5px;" />
    <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/built-with/gradle_vector.svg" alt="Gradle" height="64" style="margin-right: 5px;" />
    <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/spigot_vector.svg" alt="Spigot" height="64" style="margin-right: 5px;" />
    <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/paper_vector.svg" alt="Paper" height="64" style="margin-right: 5px;" />
  </p>
  
  <p>
    <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
    <img src="https://img.shields.io/badge/Version-1.0.0-success?style=for-the-badge" alt="Version" />
    <a href="https://github.com/nukecraft5419/NukeLexicon/actions">
      <img src="https://img.shields.io/github/actions/workflow/status/nukecraft5419/NukeLexicon/ci.yml?branch=main&style=for-the-badge&logo=github&label=Build" alt="Build Status" />
    </a>
    <a href="https://repo.nukecraft5419.com/">
      <img src="https://img.shields.io/badge/Maven_Repo-Hosted-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven Repo" />
    </a>
    <img src="https://img.shields.io/badge/Dependabot-Enabled-blue?style=for-the-badge&logo=dependabot" alt="Dependabot" />
    
  </p>

  <p>
    <img src="https://img.shields.io/endpoint?url=https://ghloc.vercel.app/api/nukecraft5419/NukeLexicon/badge&style=for-the-badge&label=Lines%20of%20Code" alt="Lines of Code" />
    <img src="https://img.shields.io/github/repo-size/nukecraft5419/NukeLexicon?style=for-the-badge&label=Repo%20Size" alt="Repo Size" />
    <img src="https://img.shields.io/github/license/nukecraft5419/NukeLexicon?style=for-the-badge&color=blue&label=License" alt="License" />
    <a href="https://www.codefactor.io/repository/github/nukecraft5419/nukelexicon">
      <img src="https://www.codefactor.io/repository/github/nukecraft5419/nukelexicon/badge?style=for-the-badge" alt="CodeFactor" />
    </a>
  </p>
</div>

---

## ✨ Features
* 🎨 **Modern Formatting**: Native support for [MiniMessage](https://docs.papermc.io/adventure/minimessage/format/) (RGB, Gradients, Click Events).
* 🌐 **Smart i18n**: Multi-language support with automatic player locale detection and fallback systems.
* 🔄 **PlaceholderAPI Integration**: Seamlessly converts `%placeholders%` into MiniMessage-compatible tags.
* ⚡ **Zero Boilerplate**: Optimized utility classes to dispatch formatted components in a single line.
* ⚙️ **Config Management**: Includes a robust YAML configuration updater and custom config handlers.

---

## 📦 Installation (Gradle Kotlin DSL)

Add the **Nukecraft5419 Repository** and the dependency to your `build.gradle.kts`:

```kotlin
repositories {
    mavenCentral()
    maven("https://repo.nukecraft5419.com/")
}

dependencies {
    // Replace '1.0.0' with the latest release version
    implementation("dev.nukecraft5419:nukelexicon:1.0.0")
}
```

---

### ⚠️ Note on Shading: 

Since you are using implementation, it is highly recommended to use the Shadow Gradle Plugin to shade and relocate NukeLexicon inside your plugin's JAR. 
This prevents version conflicts if multiple plugins on the same server use different versions of this API. 
If you prefer server owners to install NukeLexicon as a standalone plugin, use compileOnly instead.

---

### **Option A: Quick Initialization (Recommended)**

Automatically detects your plugin's name, prefix, and uses `en_US` as the default language:
    
```java
@Override
public void onEnable() {
    // Essential for Adventure Audiences and internal managers
    NukeLexicon.init(this);
}
```

---

### Option B: Advanced Initialization

Manually define the fallback language and the default prefix:

```java
@Override
public void onEnable() {
    // Parameters: Plugin instance, Fallback language, Default Prefix
    NukeLexicon.init(this, "en_GB", "<dark_gray>[<gold>MyPlugin</gold>]");
}
```

---

### 🧹 Clean Shutdown

To prevent memory leaks, remember to close the API in your onDisable:

```java
@Override
public void onDisable() {
    NukeLexicon.close();
}
```

---

### 💬 Send Formatted Messages

Use SendUtils to deliver MiniMessage-formatted strings to any CommandSender:

```java
// Supports MiniMessage tags, hex colors, and PAPI placeholders
SendUtils.sendMessage(player, "<gradient:aqua:blue>Welcome back, %player_name%!</gradient>");

// Log formatted messages to console
SendUtils.log("<red>[Alert]</red> System core initialized.");
```

---

### 🌍 Localization (i18n)

Retrieve and send messages based on the player's client language automatically:

```java
// Fetches the path 'messages.welcome' from the player's locale file
SendUtils.sendTranslation(player, "messages.welcome");
```

---

## 📊 Metrics

NukeLexicon uses bStats to track anonymous usage data. View global statistics here:

[![https://bstats.org/signatures/bukkit/NukeLexicon.svg](https://bstats.org/signatures/bukkit/NukeLexicon.svg)](https://bstats.org/plugin/bukkit/NukeLexicon/29948)

---

## 🤝 Contributing

Contributions are welcome! If you have suggestions or find bugs, feel free to open an issue or a pull request. Let's make localized plugins easier for everyone!

---

## 📄 License

This project is licensed under the MIT License.

---

<div align="center">
  <sub>Built with ❤️ by Nukecraft5419</sub>
</div>
