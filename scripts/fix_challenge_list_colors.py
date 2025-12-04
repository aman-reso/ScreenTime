#!/usr/bin/env python3
"""
Script to replace colors.* references with scheme.* equivalents in ChallengeListScreen.kt
"""

import re
import sys

# Color mapping
COLOR_MAPPINGS = {
    r'colors\.background': 'scheme.basicBackground',
    r'colors\.textPrimary': 'scheme.basicText',
    r'colors\.textSecondary': 'scheme.basicTextRecessive',
    r'colors\.textMuted': 'scheme.basicTextRecessive',
    r'colors\.textOnPrimary': 'scheme.basicTextOnAccent',
    r'colors\.textLight': 'scheme.basicTextRecessive',
    r'colors\.accent': 'scheme.basicAccent',
    r'colors\.card': 'scheme.basicBackgroundCard',
    r'colors\.error': 'scheme.functionalDestructiveStandard',
    r'colors\.success': 'scheme.functionalSuccessStandard',
    r'colors\.border': 'scheme.basicStroke',
    r'colors\.white': 'scheme.basicTextOnAccent',
    r'colors\.black': 'scheme.basicText',
    r'colors\.gold': 'scheme.functionalWarningStandard',
    r'colors\.greyBackground': 'scheme.basicBackgroundSubtle',
    r'colors\.greySurface': 'scheme.basicBackgroundCard',
    r'colors\.greySurfaceLight': 'scheme.basicBackgroundSubtle',
    r'colors\.greyTextDark': 'scheme.basicText',
    r'colors\.greyBorder': 'scheme.basicStroke',
    r'colors\.greyTextMuted': 'scheme.basicTextRecessive',
    r'colors\.tint': 'scheme.basicAccent',
    r'colors\.blueMedium': 'scheme.functionalInformationalStandard',
    r'colors\.blueBackground': 'scheme.functionalInformationalSubtle',
    r'colors\.purple': 'scheme.basicAccent',
    r'colors\.purpleBackground': 'scheme.basicAccent.copy(alpha = 0.1f)',
    r'colors\.green': 'scheme.functionalSuccessStandard',
    r'colors\.greenMedium': 'scheme.functionalSuccessStandard',
    r'colors\.greenDark': 'scheme.functionalSuccessStandard',
    r'colors\.greenBackground': 'scheme.functionalSuccessSubtle',
    r'colors\.yellowBackground': 'scheme.functionalWarningSubtle',
    r'colors\.yellowBorder': 'scheme.functionalWarningStandard',
    r'colors\.amberTextDark': 'scheme.functionalWarningStandard',
    r'colors\.orange': 'scheme.functionalWarningStandard',
}

# HexColor patterns to fix
HEX_COLOR_PATTERNS = [
    (r'HexColor\(colors\.(\w+)\.value\)', r'scheme.\1'),
    (r'HexColor\(if\.value\)', r'if'),  # Fix syntax errors
    (r'HexColor\(colors\.(\w+)\.copy\.value\)', r'scheme.\1'),
]

def fix_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original_content = content
    
    # Fix HexColor patterns first
    for pattern, replacement in HEX_COLOR_PATTERNS:
        content = re.sub(pattern, replacement, content)
    
    # Replace colors.* references
    for pattern, replacement in COLOR_MAPPINGS.items():
        content = re.sub(pattern, replacement, content)
    
    # Fix specific patterns
    content = re.sub(r'HexColor\(scheme\.(\w+)\.value\)', r'scheme.\1', content)
    content = re.sub(r'HexColor\(scheme\.(\w+)\.copy\.value\)', r'scheme.\1', content)
    
    if content != original_content:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Fixed {file_path}")
        return True
    return False

if __name__ == '__main__':
    file_path = 'app/src/main/java/com/app/screentime/challenge/screen/ChallengeListScreen.kt'
    fix_file(file_path)

