package com.telekom.odsystem.slots.productcardfeature

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSWrap
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ODSProductCardFeature(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSProductCardFeatureProps = ODSProductCardFeatureProps()
) {

    val style = ODSProductCardFeatureStyle().getStyle(scheme = scheme)

    ODSRow(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        horizontalArrangement = style.horizontalArrangement
    ) {
        ODSWrap(
            modifier = Modifier.weight(1f),
            horizontalGap = style.listContainerHorizontalGap,
            verticalGap = style.listContainerVerticalGap,
            horizontalAlignment = style.listContainerHorizontalAlignment,
            verticalAlignment = style.listContainerVerticalAlignment,
            horizontalArrangement = style.listContainerHorizontalArrangement
        ) {
            props.tagStaticProps?.forEachIndexed { index, item ->
                ODSTagStatic(scheme = scheme, props = item)
            }
        }
    }
}
