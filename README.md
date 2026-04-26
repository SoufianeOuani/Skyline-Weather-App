# 🌤️ Skyline — Modern Weather App

<p align="center">
  <img src="https://github.com/user-attachments/assets/c26a2aca-b798-4c37-9976-5f1dab06b460" width="180" />
</p>

<p align="center">
  <b>A modern Android weather app built with Kotlin & Jetpack Compose</b><br/>
  Clean architecture • Smooth UX • Interactive data visualization
</p>

---

## 🚀 Overview

**Skyline** is a production-style weather application designed to demonstrate modern Android development practices, focusing on:

- Clean architecture (MVVM)
- Reactive UI with Jetpack Compose
- Smooth animations and interactions
- Rich, interactive weather data visualization

---

## 🎥 Demo

### 📱 Main Screen Experience
<img width="100%" src="https://github.com/user-attachments/assets/aeb501e0-bf10-467c-8c9f-b5e2630afe32" />

---

## ✨ Features

- 🔍 **Smart City Search**
  - Real-time suggestions
  - Debounced API calls

- 🌤️ **Current Weather**
  - Temperature, condition, and dynamic backgrounds

- 📅 **7-Day Forecast**
  - Clean and readable weekly overview

- ⏰ **Hourly Forecast (Advanced)**
  - Interactive scrollable timeline
  - Auto-scroll to current hour
  - Synced graph + UI interaction

- 📈 **Dynamic Graph**
  - Wind / temperature visualization
  - Gradient-filled curve
  - Real-time selection indicator

- ⭐ **Favorites System**
  - Save and access cities instantly

---

## 📊 Highlights

- 🎯 State-driven UI (Single Source of Truth)
- 🔄 Debounced search optimization
- 📱 Fully built with Jetpack Compose
- 🎨 Premium UI inspired by iOS Weather
- ⚡ Smooth animations and transitions
- 🧠 Clean and scalable code structure

---

## 🧱 Architecture

The app follows **MVVM (Model-View-ViewModel)**:
presentation/
→ UI (Compose)
→ ViewModel (state management)

domain/
→ Models

data/
→ API (Retrofit)
→ DTOs


### Key Concepts

- Immutable state updates (`copy()`)
- Separation of concerns
- Reactive UI rendering

---

## 🛠️ Tech Stack

| Category        | Tech                     |
|----------------|--------------------------|
| Language       | Kotlin                   |
| UI             | Jetpack Compose          |
| Networking     | Retrofit                 |
| Concurrency    | Coroutines               |
| Architecture   | MVVM                     |

---

🔜 Roadmap
🌧 Weather modes toggle (temperature / wind / rain)
💾 Local database (Room)
🎞️ Advanced animations
📊 Additional weather metrics (humidity, UV, pressure)
📌 Purpose

This project was built to:

Practice modern Android development
Apply real-world architecture patterns
Build a production-level UI/UX experience
🤝 Feedback

I’m always open to feedback and suggestions!

Feel free to:

Open an issue
Suggest improvements
Share ideas
⭐ Support

If you like this project, consider giving it a ⭐
It helps a lot!

👤 Author

Soufiane Ouani

LinkedIn: https://www.linkedin.com/in/soufiane-ouani-60252a3a2?utm_source=share_via&utm_content=profile&utm_medium=member_android
