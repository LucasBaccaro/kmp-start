# Kotlin Multiplatform project targeting Android, iOS.

- [iOS](#ios)
> If you need to run the app on iOS and you have a mac. Open the iosApp folder in xcode and you can run the app.
- [Android](#android)
> If you need to run the app on Android. Open the project in Android studio and you can run the app.

## Libraries Used
- **Ktor:** Used for handling HTTP requests and networking operations.
- **Koin:** A lightweight dependency injection framework.
- **Image-Loader:** Efficiently loads and displays images.
- **Navigation Compose and Safe Ars** Utilized for navigation in a multiplatform environment.
- **Ubication** Native implementation with cocoapods
- **Arquitecture** MVVM architecture
- **UI** Share UI with Compose Multiplatform.

## iOS
<table>
  <tr>
    <td align="center"><img src="images/ios/ios_news_home.png" width="200"></td>
    <td align="center"><img src="images/ios/ios_new_detail.png" width="200"></td>
    <td align="center"><img src="images/ios/ios_users_home.png" width="200"></td>
    <td align="center"><img src="images/ios/ios_users_ubication.png" width="200"></td>

  </tr>
</table>

## Android
<table>
  <tr>
    <td align="center"><img src="images/android/android_news_home.png" width="200"></td>
    <td align="center"><img src="images/android/android_news_detail.png" width="200"></td>
    <td align="center"><img src="images/android/android_users_home.png" width="200"></td>
    <td align="center"><img src="images/android/android_users_ubication.png" width="200"></td>

  </tr>
</table>


* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* `/server` is for the Ktor server application.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here too.