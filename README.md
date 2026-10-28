# 🎵 Melora — Modern Offline Music Player for Android

[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.21-purple.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203%20(BOM%202026.09.00)-4285F4.svg?style=flat&logo=android)](https://developer.android.com/jetpack/compose)
[![Media3](https://img.shields.io/badge/AndroidX%20Media3-1.11.1-3DDC84.svg?style=flat&logo=android)](https://developer.android.com/guide/topics/media/media3)
[![AGP](https://img.shields.io/badge/AGP-9.4.1-brightgreen.svg?style=flat&logo=android)](https://developer.android.com/build)
[![Clean Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM%20%2B%20UDF-brightgreen.svg?style=flat)]()
[![Min SDK](https://img.shields.io/badge/Min%20SDK-26%20(Android%208.0)-informational.svg?style=flat)](https://developer.android.com)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-37-blue.svg?style=flat)](https://developer.android.com)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

**Melora** is a high-performance, beautifully crafted offline music player for Android. Built from the ground up using **Jetpack Compose**, **AndroidX Media3**, and strict **Clean Architecture + Unidirectional Data Flow (UDF)** principles, Melora delivers a seamless, battery-friendly listening experience with a distinctive **Deep Forest & Mint** visual identity and a custom component design system.

---

## ✨ Features

### 🎨 Custom Component Design System (`Melora*` Components)
* **100% Unified App Design System**: Built with an entire suite of custom views (`MeloraText`, `MeloraButton`, `MeloraOutlinedButton`, `MeloraTextButton`, `MeloraIconButton`, `MeloraCard`, `MeloraDialog`, `MeloraBottomSheet`, `MeloraSlider`, `MeloraSwitch`, `MeloraRadioButton`, `MeloraCheckbox`, `MeloraTextField`, `MeloraLinearProgressIndicator`, `MeloraCircularProgressIndicator`, `FilterPill`).
* **Centralized Typography Engine**: All typography roles (`AppTypography.TitleLarge`, `BodyMedium`, etc.) delegate directly to `MeloraText` for consistent text styling app-wide.
* **App-Wide Dialog & Sheet Standard**: Every popup, dialog, and bottom sheet across all screens utilizes custom `MeloraDialog` and `MeloraBottomSheet` layouts.

### 🎧 Audio Engine & Media Controls
* **AndroidX Media3 & ExoPlayer**: Solid background audio playback powered by `MediaSessionService` with lock-screen media controls, Bluetooth auto-pause/resume, and system media integration.
* **Format Support**: High-fidelity playback for MP3, FLAC, WAV, AAC, OGG, and OPUS.
* **Audio Equalizer**: Studio vertical console faders with frequency band controls, stock presets, **Bass Boost**, and **3D Virtualizer** spatial audio.
* **Gauge Ruler Speed Control**: Interactive speed controlling bottom sheet (`PlaybackSpeedBottomSheet`) featuring a touch/drag tick-mark gauge ruler (`0.25x`–`2.50x`), fine-tune `-`/`+` step buttons, and one-tap reset.
* **Native Option Sleep Timer**: Sleep timer bottom sheet (`SleepTimerBottomSheet`) with clean option rows (`Stop after this track`, `15 min`, `30 min`, `45 min`, `60 min`, `Custom` minute slider) and live active timer countdown with turn-off toggle.
* **Hero Song Options Bottom Sheet**: Rich bottom sheet (`SongOptionsBottomSheet`) featuring a hero song header card (Artwork, Title, Artist, Album, Duration) with instant shortcut actions (Favorite, Play Now, Play Next, Add to Playlist, Share).
* **Enhanced Info & Lyrics Bottom Sheets**: Structured metadata info card (`SongInfoBottomSheet`) and lyrics bottom sheet (`LyricsBottomSheet`).
* **Queue Management**: Dynamic playback queue with drag-to-reorder, shuffle, repeat-all, and repeat-one modes.

### 📚 Music Library, Multi-Selection & Media Scan
* **Local MediaStore Indexing & Media Scan**: Automatic querying of local storage via MediaStore with zero lag, plus a manual **Scan Media** option in Settings to instantly index newly added tracks.
* **Streamlined Bottom Navigation**: 6 clean bottom navigation tabs: **Songs** (with top Recently Played 3-track horizontal carousel & All Songs / Favorites filter pills), **Playlists**, **Folders**, **Albums**, **Artists**, and **Settings**.
* **Smart Multi-Selection & Batch Actions**: Multi-select mode with **Select All**, **Deselect All**, batch **Delete** (with confirmation dialog), and multi-file **Share** (`ACTION_SEND_MULTIPLE`).
* **Playlists & Favorites**: Create custom playlists, duplicate/delete playlists, and star favorite tracks backed by Room database persistence.
* **Search & Sorting**: Real-time search with debouncing and sorting by Title, Artist, Album, Duration, Date Added, or Track Number.
* **Dual-Layout View Modes**: Grid and List view toggle support across Playlists, Folders, Albums, and Artists screens.

### 🎨 Visuals & UI / UX
* **Deep Forest Visual Identity**: Dark obsidian-charcoal surfaces (`#101714`) contrasted against vibrant mint (`#72D6A0`) accents.
* **Adaptive Orientation**: Dedicated responsive layouts for **Portrait** and **Landscape** orientations across phones and tablets.
* **Onboarding & Permissions**: Permission screen featuring a glowing waveform hero badge, benefit cards, and direct app settings deep-linking.
* **Adaptive App Icon & SplashScreen**: Vinyl groove adaptive icon with monochrome asset support for Android 13+ wallpaper themes, plus Android 12+ SplashScreen.
* **100% Private & Offline**: Operates completely offline without tracking, analytics, or network requests.

---

## 🏛️ Architecture & Clean Design

Melora strictly follows **Clean Architecture**, **MVVM**, and **Unidirectional Data Flow (UDF)**:

```
                            ┌────────────────────────┐
                            │   Presentation Layer   │
                            │ (Jetpack Compose UI)   │
                            └───────────┬────────────┘
                                        │ (UI Events)
                                        ▼
                            ┌────────────────────────┐
                            │      ViewModel         │
                            │  (StateFlow<*UiState>) │
                            └───────────┬────────────┘
                                        │ (Executes)
                                        ▼
                            ┌────────────────────────┐
                            │      Domain Layer      │
                            │ (Use Cases & Contracts)│
                            └───────────┬────────────┘
                                        │ (Implements)
                                        ▼
                            ┌────────────────────────┐
                            │       Data Layer       │
                            │  (Room, MediaStore,    │
                            │   MediaSessionService) │
                            └────────────────────────┘
```

### Module / Package Structure

```
com.example.melora/
├── core/
│   ├── designsystem/             # Foundation theme tokens (Color, Theme, Typography, Dimensions)
│   │   └── components/           # Custom App Design System (MeloraText, MeloraButton, MeloraCard, etc.)
│   │       ├── bottomsheets/     # MeloraBottomSheet, SongOptionsBottomSheet, QueueBottomSheet, PlaybackSpeedBottomSheet
│   │       └── dialogs/          # MeloraDialog, DeleteConfirmDialog, RenameSongDialog
│   ├── media/                    # Media3 PlaybackService, EqualizerManager, SleepTimerManager
│   └── utils/                    # Kotlin Extensions (AudioExtensions, FileExtensions, SongExtensions, etc.)
│
├── data/
│   ├── datastore/                # Jetpack DataStore preferences (Theme, playback state)
│   ├── local/
│   │   ├── db/                   # Room Database, DAOs & Entities (PlaylistDao, FavoriteDao, HistoryDao)
│   │   └── mediastore/           # MediaStore local audio query provider
│   └── repository/               # Repository implementations (Music, Playlist, Playback, Equalizer)
│
├── di/                           # Hilt Dependency Injection modules
│
├── domain/                       # Pure Kotlin domain (Models, Repository contracts, Use cases)
│   ├── model/                    # Immutable models (Song, Album, Artist, Playlist, AudioFolder)
│   ├── repository/               # Repository interfaces
│   └── usecase/                  # Focused, testable use cases (GetSongs, SearchMusic, PlaylistUseCases)
│
└── presentation/                 # Jetpack Compose UI (Feature-based packages)
    ├── albums/                   # Album grid/list view mode
    ├── artists/                  # Artist grid/list view mode
    ├── equalizer/                # 5-band vertical console faders & sound effects
    ├── favorites/                # Favorites list and management
    ├── folders/                  # Folder grid/list view mode
    ├── navigation/               # Compose type-safe navigation graph
    ├── permission/               # Audio permission onboarding screen
    ├── player/                   # Fullscreen player (Track info, gauge speed, sleep timer)
    ├── playlists/                # Playlist grid/list view mode & management
    ├── search/                   # Instant search with filter pills
    └── settings/                 # Appearance, media scan, audio behavior, data management
```

---

## 🛠️ Tech Stack & Gradle Configuration

| Technology | Version | Purpose |
| :--- | :--- | :--- |
| **Kotlin** | `2.4.21` | Primary language with null-safety and extension functions |
| **Android Gradle Plugin** | `9.4.1` | Build system configuration |
| **KSP Compiler** | `2.3.12` | Kotlin Symbol Processing for Room and Hilt |
| **Jetpack Compose** | `Material 3 (BOM 2026.09.00)` | Declarative UI toolkit |
| **AndroidX Media3** | `1.11.1` | `ExoPlayer`, `MediaSessionService`, and background playback |
| **Dagger Hilt** | `2.60.1` | Dependency injection across ViewModels, repositories, and services |
| **Room Database** | `2.8.5` | Type-safe SQLite persistence with DAOs and Entities |
| **Jetpack DataStore** | `1.3.0` | Asynchronous key-value preference storage |
| **AndroidX SplashScreen** | `1.2.0` | Modern splash screen with exit transitions |
| **SDP & SSP** | `1.1.1` | Scalable dimensions across screen densities |
| **Kotlin Coroutines & Flow** | `1.11.0` | Asynchronous reactive state management (`StateFlow`) |
| **Coil Compose** | `2.7.0` | Asynchronous artwork image loading |

---

## 🚀 Getting Started

### Prerequisites
* **Android Studio**: Ladybug (2024.2.1+) or newer
* **JDK**: Version 17 or higher
* **Android SDK**: Compile / Target SDK `37`, Minimum SDK `26` (Android 8.0)

### Clone & Build
```bash
# Clone the repository
git clone https://github.com/hammad-ahmed-android-engineer/Melora.git
cd Melora

# Build Debug APK
./gradlew assembleDebug

# Run Unit Tests
./gradlew testDebugUnitTest
```

---

## 🔒 Permissions

Melora respects user privacy and requests permissions essential for core audio playback:
* `android.permission.READ_MEDIA_AUDIO` *(Android 13+)*: Discovers local audio files.
* `android.permission.READ_EXTERNAL_STORAGE` *(Android 12 and below)*: Accesses legacy audio storage.
* `android.permission.POST_NOTIFICATIONS` *(Android 13+)*: Displays background playback controls.
* `android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK`: Keeps audio playback active in the background.

---

## 📄 License

Distributed under the Apache License 2.0. See `LICENSE` for more information.
