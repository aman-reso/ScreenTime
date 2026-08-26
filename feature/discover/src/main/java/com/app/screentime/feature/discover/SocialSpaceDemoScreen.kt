package com.app.screentime.feature.discover

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.components.PompiereTitle
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.*
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.orchidSecondaryScheme

// Vivid pink accent from reference screenshot
private val NeonPink = Color(0xFFE20074)
private val NeonPinkGradient = Brush.horizontalGradient(listOf(Color(0xFFFF1493), Color(0xFFE20074)))
private val DarkSurfaceColor = Color(0xFF16171B)
private val DarkBgColor = Color(0xFF0F1014)

enum class SocialDemoTab {
    FEED, PROFILE, ONBOARDING
}

@Composable
fun SocialSpaceDemoScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onBackClick: () -> Unit = {}
) {
    var activeDemoTab by remember { mutableStateOf(SocialDemoTab.FEED) }

    ODSColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBgColor)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Switcher Bar (Demo Navigator)
        ODSRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.navigation_left_type_standard_size_standard),
                    tint = Color.White
                )
            }

            // Demo Mode Segmented Pill
            ODSRow(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurfaceColor)
                    .padding(3.dp),
                gap = 2.dp
            ) {
                listOf(
                    SocialDemoTab.FEED to "Feed",
                    SocialDemoTab.PROFILE to "Profile",
                    SocialDemoTab.ONBOARDING to "Intro"
                ).forEach { (tab, label) ->
                    val isSelected = activeDemoTab == tab
                    ODSBox(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) NeonPink else Color.Transparent)
                            .clickable { activeDemoTab = tab }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            text = label,
                            style = ODSTextStyles.microcopyBold,
                            color = HexColor(0xffffffff)
                        )
                    }
                }
            }

            Spacer(Modifier.size(40.dp))
        }

        // Active View
        AnimatedContent(
            targetState = activeDemoTab,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "demo_content"
        ) { tab ->
            when (tab) {
                SocialDemoTab.FEED -> SocialFeedView(
                    scheme = scheme,
                    onNavigateProfile = { activeDemoTab = SocialDemoTab.PROFILE }
                )
                SocialDemoTab.PROFILE -> SocialProfileView(
                    scheme = scheme,
                    onBack = { activeDemoTab = SocialDemoTab.FEED }
                )
                SocialDemoTab.ONBOARDING -> SocialOnboardingView(
                    onGetStarted = { activeDemoTab = SocialDemoTab.FEED }
                )
            }
        }
    }
}

/**
 * 1. Feed View (Center Phone in Screenshot)
 */
@Composable
private fun SocialFeedView(
    scheme: ODSTheme,
    onNavigateProfile: () -> Unit
) {
    var isLiked by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Header: Menu, "Home", Notification Badge, Search
            item {
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSBox(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceColor),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(imageVector = Icons.Filled.Menu),
                            tint = Color.White
                        )
                    }

                    PompiereTitle(
                        text = "Home",
                        scheme = scheme,
                        style = ODSTextStyles.pompiereHeader
                    )

                    ODSRow(gap = 10.dp) {
                        // Notification badge pill
                        ODSRow(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(DarkSurfaceColor)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            gap = 4.dp
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(imageVector = Icons.Filled.Notifications),
                                tint = Color.White
                            )
                            ODSText(
                                text = "3",
                                style = ODSTextStyles.microcopyBold,
                                color = HexColor(0xffffffff)
                            )
                        }

                        // Search Button
                        ODSBox(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceColor),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(imageVector = Icons.Filled.Search),
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            // Stories Avatar Row
            item {
                Spacer(Modifier.height(8.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val stories = listOf(
                        Triple("You", true, "Y"),
                        Triple("Johan", false, "J"),
                        Triple("Nicole", false, "N"),
                        Triple("North", false, "N"),
                        Triple("Smith", false, "S")
                    )

                    items(stories) { (name, isUser, initial) ->
                        ODSColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            gap = 4.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                ODSBox(
                                    modifier = Modifier
                                        .size(58.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, NeonPink, CircleShape)
                                        .padding(3.dp)
                                        .clip(CircleShape)
                                        .background(DarkSurfaceColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ODSText(
                                        text = initial,
                                        style = ODSTextStyles.pompiereTitleS,
                                        color = HexColor(0xffffffff)
                                    )
                                }

                                if (isUser) {
                                    ODSBox(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(NeonPink),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        ODSIcon(
                                            iconModel = ODSIconModel(imageVector = Icons.Filled.Add),
                                            tint = Color.White
                                        )
                                    }
                                }
                            }

                            ODSText(
                                text = name,
                                style = ODSTextStyles.microcopyRegular,
                                color = HexColor(0xffcacaca)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(14.dp))
            }

            // Main Live Video Post Card
            item {
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    background = listOf(ODSColorModel(hexColor = HexColor(0xff16171b))),
                    cornerRadius = ODSCorners(all = 16.dp),
                    border = ODSBorder(1.dp, listOf(ODSColorModel(hexColor = HexColor(0xff2a2b30)))),
                    padding = ODSPadding(all = 12.dp)
                ) {
                    ODSColumn(gap = 12.dp) {
                        // Hero Image with Live Badge & Viewers
                        ODSBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(orchidSecondaryScheme.basicBackgroundSubtle.getColor()),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(
                                text = "✨",
                                style = ODSTextStyles.pompiereDisplayL,
                                color = HexColor(0xffffffff)
                            )

                            // Top Left LIVE Badge
                            ODSRow(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(12.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(NeonPink)
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                gap = 4.dp
                            ) {
                                ODSBox(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                ) {}
                                ODSText(
                                    text = "LIVE",
                                    style = ODSTextStyles.microcopyBold,
                                    color = HexColor(0xffffffff)
                                )
                            }

                            // Top Right Viewers Pill
                            ODSRow(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x99000000))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                gap = 4.dp
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(imageVector = Icons.Filled.Person),
                                    tint = Color.White
                                )
                                ODSText(
                                    text = "30.5k",
                                    style = ODSTextStyles.microcopyRegular,
                                    color = HexColor(0xffffffff)
                                )
                            }
                        }

                        // Post Engagement Action Bar
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ODSRow(gap = 16.dp, verticalAlignment = Alignment.CenterVertically) {
                                // Like
                                ODSRow(
                                    modifier = Modifier.clickable { isLiked = !isLiked },
                                    gap = 4.dp,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ODSIcon(
                                        iconModel = ODSIconModel(imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder),
                                        tint = if (isLiked) NeonPink else Color.White
                                    )
                                    ODSText(text = "8.2k", style = ODSTextStyles.microcopyBold, color = HexColor(0xffffffff))
                                }

                                // Comment
                                ODSRow(gap = 4.dp, verticalAlignment = Alignment.CenterVertically) {
                                    ODSIcon(
                                        iconModel = ODSIconModel(imageVector = Icons.Outlined.ChatBubbleOutline),
                                        tint = Color.White
                                    )
                                    ODSText(text = "80", style = ODSTextStyles.microcopyBold, color = HexColor(0xffffffff))
                                }

                                // Share
                                ODSRow(gap = 4.dp, verticalAlignment = Alignment.CenterVertically) {
                                    ODSIcon(
                                        iconModel = ODSIconModel(imageVector = Icons.Filled.Share),
                                        tint = Color.White
                                    )
                                    ODSText(text = "240", style = ODSTextStyles.microcopyBold, color = HexColor(0xffffffff))
                                }
                            }

                            // Bookmark
                            ODSRow(
                                modifier = Modifier.clickable { isBookmarked = !isBookmarked },
                                gap = 4.dp,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder),
                                    tint = if (isBookmarked) NeonPink else Color.White
                                )
                                ODSText(text = "5.8k", style = ODSTextStyles.microcopyBold, color = HexColor(0xffffffff))
                            }
                        }

                        // Creator Caption
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = onNavigateProfile),
                            gap = 8.dp,
                            verticalAlignment = Alignment.Top
                        ) {
                            ODSBox(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(DarkSurfaceColor),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSText(text = "N", style = ODSTextStyles.microcopyBold, color = HexColor(0xffffffff))
                            }
                            ODSColumn(gap = 2.dp) {
                                ODSText(
                                    text = "North",
                                    style = ODSTextStyles.bodySBold,
                                    color = HexColor(0xffffffff)
                                )
                                ODSText(
                                    text = "Let's begin the photoshop war to beat some AI tool where they can't feel...",
                                    style = ODSTextStyles.microcopyRegular,
                                    color = HexColor(0xffcacaca),
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }

        // Floating Bottom Dock
        FloatingSocialDock(
            activeTab = "Home",
            onTabClick = { if (it == "Profile") onNavigateProfile() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

/**
 * 2. Creator Profile View (Right Phone in Screenshot)
 */
@Composable
private fun SocialProfileView(
    scheme: ODSTheme,
    onBack: () -> Unit
) {
    var isFollowing by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            // Gradient Header with Profile Info
            item {
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NeonPinkGradient)
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    ODSColumn(gap = 14.dp) {
                        // Top Nav Icons
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ODSBox(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x33FFFFFF))
                                    .clickable(onClick = onBack),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(imageVector = Icons.Filled.ArrowBack),
                                    tint = Color.White
                                )
                            }
                            ODSBox(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x33FFFFFF)),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(imageVector = Icons.Outlined.Tune),
                                    tint = Color.White
                                )
                            }
                        }

                        // Avatar & Name Row
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            gap = 14.dp,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ODSBox(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.White, CircleShape)
                                    .background(DarkSurfaceColor),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSText(
                                    text = "M",
                                    style = ODSTextStyles.pompiereTitleM,
                                    color = HexColor(0xffffffff)
                                )
                            }

                            ODSColumn(gap = 6.dp) {
                                ODSText(
                                    text = "Minain Baski",
                                    style = ODSTextStyles.pompiereTitleM,
                                    color = HexColor(0xffffffff)
                                )
                                ODSText(
                                    text = "@minainbas.456",
                                    style = ODSTextStyles.microcopyRegular,
                                    color = HexColor(0xffffffff)
                                )
                                ODSRow(gap = 8.dp) {
                                    // Following / Follow Button
                                    ODSButton(
                                        scheme = scheme,
                                        props = ODSButtonProps(
                                            label = if (isFollowing) "Following" else "Follow",
                                            variant = if (isFollowing) ODSButtonVariant.OUTLINE else ODSButtonVariant.PRIMARY,
                                            size = ODSButtonSize.SMALL
                                        ),
                                        onClick = { isFollowing = !isFollowing }
                                    )
                                    // Chat Button
                                    ODSButton(
                                        scheme = scheme,
                                        props = ODSButtonProps(
                                            label = "Chat",
                                            variant = ODSButtonVariant.SECONDARY,
                                            size = ODSButtonSize.SMALL
                                        ),
                                        onClick = {}
                                    )
                                }
                            }
                        }

                        // Stats Row
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            StatPill(number = "245", label = "Posts")
                            StatPill(number = "50.5k", label = "Followers")
                            StatPill(number = "250", label = "Following")
                        }
                    }
                }
            }

            // Media Tab Icons
            item {
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ODSIcon(iconModel = ODSIconModel(imageVector = Icons.Outlined.GridView), tint = NeonPink)
                    ODSIcon(iconModel = ODSIconModel(imageVector = Icons.Outlined.Videocam), tint = Color.Gray)
                    ODSIcon(iconModel = ODSIconModel(imageVector = Icons.Outlined.FavoriteBorder), tint = Color.Gray)
                    ODSIcon(iconModel = ODSIconModel(imageVector = Icons.Outlined.BookmarkBorder), tint = Color.Gray)
                }
            }

            // Staggered Photo Grid
            item {
                ODSColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    gap = 10.dp
                ) {
                    ODSRow(gap = 10.dp) {
                        ODSBox(
                            modifier = Modifier
                                .weight(1f)
                                .height(210.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(DarkSurfaceColor),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(text = "📸 Photo 1", style = ODSTextStyles.microcopyBold, color = HexColor(0xffffffff))
                        }
                        ODSBox(
                            modifier = Modifier
                                .weight(1f)
                                .height(160.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(DarkSurfaceColor),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(text = "📸 Photo 2", style = ODSTextStyles.microcopyBold, color = HexColor(0xffffffff))
                        }
                    }
                    ODSRow(gap = 10.dp) {
                        ODSBox(
                            modifier = Modifier
                                .weight(1f)
                                .height(130.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(DarkSurfaceColor),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(text = "📸 Photo 3", style = ODSTextStyles.microcopyBold, color = HexColor(0xffffffff))
                        }
                        ODSBox(
                            modifier = Modifier
                                .weight(1f)
                                .height(180.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(DarkSurfaceColor),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(text = "📸 Photo 4", style = ODSTextStyles.microcopyBold, color = HexColor(0xffffffff))
                        }
                    }
                }
            }
        }

        // Floating Bottom Dock (Profile Active)
        FloatingSocialDock(
            activeTab = "Profile",
            onTabClick = { if (it == "Home") onBack() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

/**
 * 3. Onboarding / Hero Showcase View (Left Phone in Screenshot)
 */
@Composable
private fun SocialOnboardingView(
    onGetStarted: () -> Unit
) {
    ODSColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Hero Titles
        ODSColumn(gap = 10.dp) {
            ODSText(
                text = "The",
                style = ODSTextStyles.pompiereDisplayL,
                color = HexColor(0xffffffff)
            )
            ODSText(
                text = "Social Space",
                style = ODSTextStyles.pompiereDisplayL,
                color = HexColor(0xffe20074)
            )
            ODSText(
                text = "for Everyone",
                style = ODSTextStyles.pompiereDisplayL,
                color = HexColor(0xffffffff)
            )
            ODSText(
                text = "Share your story and connect with the world in private calls & live chats.",
                style = ODSTextStyles.bodySRegular,
                color = HexColor(0xffcacaca)
            )
        }

        // Center Model Card with Dashed Border Look
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, Color(0x66E20074), RoundedCornerShape(16.dp))
                .background(DarkSurfaceColor),
            contentAlignment = Alignment.Center
        ) {
            ODSColumn(horizontalAlignment = Alignment.CenterHorizontally, gap = 8.dp) {
                ODSText(text = "🌟", style = ODSTextStyles.pompiereDisplayL, color = HexColor(0xffffffff))
                ODSText(text = "Connect & Inspire", style = ODSTextStyles.bodyMBold, color = HexColor(0xffffffff))
            }
        }

        // Bottom Navigation Dots & Action Circle Buttons
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 3-dot Carousel indicator
            ODSRow(gap = 6.dp) {
                ODSBox(modifier = Modifier.size(8.dp).clip(CircleShape).background(NeonPink)) {}
                ODSBox(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.Gray)) {}
                ODSBox(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.Gray)) {}
            }

            // Arrow Action Circle Button
            ODSBox(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(NeonPink)
                    .clickable(onClick = onGetStarted),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.navigation_right_type_standard_size_standard),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun StatPill(number: String, label: String) {
    ODSColumn(horizontalAlignment = Alignment.CenterHorizontally, gap = 2.dp) {
        ODSText(text = number, style = ODSTextStyles.bodyLBold, color = HexColor(0xffffffff))
        ODSText(text = label, style = ODSTextStyles.microcopyRegular, color = HexColor(0xffffffff))
    }
}

/**
 * Floating Dock Navigation
 */
@Composable
private fun FloatingSocialDock(
    activeTab: String,
    onTabClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier
            .clip(RoundedCornerShape(30.dp))
            .background(DarkSurfaceColor)
            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(30.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Home
        if (activeTab == "Home") {
            ODSRow(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(NeonPink)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                gap = 4.dp
            ) {
                ODSIcon(iconModel = ODSIconModel(imageVector = Icons.Filled.Home), tint = Color.White)
                ODSText(text = "Home", style = ODSTextStyles.microcopyBold, color = HexColor(0xffffffff))
            }
        } else {
            IconButton(onClick = { onTabClick("Home") }) {
                ODSIcon(iconModel = ODSIconModel(imageVector = Icons.Outlined.Home), tint = Color.Gray)
            }
        }

        // Favorites
        IconButton(onClick = {}) {
            ODSIcon(iconModel = ODSIconModel(imageVector = Icons.Outlined.FavoriteBorder), tint = Color.Gray)
        }

        // Calls
        IconButton(onClick = {}) {
            ODSIcon(iconModel = ODSIconModel(imageVector = Icons.Outlined.Call), tint = Color.Gray)
        }

        // Profile
        if (activeTab == "Profile") {
            ODSRow(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(NeonPink)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                gap = 4.dp
            ) {
                ODSIcon(iconModel = ODSIconModel(imageVector = Icons.Filled.Person), tint = Color.White)
                ODSText(text = "Profile", style = ODSTextStyles.microcopyBold, color = HexColor(0xffffffff))
            }
        } else {
            IconButton(onClick = { onTabClick("Profile") }) {
                ODSIcon(iconModel = ODSIconModel(imageVector = Icons.Outlined.Person), tint = Color.Gray)
            }
        }
    }
}
