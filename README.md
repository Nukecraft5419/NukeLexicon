<div align="center">

  <img src="https://raw.githubusercontent.com/Nukecraft5419/NukeLexicon/refs/heads/main/assets/logo.png" alt="NukeLexicon Logo" height="256" />
  
  <h1>📚 NukeLexicon API</h1>
  
  <em>A powerful, centralized core utility API for Spigot/Paper plugins, specializing in native multi-language (i18n) support and modern text formatting.</em>
  
  <br>

  <p>
    <a href="https://discord.nukecraft5419.com/">
      <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/social/discord-plural_vector.svg" alt="discord-plural" height="64" style="margin-right: 5px;" />
    </a>
    <a href="https://x.com/Nukecraft5419">
      <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/social/twitter-singular_vector.svg" alt="twitter-singular" height="64" style="margin-right: 5px;" />
    </a>
  </p>

  <p>
    <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/built-with/java_vector.svg" alt="Java" height="64" style="margin-right: 5px;" />
    <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/built-with/gradle_vector.svg" alt="Gradle" height="64" style="margin-right: 5px;" />
    <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/spigot_vector.svg" alt="Spigot" height="64" style="margin-right: 5px;" />
    <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/paper_vector.svg" alt="Paper" height="64" style="margin-right: 5px;" />
  </p>
  
  <p>
    <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
    <img src="https://img.shields.io/badge/Version-1.1.0-success?style=for-the-badge" alt="Version" />
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
* 🎭 **Rich Media Delivery**: Send Titles, Action Bars, and Sounds with a single line of code, fully integrated with the translation engine.
* 🔄 **PlaceholderAPI Integration**: Seamlessly converts legacy `%placeholders%` into MiniMessage tags while preserving RGB formatting.
* ⚡ **Zero Boilerplate**: Optimized utility classes to dispatch formatted components or retrieve safe config values (Int, Float, Boolean).
* 🛠️ **Developer Toolbox**: Robust YAML configuration updater and a component-based API for GUIs and items.

---

## 📦 Installation (Gradle Kotlin DSL)

Add the **Nukecraft5419 Repository** and the dependency to your `build.gradle.kts`:

```kotlin
repositories {
    mavenCentral()
    maven("https://repo.nukecraft5419.com/")
}

dependencies {
    // Replace '1.1.0' with the latest release version
    implementation("dev.nukecraft5419:nukelexicon:1.1.0")
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

// New in 1.1.0: Send Titles with custom timings (in milliseconds)
// Parameters: Player, TitlePath, SubtitlePath, FadeIn, Stay, FadeOut
SendUtils.sendTitle(player, "titles.welcome.main", "titles.welcome.sub", 500, 3000, 1000);

// New in 1.1.0: Send Action Bar messages
SendUtils.sendActionBar(player, "messages.actionbar_info");

// New in 1.1.0: Play sounds using Kyori Adventure keys
SendUtils.playSound(player, "entity.experience_orb.pickup", 1.0f, 1.0f);

// Log formatted messages to console
SendUtils.log("<red>[Alert]</red> System core initialized.");
```

---

### 🛠️ Component API

Need a formatted component for an Item name, Lore, or GUI title? Use `getTranslationComponent` to fetch the localized and parsed Component directly:

```java
// Returns a Kyori Component with full MiniMessage and PAPI support
Component displayName = SendUtils.getTranslationComponent(player, "items.sword_name");
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
