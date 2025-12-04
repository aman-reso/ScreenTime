package com.telekom.odsystem.organisms.ratingstarsstatic

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

const val ONE = 1
const val FIVE = 5

@Suppress("LongMethod")
/**
 * ODSRatingStarsStatic composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@Composable
fun ODSRatingStarsStatic(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSRatingStarsStaticProps = ODSRatingStarsStaticProps()
) {
    val style = ODSRatingStarsStaticStyle().getStyle(scheme = scheme, props = props)
    ODSColumn(
        modifier = modifier,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        verticalArrangement = style.verticalArrangement
    ) {
        if (!props.label.isNullOrEmpty()) {
            ODSRow(
                gap = style.labelContainerGap,
                padding = style.labelContainerPadding,
                horizontalAlignment = style.labelContainerHorizontalAlignment,
                verticalAlignment = style.labelContainerVerticalAlignment,
                horizontalArrangement = style.labelContainerHorizontalArrangement
            ) {
                ODSText(
                    text = props.label,
                    style = style.ratingLabelTextStyle,
                    color = style.ratingLabelColor,
                    textAlign = style.ratingLabelTextAlign ?: TextAlign.Center
                )
            }
        }

        ODSRow(
            gap = style.ratingGap,
            horizontalAlignment = style.ratingHorizontalAlignment,
            verticalAlignment = style.ratingVerticalAlignment,
            horizontalArrangement = style.ratingHorizontalArrangement
        ) {
            ODSRow(
                modifier = Modifier.applySemantics(props, context = LocalContext.current),
                gap = style.starsListContainerGap,
                horizontalAlignment = style.starsListContainerHorizontalAlignment,
                verticalAlignment = style.starsListContainerVerticalAlignment,
                horizontalArrangement = style.starsListContainerHorizontalArrangement
            ) {
                for (i in ONE..FIVE) {
                    ODSIcon(
                        iconModel = if (i <= props.stars) {
                            ODSIconModel(drawableRes = R.drawable.star_type_bold)
                        } else {
                            ODSIconModel(
                            drawableRes = R.drawable.star_type_standard
                        )
                        },
                        tint = if (i <= props.stars) style.starColor?.getColor() else style.starColorUnselected?.getColor(),
                        width = style.starWidth,
                        height = style.starHeight
                    )
                }
            }

            if (!props.helperText.isNullOrEmpty()) {
                ODSText(
                    text = props.helperText,
                    style = style.helperTextTextStyle,
                    color = style.helperTextColor,
                    textAlign = style.helperTextTextAlign ?: TextAlign.Center
                )
            }
        }
    }
}

private fun Modifier.applySemantics(
    props: ODSRatingStarsStaticProps,
    context: Context
): Modifier {
    return this.semantics(mergeDescendants = true) {
        val value =
            (props.stars).toString() + " " + context.getString(R.string.semantic_out_of_five_stars) + " " + context.getString(
                R.string.semantic_selected
            )
        stateDescription = value
    }
}
