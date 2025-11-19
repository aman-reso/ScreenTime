#!/bin/bash

# ScreenTime - Update Ngrok URL Script
# This script extracts the ngrok URL and provides instructions to update it

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${GREEN}📋 Ngrok URL Update Helper${NC}"
echo ""
echo -e "${YELLOW}To get your ngrok URL:${NC}"
echo "  1. Start ngrok: ./scripts/start_ngrok.sh [PORT]"
echo "  2. Copy the 'Forwarding' URL (e.g., https://xxxxx.ngrok-free.app)"
echo "  3. Use one of the methods below to update it"
echo ""
echo -e "${BLUE}Method 1: Update Firebase Remote Config${NC}"
echo "  - Go to Firebase Console > Remote Config"
echo "  - Update 'api_base_url' parameter with your ngrok URL"
echo "  - The app will fetch it automatically on next launch"
echo ""
echo -e "${BLUE}Method 2: Update Code Directly${NC}"
echo "  Files to update:"
echo "    - app/src/main/java/com/app/screentime/network/ApiEndpoints.kt"
echo "    - app/src/main/java/com/app/screentime/config/RemoteConfigManager.kt"
echo ""
echo -e "${YELLOW}Example:${NC}"
echo "  If your ngrok URL is: https://abc123.ngrok-free.app"
echo "  Update DEFAULT_BASE_URL to: https://abc123.ngrok-free.app"
echo ""

# Try to get ngrok URL from ngrok API (if ngrok is running)
if command -v curl &> /dev/null; then
    NGROK_URL=$(curl -s http://localhost:4040/api/tunnels 2>/dev/null | grep -o '"public_url":"https://[^"]*' | head -1 | cut -d'"' -f4)
    if [ ! -z "$NGROK_URL" ]; then
        echo -e "${GREEN}✅ Detected ngrok URL: ${NGROK_URL}${NC}"
        echo ""
        echo -e "${YELLOW}Quick update command:${NC}"
        echo "  sed -i '' 's|https://[^/]*.ngrok-free.app|${NGROK_URL}|g' app/src/main/java/com/app/screentime/network/ApiEndpoints.kt"
        echo "  sed -i '' 's|https://[^/]*.ngrok-free.app|${NGROK_URL}|g' app/src/main/java/com/app/screentime/config/RemoteConfigManager.kt"
    else
        echo -e "${YELLOW}ℹ️  Ngrok doesn't appear to be running${NC}"
        echo "  Start it with: ./scripts/start_ngrok.sh"
    fi
fi

