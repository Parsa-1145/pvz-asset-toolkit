# libpvz

A lightweight LibGDX library for parsing and rendering PopCap/EA Plants vs. Zombies 2 PAM animation files.

## Installation

### Gradle (JitPack)

Add the JitPack repository and dependency to your `build.gradle`:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.pizpizi:libPVZ:v0.1.0' // Replace v0.1.0 with desired release tag or commit hash
}
```

## How to Use

### 1. Initialization

Initialize the `TextureBank` and `PamPlayer` by pointing them to your extracted asset directory (which should contain `IMAGES/`, `ATLASES/`, and a `Resources.json`, which is the decoded `RESOURCES.RTON`. Rton decoding may be added in future.):

```java
FileHandle assetsFolder = Gdx.files.internal("assets");

TextureBank textures = new TextureBank("768", assetsFolder);
PamPlayer player = new PamPlayer(textures, assetsFolder);
```

### 2. Loading & Rendering Animations

You can render clips dynamically by name, or use optimized `ClipRef` handles to eliminate string lookups in the render loop.

#### Dynamic Rendering:

```java
@Override
public void render() {
    stateTime += Gdx.graphics.getDeltaTime();

    // Required: updates background texture loading queue
    textures.update();

    batch.begin();
    // Render using PAM path and clip name directly
    player.draw(batch, "ZOMBIES/PEASANT/PEASANT.PAM", "idle", stateTime, x, y, true);
    batch.end();
}
```

#### Synchronous / `ClipRef` Optimized Rendering (Recommended):

```java
// Preload synchronously during setup
player.loadSync("ZOMBIES/PEASANT/PEASANT.PAM");

// Obtain reusable performance handles (O(1) lookups during draw)
ClipRef walkClip = player.getClip("ZOMBIES/PEASANT/PEASANT.PAM", "walk");

@Override
public void render() {
    stateTime += Gdx.graphics.getDeltaTime();
    textures.update();

    batch.begin();
    player.draw(batch, walkClip, stateTime, x, y, true);
    batch.end();
}
```

### 3. Visibility Maps (Armor & Parts)

Some character parts (like armor or butter) are hidden by default. Use a `Map<String, Boolean>` to toggle specific part visibilities:

```java
Map<String, Boolean> visibilityMap = new HashMap<>();
visibilityMap.put("_zombie_egypt_armor2_states", true);
visibilityMap.put("zombie_armor_bucket_norm", true); // Show bucket armor

// Render with visibility map
player.draw(batch, walkClip, stateTime, x, y, true, visibilityMap);
```

## Running the Demo

The repository includes a runnable playground demo (`Demo.java`) located in the test sources.

1. Configure your local asset paths in `gradle.properties` (or pass them via command line):
   ```properties
   systemProp.pvz.assets=/path/to/Base Assets
   systemProp.pvz.pam=768/INITIAL/ZOMBIE/ZOMBIE_EGYPT_BASIC/ZOMBIE_EGYPT_BASIC.PAM
   ```
2. Run the Gradle task:
   ```bash
   ./gradlew runDemo
   ```

<img width="688" height="644" alt="libPVZ_Demo" src="https://github.com/user-attachments/assets/0d496925-54bb-4902-8f8e-3c431b3c632c" />

---

## Disclaimer

This library is an educational tool. The source code of `libpvz` is provided under the MIT License.

**All "Plants vs. Zombies" assets, animations, file formats, and characters are the intellectual property of Electronic Arts and PopCap Games.** This project is not affiliated with, endorsed by, or sponsored by EA. Any proprietary assets used in conjunction with this library are strictly for educational purposes.
