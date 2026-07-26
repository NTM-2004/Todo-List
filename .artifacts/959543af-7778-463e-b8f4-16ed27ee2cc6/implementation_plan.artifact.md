# Fix: Unable to instantiate appComponentFactory

The application is crashing with a `ClassNotFoundException` for `androidx.core.app.CoreComponentFactory`. This is a core AndroidX class used for dependency injection into components like Activities and Fragments. The failure to find it suggests a configuration issue in the build system or an unstable SDK/AGP version.

## User Review Required

> [!IMPORTANT]
> I am proposing to downgrade `compileSdk` and `targetSdk` from `36` to `35`. SDK 36 is likely a preview version and may be causing instability in the build tools or library resolution. API 35 (Android 15) is the current stable target.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///D:/College/Android/TodoList/gradle/libs.versions.toml)
- Update `agp` to a stable version `8.7.3`.
- Update `coreKtx` to `1.13.1` (a highly stable version).
- Update `appcompat` to `1.7.0`.
- Update `material` to `1.12.0`.
- Update `navigation` versions to `2.8.5`.

#### [MODIFY] [app/build.gradle.kts](file:///D:/College/Android/TodoList/app/build.gradle.kts)
- Change `compileSdk` to `35`.
- Change `targetSdk` to `35`.
- Add explicit `androidx.core:core` dependency to ensure `CoreComponentFactory` is included.

### Cleanup

- Perform a Gradle clean and rebuild to ensure all stale artifacts are removed.

## Verification Plan

### Automated Tests
- Run `./gradlew clean :app:assembleDebug` to verify the build completes without the `25.0.1` error.

### Manual Verification
- Deploy the app to a device/emulator and verify it launches without the `Unable to instantiate appComponentFactory` crash.
