#!/bin/bash

# ScreenTime - Ngrok Deployment Script
# This script starts ngrok to expose your local API server

# Configuration
LOCAL_PORT=${1:-8080}  # Default port 8080, can be overridden as first argument
NGROK_AUTH_TOKEN=${NGROK_AUTH_TOKEN:-""}  # Set your ngrok auth token as environment variable

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}🚀 Starting Ngrok for ScreenTime API${NC}"
echo -e "${YELLOW}Local Port: ${LOCAL_PORT}${NC}"
echo ""

# Check if ngrok is installed
if ! command -v ngrok &> /dev/null; then
    echo -e "${RED}❌ Error: ngrok is not installed${NC}"
    echo ""
    echo "Please install ngrok:"
    echo "  macOS: brew install ngrok/ngrok/ngrok"
    echo "  Linux: https://ngrok.com/download"
    echo "  Or visit: https://ngrok.com/download"
    exit 1
fi

# Check if auth token is set
if [ -z "$NGROK_AUTH_TOKEN" ]; then
    echo -e "${YELLOW}⚠️  Warning: NGROK_AUTH_TOKEN not set${NC}"
    echo "You can set it with: export NGROK_AUTH_TOKEN=your_token"
    echo "Or add it to your ~/.zshrc or ~/.bashrc"
    echo ""
    read -p "Do you want to continue without auth token? (y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
else
    ngrok config add-authtoken "$NGROK_AUTH_TOKEN" 2>/dev/null
fi

# Check if port is in use
if ! lsof -Pi :$LOCAL_PORT -sTCP:LISTEN -t >/dev/null 2>&1 ; then
    echo -e "${YELLOW}⚠️  Warning: Nothing is listening on port ${LOCAL_PORT}${NC}"
    echo "Make sure your API server is running on port ${LOCAL_PORT}"
    echo ""
    read -p "Do you want to continue anyway? (y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

echo -e "${GREEN}Starting ngrok tunnel...${NC}"
echo ""
echo -e "${YELLOW}📝 Note:${NC}"
echo "  - The ngrok URL will be displayed below"
echo "  - Update Firebase Remote Config with the new URL"
echo "  - Or update DEFAULT_BASE_URL in ApiEndpoints.kt"
echo "  - Press Ctrl+C to stop ngrok"
echo ""

# Start ngrok
ngrok http $LOCAL_PORT --log=stdout

