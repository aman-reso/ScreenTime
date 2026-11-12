package com.app.screentime.feature.newdesign.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.screentime.feature.newdesign.R

/**
 * User roles determining the items displayed in the floating navigation bar.
 */
enum class WinterNavRole {
    USER,
    CREATOR
}

/**
 * Bottom navigation destinations matching Figma Node 122:6 ("Floating bottom navigation").
 */
enum class WinterNavItem(
    val title: String,
    @get:DrawableRes val iconRes: Int
) {
    DISCOVER("Discover", R.drawable.ic_bottom_nav_discover),
    WALLET("Wallet", R.drawable.ic_bottom_nav_wallet),
    EARNINGS("Earning", R.drawable.ic_bottom_nav_earning),
    CHATS("Chats", R.drawable.ic_bottom_nav_chats),
    CALLS("Calls", R.drawable.ic_bottom_nav_calls),
    PROFILE("Profile", R.drawable.ic_bottom_nav_profile)
}

object WinterFloatingNavDefaults {
    val Height: Dp = 56.dp
    val CornerRadius: Dp = 40.dp
    val SelectedWidth: Dp = 64.dp
    val SelectedHeight: Dp = 36.dp
    val SelectedCornerRadius: Dp = 20.dp
    val IconSize: Dp = 24.dp
}

/**
 * WinterFloatingNavigation - Redesigned to match Figma Node 122:6 ("Floating bottom navigation").
 *
 * Rules:
 * 1. Multiples of 4dp for all padding, margins, and sizes.
 * 2. Broken down into clean, modular composable functions.
 * 3. Uses dynamic color scheme tokens and typography.
 * 4. Never hardcode container heights arbitrarily (tokenized 56.dp standard bar height).
 */
@Composable
fun WinterFloatingNavigation(
    selectedItem: WinterNavItem,
    onItemSelected: (WinterNavItem) -> Unit,
    modifier: Modifier = Modifier,
    role: WinterNavRole = WinterNavRole.USER,
    items: List<WinterNavItem> = listOf(
        WinterNavItem.DISCOVER,
        WinterNavItem.WALLET,
        WinterNavItem.EARNINGS,
        WinterNavItem.CHATS,
        WinterNavItem.CALLS,
        WinterNavItem.PROFILE
    )
) {
    val barShape = RoundedCornerShape(WinterFloatingNavDefaults.CornerRadius)

    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = barShape,
                ambientColor = Color(0x212E3621),
                spotColor = Color(0x212E3621)
            )
            .height(WinterFloatingNavDefaults.Height)
            .clip(barShape)
            .background(WinterTokens.surfaceNavigation)
            .border(1.dp, WinterTokens.borderDefault, barShape)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                WinterFloatingNavItem(
                    item = item,
                    isSelected = item == selectedItem,
                    onSelect = { onItemSelected(item) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Individual navigation tab item (either selected active pill or unselected icon).
 */
@Composable
private fun WinterFloatingNavItem(
    item: WinterNavItem,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) WinterTokens.iconOnAccent else WinterTokens.iconSecondary,
        animationSpec = tween(durationMillis = 180),
        label = "NavItemColor"
    )

    Box(
        modifier = modifier
            .height(WinterFloatingNavDefaults.Height)
            .clickable(
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSelect
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            // Selected Pill Background (64dp x 36dp, rounded 20dp, #ACE2C6)
            Box(
                modifier = Modifier
                    .width(WinterFloatingNavDefaults.SelectedWidth)
                    .height(WinterFloatingNavDefaults.SelectedHeight)
                    .clip(RoundedCornerShape(WinterFloatingNavDefaults.SelectedCornerRadius))
                    .background(WinterTokens.actionPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = item.iconRes),
                    contentDescription = item.title,
                    tint = contentColor,
                    modifier = Modifier.size(WinterFloatingNavDefaults.IconSize)
                )
            }
        } else {
            // Unselected Icon
            Icon(
                painter = painterResource(id = item.iconRes),
                contentDescription = item.title,
                tint = contentColor,
                modifier = Modifier.size(WinterFloatingNavDefaults.IconSize)
            )
        }
    }
}

@Preview(name = "WinterFloatingNavigation - Discover Selected", showBackground = true)
@Composable
private fun WinterFloatingNavigationDiscoverPreview() {
    var selectedItem by remember { mutableStateOf(WinterNavItem.DISCOVER) }

    Surface(color = WinterTokens.backgroundCanvas) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            WinterFloatingNavigation(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
