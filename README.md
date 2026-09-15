# World Food Explorer

## Student Information

- Student: Nusaiba Islam Joyee
- Student ID: s8156519
- Unit: NIT3213 Mobile Application Development
- Campus: Brisbane
- Assessment: Final Assignment

## Application Overview

World Food Explorer is an Android application developed in Kotlin. It authenticates students through the university API and uses the keypass returned by the server to retrieve food entities.

Users can search for dishes, view complete food information, copy details and share dishes through other Android applications.

## Main Features

- Backend authentication using a POST request
- Login validation and error handling
- Loading indicators during API requests
- Dashboard data retrieved using the returned keypass
- RecyclerView displaying food entities
- Search by dish, country, ingredient or meal type
- Dynamic search-result count
- Complete selected-food Details screen
- Copy food information
- Share food information
- Logout confirmation
- Network retry option
- Terms and Conditions dialog
- Material 3 interface
- Hilt dependency injection
- Unit tests for Login and Dashboard ViewModels

## Application Screens

### Login Screen

The Login screen sends the entered credentials directly to the Brisbane authentication endpoint. Successful authentication returns a keypass and navigates the user to the Dashboard. Incorrect login details produce an error message.

### Dashboard Screen

The Dashboard retrieves food entities from the university API and displays them using a RecyclerView. Users can search the list, select a food, retry a failed request or log out.

### Details Screen

The Details screen displays all API information for the selected food:

- Dish name
- Origin
- Main ingredient
- Meal type
- Description

Users can also copy and share the selected food information.

## API Integration

Base URL: `https://nit3213apinew.onrender.com/`

Authentication endpoint: `POST /br/auth`

Dashboard endpoint: `GET /dashboard/{keypass}`

Postman was used only to test the API during development. The Android application communicates directly with the university API using Retrofit.

## Architecture and Technologies

- UI layer: Fragments, RecyclerView adapter and ViewModels
- Data layer: Models, Retrofit service and repository
- Dependency injection: Hilt
- Asynchronous operations: Kotlin Coroutines
- State management: StateFlow
- Navigation: Android Navigation Component
- View access: View Binding
- Interface design: Material 3

## Project Structure

- `data/model` — API data models
- `data/remote` — Retrofit API service
- `data/repository` — Repository interface and implementation
- `di` — Hilt dependency modules
- `ui/login` — Login screen and ViewModel
- `ui/dashboard` — Dashboard, RecyclerView adapter and ViewModel
- `ui/details` — Selected-food Details screen
- `FoodApplication.kt` — Hilt application class
- `MainActivity.kt` — Main activity and navigation host

## Testing

Unit tests were created for the LoginViewModel and DashboardViewModel.

The tests verify:

- Successful login returns the server keypass
- Failed login produces an error state
- Successful Dashboard request produces a food list
- Failed Dashboard request produces an error state

Windows test command: `.\gradlew.bat test`

## How to Run the Application

1. Download or clone the project.
2. Open the project folder in Android Studio.
3. Allow Gradle synchronization to finish.
4. Select an emulator or physical Android device.
5. Click Run.
6. Enter the assigned university credentials.
7. Browse the foods and select a dish to view its details.

## Security Note

Login credentials are sent to the university authentication API. They are not permanently stored or hard-coded inside the application.

## Git Usage

Git was used to record important development milestones, including project setup, networking, dependency injection, interface development, testing and documentation.

## Author

Nusaiba Islam Joyee  
Student ID: s8156519