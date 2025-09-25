# Lock Screen Demo - Android App

A demonstration Android application that showcases a polished welcome animation followed by a simulated lock screen interface requiring PIN entry.

## What This App Does

This app demonstrates:
- **Splash Screen**: Smooth, animated "Hello" text with particle effects and gradient background
- **Permission Handling**: Transparent permission requests with clear explanations
- **Lock Screen Simulation**: Fullscreen PIN entry interface with Material3 design
- **Security Demo**: Simulated lock screen requiring PIN `123456` for access
- **Modern UI**: Built with Jetpack Compose and Material3 theming

## Features

- 🎨 **Polished Animations**: Smooth welcome screen with fade-in, scale, and particle effects
- 🔒 **PIN Lock Screen**: Realistic lock screen interface with haptic feedback
- 📱 **Material3 Design**: Modern UI with gradient backgrounds and smooth transitions
- ⚡ **Permission Management**: Clear permission explanations and proper Android permission flow
- 🔐 **Demo Security**: Safe simulation that doesn't compromise device security
- ⏰ **Real-time Clock**: Live time display on the lock screen
- 📳 **Haptic Feedback**: Vibration feedback on PIN keypad interactions

## Build Instructions

### Prerequisites
- Android Studio (latest stable version)
- Android SDK 34 or higher
- Java 8 or higher

### Local Development Setup

1. **Download the Project**
   ```bash
   # Clone or download this project to your local machine
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the project folder and select it
   - Wait for Gradle sync to complete

3. **Build the APK**
   
   **Option A: Using Android Studio GUI**
   - Go to `Build` → `Build Bundle(s)/APK(s)` → `Build APK(s)`
   - Wait for the build to complete
   
   **Option B: Using Command Line**
   ```bash
   # Navigate to project directory
   cd LockScreenDemo
   
   # Make gradlew executable (Linux/Mac)
   chmod +x gradlew
   
   # Build debug APK
   ./gradlew assembleDebug
   
   # On Windows, use:
   # gradlew.bat assembleDebug
   ```

4. **Locate the APK**
   - The built APK will be available at: `app/build/outputs/apk/debug/app-debug.apk`
   - You can install this APK on any Android device you own

### Installation on Device

1. Enable "Unknown Sources" or "Install Unknown Apps" in your device settings
2. Transfer the APK file to your device
3. Open the APK file and follow installation prompts
4. Grant the requested permissions when prompted

## Permissions Requested

This app requests the following permissions with clear explanations:

| Permission | Purpose | Required |
|------------|---------|-----------|
| `INTERNET` | For potential demo features (optional) | No |
| `WAKE_LOCK` | Keep screen awake during PIN entry demonstration | Yes |
| `VIBRATE` | Provide haptic feedback on PIN keypad | Yes |

All permissions are requested transparently through the standard Android permission system. The app explains why each permission is needed before requesting it.

## How to Use

1. **Launch the App**: Open the installed app
2. **Watch the Animation**: Enjoy the smooth "Hello" welcome animation
3. **Grant Permissions**: Review and grant the requested permissions
4. **Enter PIN**: Use the PIN keypad to enter `123456`
5. **Experience Success**: See the success screen and exit

## Technical Details

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Design System**: Material3
- **Target SDK**: 34
- **Minimum SDK**: 24 (Android 7.0+)
- **Architecture**: Single Activity with Compose Navigation

## Project Structure

```
app/
├── src/main/
│   ├── java/com/example/lockscreendemo/
│   │   ├── MainActivity.kt          # Splash screen and permissions
│   │   ├── LockActivity.kt          # PIN lock screen interface
│   │   └── ui/theme/               # Material3 theming
│   ├── res/
│   │   ├── values/                 # Strings, colors, themes
│   │   ├── drawable/               # App icons and graphics
│   │   └── xml/                    # Backup and data extraction rules
│   └── AndroidManifest.xml         # App configuration and permissions
├── build.gradle                    # Module-level build configuration
└── proguard-rules.pro             # ProGuard configuration
```

## Safety and Ethics Notice

**⚠️ IMPORTANT SECURITY NOTICE**

- **Device Ownership Required**: Only install and run this APK on devices you own
- **Demonstration Purpose**: This app simulates a lock screen for demo/educational purposes only
- **No System-Level Security**: This app does NOT provide actual device security or replace your system lock screen
- **Safe Exit Methods**: You can always exit using:
  - Device power button (normal system lock)
  - Home button or navigation gestures
  - Recent apps switcher
  - Device restart if needed
- **No Data Collection**: This app does not collect, store, or transmit any personal data
- **No System Modification**: The app does not modify system settings or request device administrator privileges

This demonstration app is designed to be educational and safe. It operates within the Android app sandbox and cannot compromise your device's security.

## Troubleshooting

### Build Issues
- Ensure you have the latest Android Studio and Android SDK
- Check that your device meets the minimum SDK requirements (API 24+)
- Try cleaning and rebuilding: `./gradlew clean assembleDebug`

### Permission Issues
- If permissions are denied, the app will explain why they're needed
- You can manually grant permissions in Settings → Apps → Lock Screen Demo → Permissions

### Installation Issues
- Enable "Install Unknown Apps" for your file manager or browser
- Ensure you're installing on a device you own
- Check that the device meets minimum Android version requirements (7.0+)

## Development Dependencies

```gradle
dependencies {
    // Core Android libraries
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.7.0'
    implementation 'androidx.activity:activity-compose:1.8.2'
    
    // Jetpack Compose
    implementation platform('androidx.compose:compose-bom:2023.10.01')
    implementation 'androidx.compose.ui:ui'
    implementation 'androidx.compose.material3:material3'
    implementation 'androidx.compose.animation:animation:1.5.4'
    
    // Permissions handling
    implementation 'com.google.accompanist:accompanist-permissions:0.32.0'
    implementation 'com.google.accompanist:accompanist-systemuicontroller:0.32.0'
}
```

## License

This project uses open-source libraries and is intended for educational and demonstration purposes. 

### Third-Party Libraries
- **Jetpack Compose**: Apache License 2.0
- **Material Design Components**: Apache License 2.0
- **Accompanist Libraries**: Apache License 2.0

All dependencies are properly included in the Gradle build files and their licenses are respected.

---

**Created with Android Studio and Jetpack Compose**  
**Target Audience**: Android developers, security researchers, educational purposes  
**PIN for Demo**: `123456`