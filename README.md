# MovieKnowledge App

A modern Android application for movie discovery and management, built with Jetpack Compose and following clean architecture principles.

## 📱 Features

### Core Functionality
- **🎬 Add Movies to Database** - Initialize local database with curated movie collection
- **🔍 Search Movies** - Find specific movies by title using OMDB API
- **👥 Search Actors** - Discover movies by actor names in local database
- **📝 Search by Title** - Browse multiple movies matching search criteria
- **🎯 Filter Movies** - Filter and discover movies with advanced search
- **💾 Save Movies** - Store favorite movies to local database

### Technical Features
- **🏗️ Modern Architecture** - MVVM pattern with Repository design
- **🔄 Reactive Programming** - StateFlow for real-time UI updates
- **🎨 Material Design 3** - Beautiful, consistent UI components
- **📱 Single Activity** - Navigation Compose for seamless navigation
- **🌐 API Integration** - OMDB API for comprehensive movie data
- **💾 Local Storage** - Room database for offline access
- **⚡ Performance** - Efficient data loading and caching

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   UI Layer      │    │  Domain Layer   │    │   Data Layer    │
│                 │    │                 │    │                 │
│ • Compose UI    │◄──►│ • ViewModel     │◄──►│ • Repository    │
│ • Navigation    │    │ • StateFlow     │    │ • Room DB       │
│ • Screens       │    │ • Use Cases     │    │ • OMDB API      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Key Components

- **UI Layer**: Jetpack Compose screens with Material Design 3
- **ViewModel**: Manages UI state and business logic
- **Repository**: Single source of truth for data operations
- **Room Database**: Local storage for movies and offline access
- **Retrofit**: HTTP client for OMDB API integration

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog | 2023.1.1 or later
- JDK 17 or later
- Android SDK 34
- Minimum SDK 24 (Android 7.0)

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd MovieKnowledge
   ```

2. **Configure Android SDK**
   - Open Android Studio
   - Set up Android SDK path in `local.properties`
   - Sync project with Gradle files

3. **API Configuration**
   - The app uses OMDB API for movie data
   - API key is included in the project for demo purposes
   - For production use, obtain your own API key from [OMDB API](http://www.omdbapi.com/apikey.aspx)

4. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

## 📖 Usage Guide

### Home Screen
The main dashboard provides access to all features:

- **Add Movies to DB** - Initialize database with sample movies
- **Search for Movies** - Find specific movies by exact title
- **Search for Actors** - Find movies by actor names
- **Search by Title** - Browse multiple movies matching criteria  
- **Filter Movies** - Advanced movie filtering and discovery

### Feature Walkthrough

#### 1. Add Movies to Database
- Tap "Add Movies to DB" to initialize the local database
- Loads 5 curated movies with complete metadata
- Only needs to be done once per app installation

#### 2. Search Movies (Exact Title)
- Enter exact movie title (e.g., "Inception", "The Matrix")
- View detailed movie information from OMDB API
- Save movies to local database for offline access

#### 3. Search Actors
- Enter actor name (e.g., "Leonardo DiCaprio", "Morgan Freeman")
- Browse movies from local database containing that actor
- Case-insensitive partial matching supported

#### 4. Search by Title / Filter Movies
- Enter partial movie titles for broad search
- Browse multiple results with basic information
- Filter through extensive movie collections

### Sample Data
The app includes these movies for testing:
- **The Shawshank Redemption** (1994) - Tim Robbins, Morgan Freeman
- **Batman: The Dark Knight Returns, Part 1** (2012) - Peter Weller
- **The Lord of the Rings: The Return of the King** (2003) - Elijah Wood, Viggo Mortensen
- **Inception** (2010) - Leonardo DiCaprio, Joseph Gordon-Levitt
- **The Matrix** (1999) - Keanu Reeves, Laurence Fishburne

## 🛠️ Technical Stack

### Core Technologies
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern UI toolkit
- **Material Design 3** - UI component library
- **Navigation Compose** - Navigation framework

### Architecture Components
- **ViewModel** - UI state management
- **StateFlow** - Reactive state handling
- **Room** - Local database
- **Repository Pattern** - Data layer abstraction

### Networking & Data
- **Retrofit** - HTTP client
- **Gson** - JSON serialization
- **OkHttp** - HTTP logging and networking
- **Coroutines** - Asynchronous programming

### Development Tools
- **Gradle** - Build system
- **KSP** - Kotlin Symbol Processing
- **Android Gradle Plugin** - Android build tools

## 📁 Project Structure

```
app/src/main/java/com/example/movieknowledge/
├── data/
│   ├── local/
│   │   ├── entities/          # Room entities
│   │   ├── AppDatabase.kt     # Database configuration
│   │   └── MovieDao.kt        # Data access objects
│   ├── remote/
│   │   ├── models/            # API response models
│   │   └── OmdbApi.kt         # Retrofit API interface
│   └── repository/
│       └── MovieRepository.kt # Data repository
├── ui/
│   ├── components/            # Reusable UI components
│   ├── screens/               # App screens
│   │   ├── HomeScreen.kt
│   │   ├── SearchMoviesScreen.kt
│   │   ├── SearchActorsScreen.kt
│   │   ├── SearchByTitleScreen.kt
│   │   └── FilterMoviesScreen.kt
│   └── theme/                 # App theming
├── viewmodel/
│   └── MovieViewModel.kt      # UI state management
└── MainActivity.kt            # App entry point
```

## 🎨 UI/UX Features

### Design Principles
- **Material Design 3** - Modern, accessible design system
- **Responsive Layout** - Adapts to different screen sizes
- **Consistent Navigation** - Intuitive user flow
- **Loading States** - Clear feedback for async operations
- **Error Handling** - Graceful error messages and recovery

### Interactive Elements
- **Scroll-to-Top FAB** - Quick navigation in long lists
- **Search Icons** - Multiple ways to trigger search
- **Card-based Layout** - Easy scanning of movie information
- **Professional Typography** - Clear information hierarchy

## 🔧 Configuration

### Build Configuration
```kotlin
android {
    compileSdk = 34
    minSdk = 24
    targetSdk = 34
    
    buildFeatures {
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
}
```

### Dependencies
Key dependencies include:
- Jetpack Compose BOM
- Navigation Compose
- Room database
- Retrofit & Gson
- Material Design 3
- Coroutines

## 🧪 Testing

### Sample Test Scenarios
1. **Database Initialization** - Verify movies are loaded correctly
2. **API Integration** - Test movie search functionality
3. **Actor Search** - Validate local database queries
4. **Navigation** - Ensure smooth screen transitions
5. **Error Handling** - Test network failure scenarios

### Test Data
Use these search terms for testing:
- **Movies**: "Inception", "The Matrix", "Batman"
- **Actors**: "Leonardo DiCaprio", "Morgan Freeman", "Keanu Reeves"
- **Titles**: "Lord of the Rings", "Dark Knight", "Shawshank"

## 🐛 Troubleshooting

### Common Issues

**Build Errors**
- Ensure Android SDK is properly configured
- Check `local.properties` for correct SDK path
- Sync project with Gradle files

**API Issues**
- Verify internet connection
- Check API key validity (if using custom key)
- Review network security configuration

**Database Issues**
- Clear app data to reset database
- Verify Room schema migrations
- Check database initialization logs

## 📄 License

This project is created for educational purposes and demonstrates modern Android development practices.

## 🤝 Contributing

This is an educational project showcasing:
- Clean Architecture principles
- Modern Android development
- Jetpack Compose best practices
- MVVM pattern implementation
- API integration techniques

## 📞 Support

For questions or issues:
1. Check the troubleshooting section
2. Review the code documentation
3. Examine the sample data and test scenarios

---

**MovieKnowledge** - Discover movies with modern Android architecture 🎬✨ 