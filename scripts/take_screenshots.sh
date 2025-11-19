#!/bin/bash

# Script to take Play Store compliant screenshots for ScreenTime app
# Make sure your device is connected via ADB and the app is installed

set -e

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Configuration
PACKAGE_NAME="com.app.screentime"
SCREENSHOT_DIR="screenshots"
ADB_PATH="/Users/amankumar/Library/Android/sdk/platform-tools/adb"

# Check if ADB is available
if [ ! -f "$ADB_PATH" ]; then
    echo -e "${RED}Error: ADB not found at $ADB_PATH${NC}"
    echo "Please update ADB_PATH in the script or ensure Android SDK is installed."
    exit 1
fi

# Check if device is connected
DEVICES=$($ADB_PATH devices | grep -v "List" | grep "device" | wc -l | tr -d ' ')
if [ "$DEVICES" -eq 0 ]; then
    echo -e "${RED}Error: No Android device connected${NC}"
    echo "Please connect your device via USB and enable USB debugging."
    exit 1
fi

echo -e "${GREEN}Found $DEVICES device(s) connected${NC}"

# Create screenshot directory
mkdir -p "$SCREENSHOT_DIR"

# Function to take screenshot
take_screenshot() {
    local screen_name=$1
    local filename="${SCREENSHOT_DIR}/${screen_name}.png"
    echo -e "${YELLOW}Taking screenshot: $screen_name${NC}"
    $ADB_PATH shell screencap -p > "$filename"
    
    # Wait a bit for the screen to render
    sleep 2
    
    if [ -f "$filename" ] && [ -s "$filename" ]; then
        echo -e "${GREEN}✓ Screenshot saved: $filename${NC}"
    else
        echo -e "${RED}✗ Failed to save screenshot: $filename${NC}"
    fi
}

# Function to wait for app to be ready
wait_for_app() {
    echo -e "${YELLOW}Waiting for app to be ready...${NC}"
    sleep 3
}

# Function to navigate using ADB
navigate() {
    local action=$1
    case $action in
        "home")
            $ADB_PATH shell input keyevent KEYCODE_HOME
            sleep 1
            ;;
        "back")
            $ADB_PATH shell input keyevent KEYCODE_BACK
            sleep 1
            ;;
        "tap")
            local x=$2
            local y=$3
            $ADB_PATH shell input tap $x $y
            sleep 2
            ;;
        "swipe")
            local x1=$2
            local y1=$3
            local x2=$4
            local y2=$5
            $ADB_PATH shell input swipe $x1 $y1 $x2 $y2
            sleep 2
            ;;
    esac
}

# Get device screen dimensions
echo -e "${YELLOW}Getting device screen dimensions...${NC}"
WIDTH=$($ADB_PATH shell wm size | cut -d' ' -f3 | cut -d'x' -f1)
HEIGHT=$($ADB_PATH shell wm size | cut -d' ' -f3 | cut -d'x' -f2)
echo -e "${GREEN}Screen size: ${WIDTH}x${HEIGHT}${NC}"

# Calculate tap positions (approximate, adjust based on your device)
CENTER_X=$((WIDTH / 2))
CENTER_Y=$((HEIGHT / 2))
BOTTOM_Y=$((HEIGHT - 100))
STATS_TAB_X=$((WIDTH / 3))
PROFILE_TAB_X=$((WIDTH * 2 / 3))

echo -e "${GREEN}Starting screenshot capture...${NC}"
echo ""

# 1. Launch the app
echo -e "${YELLOW}Step 1: Launching app...${NC}"
$ADB_PATH shell monkey -p "$PACKAGE_NAME" -c android.intent.category.LAUNCHER 1
wait_for_app

# 2. Permission screen (if shown)
echo -e "${YELLOW}Step 2: Permission screen${NC}"
take_screenshot "01_permission_screen"
echo "Please grant permissions manually if needed, then press Enter to continue..."
read -r

# 3. Home/Landing screen
echo -e "${YELLOW}Step 3: Home/Landing screen${NC}"
wait_for_app
take_screenshot "02_home_screen"

# 4. Statistics screen
echo -e "${YELLOW}Step 4: Statistics screen${NC}"
# Tap on Statistics tab (bottom navigation)
navigate "tap" "$STATS_TAB_X" "$BOTTOM_Y"
wait_for_app
take_screenshot "03_statistics_screen"

# 5. Profile screen
echo -e "${YELLOW}Step 5: Profile screen${NC}"
# Tap on Profile tab (bottom navigation)
navigate "tap" "$PROFILE_TAB_X" "$BOTTOM_Y"
wait_for_app
take_screenshot "04_profile_screen"

# 6. Search screen (if accessible from home)
echo -e "${YELLOW}Step 6: Search screen${NC}"
# Go back to home first
navigate "tap" "$CENTER_X" "$BOTTOM_Y"
wait_for_app
# Try to open search (you may need to adjust this based on your UI)
navigate "tap" "$CENTER_X" "100"
wait_for_app
take_screenshot "05_search_screen"
navigate "back"

# 7. App details screen (if accessible)
echo -e "${YELLOW}Step 7: App details screen${NC}"
# Go back to home
navigate "tap" "$CENTER_X" "$BOTTOM_Y"
wait_for_app
# Tap on an app item (you may need to adjust this)
navigate "tap" "$CENTER_X" "$((HEIGHT / 3))"
wait_for_app
take_screenshot "06_app_details_screen"
navigate "back"

# 8. Settings or additional screens
echo -e "${YELLOW}Step 8: Additional screens${NC}"
# Navigate to any additional important screens
# Add more navigation commands here as needed

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Screenshot capture completed!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "Screenshots saved in: $SCREENSHOT_DIR"
echo ""
echo "Next steps for Play Store:"
echo "1. Review screenshots and select the best ones"
echo "2. Ensure screenshots are at least 320px wide"
echo "3. Recommended sizes:"
echo "   - Phone: 1080x1920 (portrait) or 1920x1080 (landscape)"
echo "   - Tablet: 1600x2560 (portrait) or 2560x1600 (landscape)"
echo "4. You need at least 2 screenshots, maximum 8"
echo "5. Screenshots should showcase key features"
echo ""

