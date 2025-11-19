# Screenshot Scripts for Play Store

This directory contains scripts to help you capture Play Store compliant screenshots for your ScreenTime app.

## Prerequisites

1. **Android device connected via USB** with USB debugging enabled
2. **ADB (Android Debug Bridge)** installed and in your PATH
3. **App installed** on the connected device

## Quick Start

### Option 1: Automated Script (Recommended)

Run the automated screenshot script:

```bash
./scripts/take_screenshots.sh
```

The script will:
- Check if a device is connected
- Launch your app
- Navigate through key screens
- Capture screenshots automatically
- Save them in the `screenshots/` directory

**Note:** You may need to manually grant permissions or navigate to certain screens during the process.

### Option 2: Manual Screenshots

If you prefer to take screenshots manually:

1. **Launch the app** on your device
2. **Navigate to each screen** you want to capture
3. **Take screenshot** using one of these methods:

#### Using ADB:
```bash
adb shell screencap -p > screenshots/manual_screenshot.png
```

#### Using Device:
- **Android**: Press Power + Volume Down buttons simultaneously
- **Screenshots are saved** to your device's gallery

#### Using Android Studio:
1. Open Android Studio
2. Go to **View → Tool Windows → Device File Explorer**
3. Navigate to `/sdcard/Pictures/Screenshots/`
4. Download screenshots from your device

## Play Store Requirements

### Screenshot Specifications

1. **Minimum Requirements:**
   - At least **2 screenshots** required
   - Maximum **8 screenshots** allowed
   - Minimum width: **320px**
   - Maximum file size: **8MB per image**

2. **Recommended Sizes:**
   - **Phone (Portrait):** 1080 x 1920 pixels
   - **Phone (Landscape):** 1920 x 1080 pixels
   - **Tablet (Portrait):** 1600 x 2560 pixels
   - **Tablet (Landscape):** 2560 x 1600 pixels

3. **Format:**
   - PNG or JPEG
   - 24-bit color depth
   - No transparency (for JPEG)

### Best Practices

1. **Showcase Key Features:**
   - Home screen with app usage stats
   - Statistics/analytics screens
   - Profile/settings screen
   - Any unique features (app blocking, VPN, etc.)

2. **Quality Guidelines:**
   - Use high-resolution screenshots
   - Ensure text is readable
   - Remove any personal/sensitive data
   - Show the app in its best state (with sample data)

3. **Content Guidelines:**
   - No misleading content
   - No references to other platforms (iOS, etc.)
   - No pricing information in screenshots
   - Follow Google Play content policies

## Screenshot Organization

After capturing screenshots, organize them:

```
screenshots/
├── phone/
│   ├── 01_home_screen.png
│   ├── 02_statistics_screen.png
│   ├── 03_profile_screen.png
│   └── ...
├── tablet/
│   └── ...
└── feature_phone/
    └── ...
```

## Troubleshooting

### Device Not Found
- Ensure USB debugging is enabled
- Check USB connection
- Run `adb devices` to verify connection

### Screenshots Are Black/Empty
- Wait a few seconds after navigating before taking screenshot
- Ensure the app is fully loaded
- Try taking screenshot manually first

### Wrong Screen Captured
- Adjust wait times in the script
- Manually navigate to the screen before taking screenshot
- Use manual screenshot method for specific screens

## Additional Resources

- [Google Play Console Screenshot Guidelines](https://support.google.com/googleplay/android-developer/answer/9866151)
- [Android Asset Studio](https://romannurik.github.io/AndroidAssetStudio/) - For creating promotional graphics
- [Play Store Listing Best Practices](https://support.google.com/googleplay/android-developer/answer/1078873)

