# TachiAZFork

A fast, lightweight Android reader fork that preserves the classic **Material Design 1 (sidebar + hamburger menu)** interface from the golden era of Tachiyomi, combined with modern extension repository support and features from TachiyomiAZ, EH, J2K, and SY.

## Highlights
- **Classic UI**: Material Design 1 navigation drawer instead of the bottom navigation bar.
- **External Extension Repositories**: Full support for third-party extension repos via JSON and modern Protobuf (`index.pb`) formats.
- **Deep Link Support**: Add extension repositories seamlessly with `tachiazfork://add-repo`, `tachiyomi://add-repo`, and `mihon://extension-store`.
- **Pure Android Focus**: Cleaned of legacy iOS experimental wrappers to optimize Gradle build times and performance on Android devices.
- **EH / ExH Features**: Integrated gallery features, tags, and specialized cookie authentication.
- **Trackers & Recommendations**: AniList and MyAnimeList integrations.

## Versioning
This fork maintains clean Semantic Versioning starting from **v1.0.0** (versionCode `1`).

## Compiling & Building

### In GitHub Actions (Recommended)
This repository includes configured CI/CD workflows:
- `.github/workflows/build.yml`: Automatically builds signed APK artifacts on pushes to `master`.
- `.github/workflows/release.yml`: Automatically publishes releases and APK assets when the `versionName` is bumped.

### Locally on your machine
Prerequisites:
- JDK 21 (Temurin / OpenJDK 21)
- Android SDK (API 34/35)

Run in terminal:
```bash
./gradlew assembleStandardDebug
```

## License
Licensed under Apache-2.0.
