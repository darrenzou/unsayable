# Unsayable

A simple Taboo-style word game for Android. The card shows a word to describe and
the words you're not allowed to say while describing it. Tap **Next card** for another one.

## Difficulty

Every card has five forbidden words.

- **Easy**: everyday things you can picture.
- **Hard**: trickier words and phrases.
- **Even Harder**: abstract ideas that are hard to pin down.

Cards don't repeat until you've seen every card at that difficulty.

## Building

Open the project in Android Studio, or from the command line:

```sh
./gradlew assembleDebug        # APK in app/build/outputs/apk/debug/
./gradlew testDebugUnitTest    # deck tests
```

Requires JDK 17+ and the Android SDK (compileSdk 35). Minimum Android version is 8.0 (API 26).
Every push and pull request is built by GitHub Actions, which uploads the debug APK as a build artifact.

## Code layout

- `game/Game.kt`: cards, difficulty levels and the shuffled deck (plain Kotlin, unit tested).
- `game/StarterDeck.kt`: the 125 built-in cards, tagged by difficulty.
- `GameViewModel.kt`: holds the chosen difficulty and the current card.
- `ui/`: the Jetpack Compose home and card screens.
