# BrainMon: A Trivia RPG Adventure 🧠⚔️

BrainMon is a location-based monster-catching game built with Modern Android Development (MAD) standards. Players explore real-world biomes, battle monsters using their knowledge (Math, Science, History), and complete their Pokedex.

![App Demo](screenshot/demo.gif)

## 📥 Download
[**Download the APK here**](https://github.com/kornti-debug/BrainMon/releases/tag/v1.0)

## 🚀 Features
* **Reactive UI:** Built 100% with **Jetpack Compose**.
* **Local Persistence:** Uses **Room Database** to save caught monsters and track Pokedex progress permanently.
* **Network Requests:** Fetches live monster data (PokeAPI) and trivia questions (OpenTriviaDB) using **Retrofit**.
* **Architecture:** Follows **MVVM** (Model-View-ViewModel) with **Clean Architecture** principles.
* **State Management:** Uses **Kotlin Flows** and **StateFlow** for real-time UI updates (e.g., catching a monster instantly updates the Explore screen stats).
* **Dynamic Logic:** Biome-specific encounters (Water types at the beach, Math questions in the desert).

## 🛠️ Tech Stack
* **Language:** Kotlin
* **UI:** Jetpack Compose (Material3)
* **Async:** Coroutines & Flow
* **DI:** Manual Dependency Injection (AppContainer)
* **Network:** Retrofit & Gson
* **Database:** Room & SQLite
* **Navigation:** Compose Navigation

## 📸 Screenshots
| Dashboard | Explore Screen | Battle Screen 
|:---:|:---:|:---:|
| <img src="screenshot/brainmon_dashboard.png" width="200"/> | <img src="screenshot/brainmon_explorescreen.png" width="200"/> | <img src="screenshot/brainmon_battlescreen.png" width="200"/>

| Pokedex | Lab Screen | Stats Screen |
|:---:|:---:|:---:|
| <img src="screenshot/brainmon_pokedex.png" width="200"/> | <img src="screenshot/brainmon_labscreen.png" width="200"/> | <img src="screenshot/brainmon_statsscreen.png" width="200"/>
