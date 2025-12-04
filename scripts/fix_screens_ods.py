#!/usr/bin/env python3
"""
Script to replace non-ODS components with ODS components in screen files.
"""
import re
from pathlib import Path

# Screen files to process
screen_files = [
    "app/src/main/java/com/app/screentime/landing/screen/LandingScreen.kt",
    "app/src/main/java/com/app/screentime/record/screen/RecordDetailScreen.kt",
    "app/src/main/java/com/app/screentime/leaderboard/screen/LeaderboardScreen.kt",
    "app/src/main/java/com/app/screentime/focus/screen/FocusModeScreen.kt",
    "app/src/main/java/com/app/screentime/consent/screen/ConsentScreen.kt",
    "app/src/main/java/com/app/screentime/challenge/screen/ChallengeListScreen.kt",
    "app/src/main/java/com/app/screentime/challenge/screen/ChallengesScreen.kt",
]

# Common replacements (be careful with these - they're context-dependent)
replacements = [
    # Box -> ODSBox (but need to handle background, padding separately)
    # Row -> ODSRow
    # Column -> ODSColumn
    # Icon -> ODSIcon
    # IconButton -> ODSButton
    # Button -> ODSButton
    # TextButton -> ODSButton with GHOST variant
]

def process_file(file_path: Path):
    """Process a single file."""
    if not file_path.exists():
        print(f"File not found: {file_path}")
        return False
    
    try:
        content = file_path.read_text(encoding='utf-8')
        original = content
        
        # Add ODS imports if not present
        if 'import com.telekom.odsystem.atoms.ODSBox' not in content:
            # Find last import line
            lines = content.split('\n')
            last_import_idx = 0
            for i, line in enumerate(lines):
                if line.startswith('import '):
                    last_import_idx = i
            
            # Add ODS imports after last import
            ods_imports = [
                'import com.telekom.odsystem.atoms.ODSBox',
                'import com.telekom.odsystem.atoms.ODSColumn',
                'import com.telekom.odsystem.atoms.ODSRow',
                'import com.telekom.odsystem.atoms.button.ODSButton',
                'import com.telekom.odsystem.atoms.button.ODSButtonProps',
                'import com.telekom.odsystem.atoms.button.ODSButtonVariant',
                'import com.telekom.odsystem.atoms.icon.ODSIcon',
                'import com.telekom.odsystem.atoms.icon.ODSIconModel',
                'import com.telekom.odsystem.foundations.ODSColorModel',
                'import com.telekom.odsystem.foundations.ODSPadding',
            ]
            
            for imp in ods_imports:
                if imp not in content:
                    lines.insert(last_import_idx + 1, imp)
                    last_import_idx += 1
        
        content = '\n'.join(lines)
        
        if content != original:
            file_path.write_text(content, encoding='utf-8')
            print(f"Updated: {file_path}")
            return True
        return False
    except Exception as e:
        print(f"Error processing {file_path}: {e}")
        return False

if __name__ == '__main__':
    base_dir = Path(__file__).parent.parent
    updated = 0
    
    for file_path_str in screen_files:
        file_path = base_dir / file_path_str
        if process_file(file_path):
            updated += 1
    
    print(f"\nTotal files updated: {updated}")

