# 📊 Android Finance Tracker (Modern MAD Portfolio)

A production-ready personal finance management application built for Android. This project focuses on intensive local data manipulation, custom data visualization, reactive state management, and secure document exporting. It was developed to demonstrate mastery of modern software engineering practices and **MAD (Modern Android Development)** industry standards.

## 🚀 Key Features
* **Reactive Financial Tracking:** Seamlessly logs income and expenses with real-time balance calculations powered by local storage.
* **Custom Data Visualization:** An interactive Donut/Pie Chart drawn entirely from scratch using **Jetpack Compose Canvas 2D** geometry (zero third-party library dependencies).
* **Robust CSV Exporting:** Converts raw local database transactions into structured spreadsheet documents (`.csv`) formatted for instant compatibility with Microsoft Excel and Google Sheets.
* **Secure File Sharing:** Implements Android **FileProvider** architecture to securely stream and share generated CSV reports directly to external apps like WhatsApp, Gmail, or Google Drive.
* **Seamless Dark Mode Support:** Features a fully adaptive Material 3 design system that automatically recalibrates layout surfaces and dynamically toggles the phone's status bar notification icons for optimal contrast.

## 🛠️ Tech Stack & Architecture Standards
* **Language:** Kotlin 2.x (Modern type-safe compilation)
* **UI Framework:** Jetpack Compose (Declarative UI layout engineering)
* **Design System:** Material Design 3
* **Architecture:** MVVM (Model-View-ViewModel) + Repository Pattern
* **Database Engine:** Room Database (Robust SQLite abstraction layer)
* **Asynchronous Flow:** Kotlin Coroutines & StateFlow (Strict Unidirectional Data Flow / UDF)
* **Annotation Processing:** KSP (Kotlin Symbol Processing)
* **Dependency Management:** Gradle Version Catalog (`libs.versions.toml`)

## 🏛️ Code Architecture Structure
This project strictly enforces the **Separation of Concerns (SoC)** principle, isolating business logic from infrastructure requirements:
* `data/local/`: Handles database initialization, entities (tables), and DAOs (asynchronous reactive queries).
* `data/repository/`: Acts as the single source of truth mediating data streams between local persistence and the UI layer.
* `ui/dashboard/`: Contains the financial business logic controller (ViewModel) and the reactive dashboard container screens (Composables).
* `ui/components/`: Dedicated to advanced pixel-perfect graphic layouts and low-level custom engine drawings (Canvas).
