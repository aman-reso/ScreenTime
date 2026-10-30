package com.app.screentime.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.organisms.bottomnavigation.ODSBottomNavigation
import com.telekom.odsystem.organisms.bottomnavigation.ODSBottomNavigationItemProps
import com.telekom.odsystem.organisms.bottomnavigation.ODSBottomNavigationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ScreenTimeBottomNavigation(
    scheme: ODSTheme,
    selectedIndex: Int = 0,
    onTabSelected: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val items = bottomNavTabs.mapIndexed { index, tab ->
        val isSelected = selectedIndex == index
        ODSBottomNavigationItemProps(
            active = isSelected,
            disabled = false,
            icon = ODSIconModel(
                drawableRes = tab.iconRes ?: 0,
                tint = if (isSelected) scheme.basicAccent else scheme.basicTextRecessive
            ),
            iconActive = ODSIconModel(
                drawableRes = tab.iconRes ?: 0,
                tint = scheme.basicAccent
            ),
            text = tab.label
        )
    }

    ODSBottomNavigation(
        modifier = modifier
            .fillMaxWidth()
            .background(scheme.basicBackground.getColor())
            .navigationBarsPadding(),
        scheme = scheme,
        props = ODSBottomNavigationProps(
            items = items,
            labels = true
        ),
        selectedIndex = selectedIndex,
        onIndexChanged = onTabSelected
    )
}
