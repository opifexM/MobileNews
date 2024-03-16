# Mobile News

Mobile News is a mobile application designed to fetch, display, and manage news articles from various sources. This app uses Android Studio and Java to leverage the Android SDK for UI design and interaction.

## Description

Mobile News is an Android application designed to keep users informed by aggregating news from various sources. Developed in Java, it utilizes Android Studio for development, featuring a modern and intuitive user interface. The app retrieves news articles through a RESTful API, displaying them across categories such as Business, Entertainment, General, Health, Science, Sports, and Technology. Users can customize their news feed by selecting their preferred news categories within the settings. The application provides functionalities for users to view detailed article content for an extended reading experience.

## Features

-   **Category Selection**: Users can customize their news feed by selecting their preferred news categories.
-   **Live News Updates**: Fetches the latest news articles from the specified API based on user-selected categories.
-   **Archive Articles**: Saves articles locally using SQLite database.
-   **Detailed Article Viewing**: Presents articles in a detailed view, offering options to read directly within the app or in a web browser for the whole experience.

## Usage

Upon launching the app, users can select their preferred news categories from the settings menu. The main screen displays a list of articles fetched based on these preferences. Users can tap on any article to read the full content, mark articles as favorites, or save them for offline reading. The app also supports refreshing the news feed to fetch the latest articles.

## Technologies Used

-   **Java**: Primary programming language for application development.
-   **Android SDK**: Provides tools and APIs for building Android applications.
-   **SQLite**: Manages local database storage for articles and user preferences.
-   **Gson**: Facilitates JSON data parsing into Java objects and vice versa.
-   **HttpURLConnection**: Handles network requests to fetch news data from APIs.

## License

Mobile News is licensed under the MIT license.
