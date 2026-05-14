# ACW 🎬

An Android cinema booking app built with Kotlin and Jetpack Compose, backed by Firebase. Ported and expanded from a web app, featuring end-to-end booking flows and a full admin system.

---

## Features

### User
- Browse currently showing and upcoming movies
- Select screenings by date, time, and hall
- Interactive seat selection with real-time availability
- Ticket purchasing simulation
- View and manage personal bookings

### Admin
- Manage movies (add, edit, delete)
- Configure theatres, halls, and seating layouts
- Schedule and manage screenings
- Manage seat states (maintenance / unavailable)
- Conflict validation for overlapping screenings

---

## Screenshots

| Home | Screening Selection | Seat Selection |
|------|---------------------|----------------|
| <img src="https://i.imgur.com/yGOw5Ra.jpeg" width="250"/> | <img src="https://i.imgur.com/b9Fuj77.jpeg" width="250"/> | <img src="https://i.imgur.com/3tC1J9d.jpeg" width="250"/> |

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose |
| Auth | Firebase Authentication |
| Database | Cloud Firestore |
| Build | Gradle |

---

## Setup

### Prerequisites
- Android Studio (latest stable)
- A Firebase account

### Steps

1. **Create a Firebase project**
   - Go to [Firebase Console](https://console.firebase.google.com/) and create a new project

2. **Register your Android app**
   - In the Firebase project, add an Android app
   - Use the package name matching the one in `app/build.gradle`

3. **Enable services**
   - Enable **Firestore Database** (start in test mode or configure rules)
   - Enable **Firebase Authentication** and your preferred sign-in methods

4. **Download config file**
   - Download `google-services.json` from the Firebase project settings

5. **Place the config file**
   ```
   ACW/
   └── app/
       └── google-services.json   ← place it here
   ```

6. **Build and run**
   - Open the project in Android Studio
   - Let Gradle sync
   - Run on an emulator or physical device (API 24+)
