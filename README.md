FeedUs is a cross-platform, and open-source RSS client.

It is in the early stages of development and anyone is welcome to give suggestions and PR.

FeedUs contains code that is open sourced from [FeedMe](https://github.com/seazon/FeedMe).

## Screenshots
### Android
<img width="270" alt="ui-Android-login" src="https://github.com/user-attachments/assets/36c833e9-f402-4b00-ba9a-1bd52dafcad0" />

### macOS
```
./gradlew clean
./gradlew build
./gradlew run
```
<img width="801" alt="ui-macOS-login" src="https://github.com/user-attachments/assets/231b3316-9e38-4e95-842f-57d8e303a6be" />

## KMP
This is a Kotlin Multiplatform project targeting Android, iOS, Desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.
