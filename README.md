# 📊 Android Finance Tracker (Premium Portfolio App)

A production-ready personal finance management application built for Android. This project showcases heavy local data manipulation, high-security data encryption, custom drawing visualization, reactive state processing, and streamlined data extraction. It serves as a comprehensive demonstration of **MAD (Modern Android Development)** best practices and strict software engineering industry standards.

## 🚀 Key Features & Architectural Enhancements
* **🛡️ Biometric Security Lock:** Secures sensitive financial records with a built-in device fingerprint/face unlock layer utilizing the Google `androidx.biometric` framework.
* **📈 Smooth Animated Canvas Charts:** An interactive financial distribution Donut/Pie Chart engineered completely from scratch using **Jetpack Compose Canvas 2D** geometry (zero third-party dependencies) with dynamic, fluid entrance animations.
* **🎯 Unified Export & Share Flow:** Merges complex storage logic into a single-click workflow. It instantly converts raw Room DB transactions into a clean semicolon-separated `.csv` report and streams it safely to external services (WhatsApp, Gmail, Google Drive) using **FileProvider Architecture**.
* **💡 Reactive State Processing:** Synchronizes real-time balance calculations, spending distributions, and transactional history reactively to the layout surfaces.
* **🌓 Automatic System-Wide Theme Adaptation:** Seamlessly responds to the user's phone dark/light mode preference, offering adaptive background dark shades and dynamic Status Bar notification icon color inversions for enhanced contrast.

## 🛠️ Tech Stack & Industrial Competencies
* **Language:** Kotlin 2.x (Modern type-safe compile-time optimization)
* **UI Engine:** Jetpack Compose (Declarative interface layout composition)
* **Design Standards:** Material Design 3 Ecosystem
* **Architecture:** MVVM (Model-View-ViewModel) + Repository Design Pattern
* **Database Engine:** Room Database (Robust SQLite abstraction layer)
* **Asynchronous Flow:** Kotlin Coroutines & StateFlow (Strict Unidirectional Data Flow / UDF)
* **Annotation Processing:** KSP (Kotlin Symbol Processing)
* **Dependency System:** Gradle Version Catalog (`libs.versions.toml`)
* **Build Optimization:** Custom R8/ProGuard configuration squeezing production package footprint down to **5 MB**.

## 🏛️ Code Architecture Structure
This project strictly enforces the **Separation of Concerns (SoC)** principle, isolating business logic from infrastructure requirements:
* `data/local/`: Handles database initialization, entities (tables), and DAOs (asynchronous reactive queries).
* `data/repository/`: Acts as the single source of truth mediating data streams between local persistence and the UI layer.
* `ui/dashboard/`: Contains the financial business logic controller (ViewModel) and the reactive dashboard container screens (Composables).
* `ui/components/`: Dedicated to advanced pixel-perfect graphic layouts and low-level custom engine drawings (Canvas).