package com.telekom.odsystem.molecules.list

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.listitem.ODSListItem
import com.telekom.odsystem.atoms.listitem.ODSListItemProps
import com.telekom.odsystem.atoms.listitem.ODSListItemVariant
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSList composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onFirstLevelListItemClicked Callback triggered when action occurs.
 * @param onSecondLevelListItemClicked Callback triggered when action occurs.
 * @param onThirdLevelListItemClicked Callback triggered when action occurs.
 */
@Suppress("LongMethod", "ParameterListWrapping")
@Composable
fun ODSList(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSListProps = ODSListProps(),
    onFirstLevelListItemClicked: (Int) -> Unit = {},
    onSecondLevelListItemClicked: (firstLevelIndex: Int, secondLevelIndex: Int) -> Unit = { firstLevelIndex, secondLevelIndex -> },
    onThirdLevelListItemClicked: (
        firstLevelIndex: Int,
        secondLevelIndex: Int,
        thirdLevelIndex: Int
    ) -> Unit = { firstLevelIndex, secondLevelIndex, thirdLevelIndex -> }
) {
    val style = ODSListStyle().getStyle(scheme = scheme)
    ODSColumn(
        modifier = modifier,
        gap = style.gap,
        padding = style.padding,
        cornerRadius = style.cornerRadius,
        verticalAlignment = style.verticalAlignment,
        verticalArrangement = style.verticalArrangement,
        horizontalAlignment = style.horizontalAlignment,
    ) {
        val firstLevelItems = props.items
        ODSColumn(
            gap = style.listContainerGap,
            verticalAlignment = style.listContainerVerticalAlignment,
            horizontalAlignment = style.listContainerHorizontalAlignment,
            verticalArrangement = style.listContainerVerticalArrangement
        ) {
            firstLevelItems?.indices?.forEach { firstLevelIndex ->
                val firstListProps = firstLevelItems[firstLevelIndex].listItemProps
                    .toODSFirstLevelListItemProps(
                        variant = props.variant,
                        number = getFirstLevelNumber(firstLevelIndex = firstLevelIndex)
                    )
                ODSListItem(
                    modifier = Modifier.applySemanticsListItem(
                        props = firstListProps,
                        level = 1,
                        element = firstLevelIndex + 1,
                        context = LocalContext.current
                    ),
                    scheme = scheme,
                    props = firstListProps,
                    onClick = {
                        onFirstLevelListItemClicked(firstLevelIndex)
                    }
                )

                ODSColumn(
                    padding = style.secondLevelPadding,
                    verticalArrangement = style.secondLevelVerticalArrangement,
                    verticalAlignment = style.secondLevelVerticalAlignment,
                    horizontalAlignment = style.secondLevelHorizontalAlignment
                ) {
                    val secondLevelItems = firstLevelItems[firstLevelIndex].items
                    ODSColumn(
                        gap = style.listContainerGap,
                        verticalAlignment = style.listContainerVerticalAlignment,
                        horizontalAlignment = style.listContainerHorizontalAlignment,
                        verticalArrangement = style.listContainerVerticalArrangement
                    ) {
                        secondLevelItems.indices.forEach { secondLevelIndex ->
                            val secondListProps = secondLevelItems[secondLevelIndex].listItemProps
                                .toODSInnerLevelListItemProps(
                                    variant = props.variant,
                                    number = getSecondLevelNumber(
                                        firstLevelIndex = firstLevelIndex,
                                        secondLevelIndex = secondLevelIndex
                                    )
                                )
                            ODSListItem(
                                modifier = Modifier.applySemanticsListItem(
                                    props = secondListProps,
                                    level = 2,
                                    element = secondLevelIndex + 1,
                                    context = LocalContext.current
                                ),
                                scheme = scheme,
                                props = secondListProps,
                                onClick = {
                                    onSecondLevelListItemClicked(
                                        firstLevelIndex,
                                        secondLevelIndex
                                    )
                                }
                            )

                            ODSColumn(
                                padding = ODSPadding(
                                    left = (style.thirdLevelPadding?.left?.minus(
                                        style.secondLevelPadding?.left ?: 0.dp
                                    ))
                                ),
                                verticalArrangement = style.thirdLevelVerticalArrangement,
                                verticalAlignment = style.thirdLevelVerticalAlignment,
                                horizontalAlignment = style.thirdLevelHorizontalAlignment
                            ) {
                                val thirdLevelItems = secondLevelItems[secondLevelIndex].items
                                ODSColumn(
                                    gap = style.listContainerGap,
                                    verticalAlignment = style.listContainerVerticalAlignment,
                                    horizontalAlignment = style.listContainerHorizontalAlignment,
                                    verticalArrangement = style.listContainerVerticalArrangement
                                ) {
                                    thirdLevelItems.indices.forEach { thirdLevelIndex ->
                                        val thirdListProps =
                                            thirdLevelItems[thirdLevelIndex].listItemProps
                                                .toODSInnerLevelListItemProps(
                                                    variant = props.variant,
                                                    number = getThirdLevelNumber(
                                                        firstLevelIndex = firstLevelIndex,
                                                        secondLevelIndex = secondLevelIndex,
                                                        thirdLevelIndex = thirdLevelIndex
                                                    )
                                                )
                                        ODSListItem(
                                            modifier = Modifier.applySemanticsListItem(
                                                props = thirdListProps,
                                                level = 3,
                                                element = thirdLevelIndex + 1,
                                                context = LocalContext.current
                                            ),
                                            scheme = scheme,
                                            props = thirdListProps,
                                            onClick = {
                                                onThirdLevelListItemClicked(
                                                    firstLevelIndex,
                                                    secondLevelIndex,
                                                    thirdLevelIndex
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getFirstLevelNumber(firstLevelIndex: Int): String {
    return firstLevelIndex.plus(1).toString().plus(".")
}

private fun getSecondLevelNumber(
    firstLevelIndex: Int,
    secondLevelIndex: Int
): String {
    return firstLevelIndex.plus(1).toString().plus(".").plus(secondLevelIndex.plus(1))
}

private fun getThirdLevelNumber(
    firstLevelIndex: Int,
    secondLevelIndex: Int,
    thirdLevelIndex: Int
): String {
    return firstLevelIndex.plus(1).toString().plus(".").plus(secondLevelIndex.plus(1)).plus(".")
        .plus(thirdLevelIndex.plus(1))
}

private fun Modifier.applySemanticsListItem(
    props: ODSListItemProps,
    level: Int?,
    element: Int?,
    context: Context
): Modifier {
    return this.clearAndSetSemantics {
        val prefixText = when (props.variant) {
            ODSListItemVariant.ICON, ODSListItemVariant.BULLETPOINT, ODSListItemVariant.OUTLINE_BULLET -> {
                val listItemLevelText =
                    context.getString(R.string.semantic_list_item_level) + " , " + level
                val listItemElementText =
                    context.getString(R.string.semantic_element) + " , " + element
                "$listItemLevelText , $listItemElementText , " + (props.icon?.contentDescription
                    ?: "")
            }

            ODSListItemVariant.NUMBER -> {
                props.number
            }
        }
        val listItemLabel =
            if (props.link) context.getString(R.string.semantic_link) else ""
        this.contentDescription =
            "$prefixText , ${props.text ?: ""} , $listItemLabel"
    }
}
