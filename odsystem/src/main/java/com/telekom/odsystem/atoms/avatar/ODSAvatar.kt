package com.telekom.odsystem.atoms.avatar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.badgeicon.ODSBadgeIcon
import com.telekom.odsystem.atoms.badgenumber.ODSBadgeNumber
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.foundations.SINGLE_LINE
import com.telekom.odsystem.foundations.offset
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSAvatar composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@Composable
fun ODSAvatar(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSAvatarProps = ODSAvatarProps(),
) {
    val style = ODSAvatarStyle().getStyle(scheme = scheme, props = props)
    when (props.variant) {
        ODSAvatarVariant.AVATAR -> {
            if (props.image == null) return
        }

        ODSAvatarVariant.ICON -> {
            if (props.icon == null) return
        }

        ODSAvatarVariant.INITIALS -> {
            if (props.initials.isNullOrEmpty()) return
        }
    }

    ODSAvatarContainer(
        modifier = modifier,
        scheme = scheme,
        style = style,
        props = props,
    )
}

@Composable
private fun ODSAvatarContainer(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSAvatarStyle,
    props: ODSAvatarProps,
) {
    ODSBox(
        contentAlignment = style.contentAlignment,
        width = style.width,
        height = style.height,
        modifier = modifier
            .semantics(mergeDescendants = true) { }
            .sizeWithinBounds(
                minWidth = style.minWidth ?: Dp.Unspecified,
                minHeight = style.minHeight ?: Dp.Unspecified
            )
    ) {
        ODSRow(
            padding = style.padding,
            cornerRadius = style.borderRadius,
            horizontalArrangement = style.horizontalArrangement,
            horizontalAlignment = style.horizontalAlignment,
            verticalAlignment = style.verticalAlignment,
            background = style.backgroundColor,
            width = style.width,
            height = style.height,
            modifier = Modifier.sizeWithinBounds(
                minWidth = style.minWidth ?: Dp.Unspecified,
                minHeight = style.minHeight ?: Dp.Unspecified
            )
        ) {
            when (props.variant) {
                ODSAvatarVariant.AVATAR -> ODSAvatarVariant(props = props, style = style)
                ODSAvatarVariant.ICON -> ODSIconVariant(props = props, style = style)
                ODSAvatarVariant.INITIALS -> ODSInitialsVariant(props = props, style = style)
            }
        }
        if (props.showBadge) {
            val alignment = getAlignment(
                badgeType = props.badgeType, style = style
            )
            val offset = getOffset(
                badgeType = props.badgeType, style = style
            )
            ODSBadgeContainer(
                modifier = Modifier
                    .align(alignment = alignment)
                    .offset(offset = offset),
                props = props,
                scheme = scheme,
            )
        }
    }
}

@Composable
private fun ODSBadgeContainer(
    modifier: Modifier,
    scheme: ODSTheme,
    props: ODSAvatarProps,
) {
    if (props.badgeType == ODSAvatarBadgeType.NUMBER) {
        props.badgeNumberProps?.let {
            ODSBadgeNumber(
                modifier = modifier,
                props = it.toODSBadgeNumberProps(size = props.size),
                scheme = scheme,
            )
        }
    } else {
        props.badgeIconProps?.let {
            ODSBadgeIcon(
                modifier = modifier,
                props = it.toODSBadgeIconProps(size = props.size),
                scheme = scheme,
            )
        }
    }
}

@Composable
private fun ODSAvatarVariant(
    props: ODSAvatarProps,
    style: ODSAvatarStyle
) {
    ODSImage(
        imageModel = props.image,
        height = style.imageHeight,
        width = style.imageWidth,
        cornerRadius = style.imageBorderRadius,
        contentScale = style.imageObjectFit ?: ContentScale.Crop,
    )
}

@Composable
private fun ODSIconVariant(
    props: ODSAvatarProps,
    style: ODSAvatarStyle,
) {
    ODSIcon(
        width = style.iconWidth,
        height = style.iconHeight,
        iconModel = props.icon,
        tint = style.iconColor?.getColor(),
    )
}

@Composable
private fun ODSInitialsVariant(
    props: ODSAvatarProps,
    style: ODSAvatarStyle,
) {
    val context = LocalContext.current
    val initials = props.initials.orEmpty()
    val contentDescription = context.getString(R.string.semantic_profile_name_initials, initials)
    if (!props.initials.isNullOrEmpty()) {
        ODSText(
            modifier = Modifier.clearAndSetSemantics {
                this.contentDescription = contentDescription
            },
            text = props.initials,
            style = style.initialsTextStyle,
            color = style.initialsColor,
            textAlign = style.initialsTextAlign,
            maxLines = SINGLE_LINE
        )
    }
}

private fun getAlignment(badgeType: ODSAvatarBadgeType, style: ODSAvatarStyle): Alignment {
    return when (badgeType) {
        ODSAvatarBadgeType.NUMBER -> style.odsBadgeNumberContentAlignment ?: Alignment.TopEnd
        ODSAvatarBadgeType.ICON -> style.odsBadgeIconContentAlignment ?: Alignment.TopEnd
    }
}

private fun getOffset(badgeType: ODSAvatarBadgeType, style: ODSAvatarStyle): ODSOffset? {
    return when (badgeType) {
        ODSAvatarBadgeType.NUMBER -> style.odsBadgeNumberOffset
        ODSAvatarBadgeType.ICON -> style.odsBadgeIconOffset
    }
}
