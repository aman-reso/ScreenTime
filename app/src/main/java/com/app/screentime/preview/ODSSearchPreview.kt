package com.app.screentime.preview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.resultitem.ODSResultItemProps
import com.telekom.odsystem.extensions.background
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.molecules.searchbar.ODSSearchBar
import com.telekom.odsystem.molecules.searchbar.ODSSearchBarButtonProps
import com.telekom.odsystem.molecules.searchbar.ODSSearchBarProps
import com.telekom.odsystem.molecules.searchresultlist.ODSSearchResultList
import com.telekom.odsystem.molecules.searchresultlist.ODSSearchResultListProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.searchview.ODSSearchView
import com.telekom.odsystem.organisms.searchview.ODSSearchViewProps

@Preview(showBackground = true)
@Composable
fun ODSSearchPreview() {
    ODSBox(
        modifier = Modifier,
        background = listOf(ODSColorModel(neutralScheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DSVariables.spacingComponent5),
            gap = DSVariables.spacingComponent5
        ) {
            // Section: ODSSearchBar Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent4
            ) {
                ODSText(
                    text = "ODSSearchBar - Basic Search Input",
                    style = com.telekom.odsystem.DSTextStyles.titleM,
                    color = neutralScheme.basicText
                )

                // Basic Search Bar
                var searchText1 by remember { mutableStateOf("") }
                ODSSearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSearchBarProps(
                        input = searchText1,
                        placeholder = "Search..."
                    ),
                    onValueChange = { searchText1 = it }
                )

                // Search Bar with Clear Button
                var searchText2 by remember { mutableStateOf("") }
                ODSSearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSearchBarProps(
                        input = searchText2,
                        placeholder = "Search with clear button...",
                        buttonProps = ODSSearchBarButtonProps(
                            buttonIcon = ODSIconModel(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear"
                            )
                        )
                    ),
                    onValueChange = { searchText2 = it },
                    onButtonClick = { searchText2 = "" }
                )

                // Search Bar with Search Icon Button
                var searchText3 by remember { mutableStateOf("") }
                ODSSearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSearchBarProps(
                        input = searchText3,
                        placeholder = "Search with search button...",
                        buttonProps = ODSSearchBarButtonProps(
                            buttonIcon = ODSIconModel(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        )
                    ),
                    onValueChange = { searchText3 = it },
                    onButtonClick = { /* Handle search */ }
                )

                // Disabled Search Bar
                ODSSearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSearchBarProps(
                        input = "Disabled search",
                        placeholder = "Search...",
                        disabled = true
                    ),
                    onValueChange = {}
                )

                // Search Bar with Custom Keyboard
                var searchText4 by remember { mutableStateOf("") }
                ODSSearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSearchBarProps(
                        input = searchText4,
                        placeholder = "Search with number keyboard..."
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            // Handle search action
                        }
                    ),
                    onValueChange = { searchText4 = it }
                )
            }

            // Section: ODSSearchView Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent4
            ) {
                ODSText(
                    text = "ODSSearchView - Full Search Interface",
                    style = com.telekom.odsystem.DSTextStyles.titleM,
                    color = neutralScheme.basicText
                )

                // Search View with Back Button
                var searchText5 by remember { mutableStateOf("") }
                val focusRequester1 = remember { FocusRequester() }
                ODSSearchView(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSearchViewProps(
                        showBackButton = true,
                        searchBarProps = ODSSearchBarProps(
                            input = searchText5,
                            placeholder = "Search with back button...",
                            buttonProps = ODSSearchBarButtonProps(
                                buttonIcon = ODSIconModel(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear"
                                )
                            )
                        )
                    ),
                    focusRequester = focusRequester1,
                    onSearchValueChange = { searchText5 = it },
                    onButtonClick = { searchText5 = "" },
                    onBackButtonClick = { /* Handle back */ }
                )

                // Search View without Back Button
                var searchText6 by remember { mutableStateOf("") }
                val focusRequester2 = remember { FocusRequester() }
                ODSSearchView(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSearchViewProps(
                        showBackButton = false,
                        searchBarProps = ODSSearchBarProps(
                            input = searchText6,
                            placeholder = "Search without back button...",
                            buttonProps = ODSSearchBarButtonProps(
                                buttonIcon = ODSIconModel(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search"
                                )
                            )
                        )
                    ),
                    focusRequester = focusRequester2,
                    onSearchValueChange = { searchText6 = it },
                    onButtonClick = { /* Handle search */ }
                )

                // Search View with Results
                var searchText7 by remember { mutableStateOf("") }
                val focusRequester3 = remember { FocusRequester() }
                val focusManager = LocalFocusManager.current

                val mockResults = remember(searchText7) {
                    if (searchText7.isBlank()) {
                        emptyList()
                    } else {
                        listOf(
                            ODSResultItemProps(
                                labelText = "Result 1: $searchText7",
                                recessiveLabelText = "Description for result 1",
                                icon = ODSIconModel(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Result"
                                )
                            ),
                            ODSResultItemProps(
                                labelText = "Result 2: $searchText7",
                                recessiveLabelText = "Description for result 2",
                                icon = ODSIconModel(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Result"
                                )
                            ),
                            ODSResultItemProps(
                                labelText = "Result 3: $searchText7",
                                recessiveLabelText = "Description for result 3",
                                icon = ODSIconModel(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Result"
                                )
                            )
                        )
                    }
                }

                ODSSearchView(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSearchViewProps(
                        showBackButton = true,
                        searchBarProps = ODSSearchBarProps(
                            input = searchText7,
                            placeholder = "Search with results...",
                            buttonProps = ODSSearchBarButtonProps(
                                buttonIcon = ODSIconModel(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear"
                                )
                            )
                        )
                    ),
                    focusRequester = focusRequester3,
                    onSearchValueChange = { searchText7 = it },
                    onButtonClick = { searchText7 = "" },
                    onBackButtonClick = { /* Handle back */ },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            focusManager.clearFocus()
                        }
                    ),
                    resultListSlot = {
                        if (mockResults.isNotEmpty()) {
                            ODSSearchResultList(
                                modifier = Modifier.fillMaxWidth(),
                                scheme = neutralScheme,
                                props = ODSSearchResultListProps(
                                    label = "Search Results",
                                    resultList = mockResults
                                ),
                                onItemClick = { index ->
                                    // Handle item click
                                }
                            )
                        }
                    }
                )
            }

            // Section: ODSSearchResultList Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent4
            ) {
                ODSText(
                    text = "ODSSearchResultList - Search Results Display",
                    style = com.telekom.odsystem.DSTextStyles.titleM,
                    color = neutralScheme.basicText
                )

                // Basic Result List
                val basicResults = listOf(
                    ODSResultItemProps(
                        labelText = "Challenge 1",
                        recessiveLabelText = "30-Day Digital Detox Challenge",
                        icon = ODSIconModel(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Challenge"
                        )
                    ),
                    ODSResultItemProps(
                        labelText = "Challenge 2",
                        recessiveLabelText = "Reduce Screen Time by 30%",
                        icon = ODSIconModel(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Challenge"
                        )
                    ),
                    ODSResultItemProps(
                        labelText = "Challenge 3",
                        recessiveLabelText = "Weekly Focus Challenge",
                        icon = ODSIconModel(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Challenge"
                        )
                    )
                )

                ODSSearchResultList(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSearchResultListProps(
                        label = "Challenges",
                        resultList = basicResults
                    ),
                    onItemClick = { index ->
                        // Handle item click
                    }
                )

                // Result List without Label
                val resultsWithoutLabel = listOf(
                    ODSResultItemProps(
                        labelText = "User 1",
                        recessiveLabelText = "username1@example.com",
                        icon = ODSIconModel(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User"
                        )
                    ),
                    ODSResultItemProps(
                        labelText = "User 2",
                        recessiveLabelText = "username2@example.com",
                        icon = ODSIconModel(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User"
                        )
                    )
                )

                ODSSearchResultList(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSearchResultListProps(
                        label = null,
                        resultList = resultsWithoutLabel
                    ),
                    onItemClick = { index ->
                        // Handle item click
                    }
                )

                // Result List with Icons Only
                val iconOnlyResults = listOf(
                    ODSResultItemProps(
                        labelText = "App 1",
                        recessiveLabelText = "com.example.app1",
                        icon = ODSIconModel(
                            imageVector = Icons.Default.Apps,
                            contentDescription = "App"
                        )
                    ),
                    ODSResultItemProps(
                        labelText = "App 2",
                        recessiveLabelText = "com.example.app2",
                        icon = ODSIconModel(
                            imageVector = Icons.Default.Apps,
                            contentDescription = "App"
                        )
                    ),
                    ODSResultItemProps(
                        labelText = "App 3",
                        recessiveLabelText = "com.example.app3",
                        icon = ODSIconModel(
                            imageVector = Icons.Default.Apps,
                            contentDescription = "App"
                        )
                    )
                )

                ODSSearchResultList(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSearchResultListProps(
                        label = "Apps",
                        resultList = iconOnlyResults
                    ),
                    onItemClick = { index ->
                        // Handle item click
                    }
                )
            }

            // Section: Complete Search Example
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent4
            ) {
                ODSText(
                    text = "Complete Search Example - Challenge Search",
                    style = com.telekom.odsystem.DSTextStyles.titleM,
                    color = neutralScheme.basicText
                )

                var challengeSearchText by remember { mutableStateOf("") }
                val focusRequester4 = remember { FocusRequester() }
                val focusManager2 = LocalFocusManager.current

                val challengeResults = remember(challengeSearchText) {
                    val allChallenges = listOf(
                        "30-Day Digital Detox",
                        "Reduce Screen Time",
                        "Weekly Focus Challenge",
                        "App Blocking Master",
                        "Daily Screen Time Limit"
                    )

                    if (challengeSearchText.isBlank()) {
                        emptyList()
                    } else {
                        allChallenges
                            .filter { it.contains(challengeSearchText, ignoreCase = true) }
                            .map { challengeName ->
                                ODSResultItemProps(
                                    labelText = challengeName,
                                    recessiveLabelText = "Click to join this challenge",
                                    icon = ODSIconModel(
                                        imageVector = Icons.Default.EmojiEvents,
                                        contentDescription = "Challenge"
                                    )
                                )
                            }
                    }
                }

                ODSSearchView(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSearchViewProps(
                        showBackButton = true,
                        searchBarProps = ODSSearchBarProps(
                            input = challengeSearchText,
                            placeholder = "Search challenges...",
                            buttonProps = ODSSearchBarButtonProps(
                                buttonIcon = ODSIconModel(
                                    imageVector = if (challengeSearchText.isNotEmpty()) {
                                        Icons.Default.Clear
                                    } else {
                                        Icons.Default.Search
                                    },
                                    contentDescription = if (challengeSearchText.isNotEmpty()) "Clear" else "Search"
                                )
                            )
                        )
                    ),
                    focusRequester = focusRequester4,
                    onSearchValueChange = { challengeSearchText = it },
                    onButtonClick = {
                        if (challengeSearchText.isNotEmpty()) {
                            challengeSearchText = ""
                        } else {
                            // Perform search
                        }
                    },
                    onBackButtonClick = { /* Handle back */ },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            focusManager2.clearFocus()
                        }
                    ),
                    resultListSlot = {
                        if (challengeResults.isNotEmpty()) {
                            ODSSearchResultList(
                                modifier = Modifier.fillMaxWidth(),
                                scheme = neutralScheme,
                                props = ODSSearchResultListProps(
                                    label = "${challengeResults.size} results found",
                                    resultList = challengeResults
                                ),
                                onItemClick = { index ->
                                    // Handle challenge selection
                                    challengeSearchText = challengeResults[index].labelText ?: ""
                                }
                            )
                        } else if (challengeSearchText.isNotEmpty()) {
                            ODSColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(DSVariables.spacingComponent4),
                                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                            ) {
                                ODSText(
                                    text = "No results found",
                                    style = com.telekom.odsystem.DSTextStyles.bodyMRegular,
                                    color = neutralScheme.basicTextRecessive
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

