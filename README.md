FeedUs is a cross-platform, and open-source RSS client.

It is in the early stages of development and anyone is welcome to give suggestions and PR.

FeedUs contains code that is open sourced from [FeedMe](https://github.com/seazon/FeedMe).

## Screenshots
### Android
<img width="250" alt="ui-Android-login" src="./docs/imgs/android-login.png" /><img width="250" alt="ui-Android-feeds" src="./docs/imgs/android-feeds.png" /><img width="250" alt="ui-Android-articles" src="./docs/imgs/android-articles.png" />

### iOS
<img width="250" alt="ui-iOS-login" src="./docs/imgs/ios-login.png" /><img width="250" alt="ui-iOS-feeds" src="./docs/imgs/ios-feeds.png" /><img width="250" alt="ui-iOS-articles" src="./docs/imgs/ios-articles.png" />

### macOS / Windows / Linux
```
./gradlew clean
./gradlew build
./gradlew run
```
<img width="250" alt="ui-macOS-login" src="./docs/imgs/macos-login.png" /><img width="250" alt="ui-macOS-feeds" src="./docs/imgs/macos-feeds.png" />

<img width="250" alt="ui-Windows-login" src="./docs/imgs/windows-login.jpg" /><img width="250" alt="ui-Windows-feeds" src="./docs/imgs/windows-feeds.jpg" /><img width="250" alt="ui-Windows-articles" src="./docs/imgs/windows-articles.jpg" />

<img width="250" alt="ui-Linux-login" src="./docs/imgs/linux-login.png" /><img width="250" alt="ui-Linux-feeds" src="./docs/imgs/linux-feeds.png" />

## Progress
| function     | Android | iOS | macOS | Windows | Linux |
|--------------|---------|-----|-------|---------|------|
| normal login | ✅      | ✅  | ✅     | ✅      | ✅    |
| auth login   | ✅      | ❌  | ❌     | ❌      | ❌    |
| sync         | ✅      | ✅  | ✅     | ✅      | ✅    |
| subscribe    | ✅      | ✅  | ✅     | ✅      | ❓    |
| mark read    | ✅      | ✅  | ✅     | ✅      | ❓    |

## API Support
|  | Support Unread Count API | Feed ID Star with ”Feed” | Support Subscribe API | Support Tag API | Support Star API | Support Fetch by Feed / Category | Support Fetch IDs and then Stream | Support pagination | Support podcast |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| [Folo](https://api.follow.is/reference) | ✅ | N | ✅ | ❌ | ✅ | ✅ | ❌ | ✅ | ✅ |
| [Fever API](https://blog.badguy.top/index.php/archives/294/) (Miniflux / CommaFeed) | ❌ | N | ❌ | ✅ | ✅ | ❌ | ✅ | ❌ | ❌ |
| Tiny Tiny RSS | ✅ | N | ✅ | ❌ | ✅ | ✅ | ✅ | ✅ | ✅ |
| Feedly | ✅ | Y | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| Feedbin | ❌ | N | ✅ | ✅ | ✅ | ❌ | ✅ | ❌ | ❌ |
| Google Reader API ( Inoreader / The Old Reader / FreshRSS / BazQux)| ✅ | Y | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
