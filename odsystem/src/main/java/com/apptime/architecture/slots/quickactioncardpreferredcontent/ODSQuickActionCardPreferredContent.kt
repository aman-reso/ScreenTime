package com.telekom.odsystem.slots.quickactioncardpreferredcontent

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.sparkline.ODSSparkline
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
@Composable
fun ODSQuickActionCardPreferredContent(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSQuickActionCardPreferredContentProps = ODSQuickActionCardPreferredContentProps(),
) {
    val context = LocalContext.current
    val style = ODSQuickActionCardPreferredContentStyle().getStyle(scheme = scheme)
    ODSColumn(
        modifier = modifier
            .fillMaxWidth()
            .applySemantics(context = context, props = props),
        gap = style.gap,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        verticalArrangement = style.verticalArrangement
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = style.copyAndSparklineGap,
            verticalAlignment = style.copyAndSparklineVerticalAlignment,
            horizontalAlignment = style.copyAndSparklineHorizontalAlignment,
            verticalArrangement = style.copyAndSparklineVerticalArrangement
        ) {
            if (props.titleType == ODSQuickActionCardPreferredContentTitleType.TEXT && !props.title.isNullOrEmpty()) {
                ODSText(
                    modifier = Modifier.fillMaxWidth(),
                    text = props.title,
                    style = style.titleStyle,
                    color = style.titleColor,
                    textAlign = style.titleTextAlign
                )
            }
            if (props.titleType == ODSQuickActionCardPreferredContentTitleType.LOGO) {
                ODSImage(
                    imageModel = props.logo,
                    height = style.logoHeight,
                    contentScale = ContentScale.Fit
                )
            }
            if (!props.subtitle.isNullOrEmpty()) {
                ODSText(
                    modifier = Modifier.fillMaxWidth(),
                    text = props.subtitle,
                    style = style.subtitleStyle,
                    color = style.subtitleColor,
                    textAlign = style.subtitleTextAlign
                )
            }
            props.sparklineProps?.let { props ->
                ODSSparkline(scheme = scheme, props = props.toODSSparklineProps())
            }
        }
        if (props.showTags) {
            ODSRow(
                gap = style.tagsContainerGap,
                horizontalAlignment = style.tagsContainerHorizontalAlignment,
                verticalAlignment = style.tagsContainerVerticalAlignment,
                horizontalArrangement = style.tagsContainerHorizontalArrangement
            ) {
                props.tag1Props?.let { props ->
                    ODSTagStatic(scheme = scheme, props = props)
                }
                props.tag2Props?.let { props ->
                    ODSTagStatic(scheme = scheme, props = props)
                }
            }
        }
    }
}

private fun Modifier.applySemantics(
    context: Context,
    props: ODSQuickActionCardPreferredContentProps,
): Modifier {
    var contentDescription = ""
    if (props.titleType == ODSQuickActionCardPreferredContentTitleType.TEXT) {
        contentDescription += props.title.orEmpty()
    }
    if (props.titleType == ODSQuickActionCardPreferredContentTitleType.LOGO) {
        contentDescription += props.logo?.contentDescription.orEmpty()
    }
    props.subtitle?.let {
        contentDescription += "\n $it"
    }
    props.sparklineProps?.let {
        contentDescription += "\n ${
            context.getString(R.string.percent_progress, "${getSparkLineProgress(it.percentage)}")
        }"
    }

    props.tag1Props?.label?.let {
        contentDescription += "\n $it"
    }
    props.tag2Props?.label?.let {
        contentDescription += "\n $it"
    }
    return clearAndSetSemantics {
        this.contentDescription = contentDescription
    }
}

@Suppress("MagicNumber")
private fun getSparkLineProgress(value: Float): Int {
    return when {
        value <= 1.0 -> 0
        value <= 25.0 -> 25
        value <= 50.0 -> 50
        value <= 75.0 -> 75
        else -> 100
    }
}
