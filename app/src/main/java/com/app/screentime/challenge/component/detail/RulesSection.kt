package com.app.screentime.challenge.component.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.accordion.ODSAccordion
import com.telekom.odsystem.molecules.accordion.ODSAccordionProps
import com.telekom.odsystem.molecules.accordion.ODSAccordionSize
import com.telekom.odsystem.slots.accordiontextbody.ODSAccordionTextBody
import com.telekom.odsystem.slots.accordiontextbody.ODSAccordionTextBodyProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Rules section using ODS Accordion.
 */
@Composable
fun RulesSection(
    rules: String,
    scheme: ODSTheme,
    expanded: Boolean = false,
    onExpandedChange: ((Boolean) -> Unit)? = null
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        gap = DSVariables.spacingComponent3
    ) {
        ODSAccordion(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSAccordionProps(
                headerText = "Rules",
                expanded = expanded,
                size = ODSAccordionSize.SMALL
            ),
            onClick = { newExpanded ->
                onExpandedChange?.invoke(newExpanded)
            }, contentSlot = {
                ODSAccordionTextBody(
                    scheme = scheme,
                    props = ODSAccordionTextBodyProps(
                        text = rules
                    )
                )
            }
        )
    }
}

