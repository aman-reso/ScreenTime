#!/usr/bin/env python3
"""
Script to fix invalid scheme property references in ChallengeListScreen.kt
"""

import re

# Invalid scheme property mappings
SCHEME_FIXES = {
    r'scheme\.textPrimary': 'scheme.basicText',
    r'scheme\.textSecondary': 'scheme.basicTextRecessive',
    r'scheme\.white': 'scheme.basicTextOnAccent',
    r'scheme\.black': 'scheme.basicText',
    r'scheme\.accent': 'scheme.basicAccent',
    r'scheme\.success': 'scheme.functionalSuccessStandard',
    r'scheme\.greyTextDark': 'scheme.basicText',
    r'scheme\.amberTextDark': 'scheme.functionalWarningStandard',
    r'scheme\.basicAccentBackground': 'scheme.basicAccent.copy(alpha = 0.1f)',
    r'scheme\.functionalSuccessStandardMedium': 'scheme.functionalSuccessStandard',
    r'scheme\.basicBackgroundCardLight': 'scheme.basicBackgroundCard',
    r'scheme\.functionalSuccessStandardBackground': 'scheme.functionalSuccessSubtle',
    r'scheme\.greenDark': 'scheme.functionalSuccessStandard',
}

# Fix invalid function call syntax
FUNCTION_FIXES = [
    (r'scheme\.white\(alpha\s*=\s*([\d.]+f?)\)', r'scheme.basicTextOnAccent.copy(alpha = \1)'),
    (r'scheme\.success\(alpha\s*=\s*([\d.]+f?)\)', r'scheme.functionalSuccessStandard.copy(alpha = \1)'),
]

def fix_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original_content = content
    
    # Fix function call syntax first
    for pattern, replacement in FUNCTION_FIXES:
        content = re.sub(pattern, replacement, content)
    
    # Fix invalid scheme properties
    for pattern, replacement in SCHEME_FIXES.items():
        content = re.sub(pattern, replacement, content)
    
    if content != original_content:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Fixed {file_path}")
        return True
    return False

if __name__ == '__main__':
    file_path = 'app/src/main/java/com/app/screentime/challenge/screen/ChallengeListScreen.kt'
    fix_file(file_path)

















