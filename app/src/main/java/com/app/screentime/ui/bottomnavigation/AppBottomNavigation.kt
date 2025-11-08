package com.app.screentime.ui.bottomnavigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Navigation item data class
 */
data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector? = null,
    val route: String,
    val badge: Int? = null
)

/**
 * Common App Bottom Navigation Component
 * @param items List of navigation items
 * @param selectedRoute Currently selected route
 * @param onItemClick Callback when an item is clicked
 * @param modifier Modifier for the navigation bar
 */
@Composable
fun AppBottomNavigation(
    items: List<NavigationItem>,
    selectedRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        items.forEach { item ->
            val isSelected = selectedRoute == item.route
            val iconToShow = if (isSelected && item.selectedIcon != null) {
                item.selectedIcon
            } else {
                item.icon
            }
            
            NavigationBarItem(
                icon = {
                    BadgedBox(badge = { item.badge?.let { Badge { Text("$it") } } }) {
                        Icon(
                            imageVector = iconToShow,
                            contentDescription = item.label
                        )
                    }
                },
                label = { Text(item.label) },
                selected = isSelected,
                onClick = { onItemClick(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

/**
 * Alternative bottom navigation with custom styling
 */
@Composable
fun CustomBottomNavigation(
    items: List<NavigationItem>,
    selectedRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surface,
    contentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    selectedIconColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    selectedTextColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    unselectedIconColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurfaceVariant,
    unselectedTextColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    NavigationBar(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        items.forEach { item ->
            val isSelected = selectedRoute == item.route
            val iconToShow = if (isSelected && item.selectedIcon != null) {
                item.selectedIcon
            } else {
                item.icon
            }
            
            NavigationBarItem(
                icon = {
                    BadgedBox(badge = { item.badge?.let { Badge { Text("$it") } } }) {
                        Icon(
                            imageVector = iconToShow,
                            contentDescription = item.label,
                            tint = if (isSelected) selectedIconColor else unselectedIconColor
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (isSelected) selectedTextColor else unselectedTextColor
                    )
                },
                selected = isSelected,
                onClick = { onItemClick(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedIconColor,
                    selectedTextColor = selectedTextColor,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

/**
 * Floating action button style bottom navigation
 */
@Composable
fun FloatingBottomNavigation(
    items: List<NavigationItem>,
    selectedRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            NavigationBar(
                modifier = Modifier.height(64.dp),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                items.forEach { item ->
                    val isSelected = selectedRoute == item.route
                    val iconToShow = if (isSelected && item.selectedIcon != null) {
                        item.selectedIcon
                    } else {
                        item.icon
                    }
                    
                    NavigationBarItem(
                        icon = {
                            BadgedBox(badge = { item.badge?.let { Badge { Text("$it") } } }) {
                                Icon(
                                    imageVector = iconToShow,
                                    contentDescription = item.label
                                )
                            }
                        },
                        label = { Text(item.label) },
                        selected = isSelected,
                        onClick = { onItemClick(item.route) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    }
}
