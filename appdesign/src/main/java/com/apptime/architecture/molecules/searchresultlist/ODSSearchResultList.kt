package com.telekom.odsystem.molecules.searchresultlist

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.resultitem.ODSResultItem
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ODSSearchResultList(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSSearchResultListProps = ODSSearchResultListProps(),
    onItemClick: (Int) -> Unit,
) {
    val style = ODSSearchResultListStyle().getStyle(scheme = scheme)

    ODSColumn(
        gap = style.gap,
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        modifier = modifier.fillMaxWidth()
    ) {
        if (props.label.isNullOrEmpty().not()) {
            ODSRow(
                padding = style.labelContainerPadding,
                horizontalArrangement = style.labelContainerHorizontalArrangement,
                horizontalAlignment = style.labelContainerHorizontalAlignment,
                verticalAlignment = style.labelContainerVerticalAlignment,
                modifier = Modifier.fillMaxWidth()
            ) {
                ODSText(
                    modifier = Modifier.weight(1f),
                    text = props.label,
                    style = style.labelTextStyle,
                    color = style.labelColor,
                    textAlign = style.labelTextAlign
                )
            }
        }

        props.resultList?.let { itemList ->
            ODSLazyColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = style.resultListContainerGap,
                padding = ODSPadding(horizontal = DSVariables.spacingComponent4),
                verticalArrangement = style.resultListContainerVerticalArrangement,
                verticalAlignment = style.resultListContainerVerticalAlignment,
                horizontalAlignment = style.resultListContainerHorizontalAlignment
            ) {
                items(itemList.size) { index ->
                    ODSResultItem(
                        scheme = scheme,
                        props = itemList[index],
                        onItemClick = { onItemClick(index) }
                    )
                    if (index < itemList.size - 1) {
                        ODSDivider(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSDividerProps(
                                variant = ODSDividerVariant.HORIZONTAL,
                                inset = false
                            )
                        )
                    }
                }
            }
        }
    }
}
