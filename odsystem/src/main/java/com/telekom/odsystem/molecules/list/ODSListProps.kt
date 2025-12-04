package com.telekom.odsystem.molecules.list

import android.R.attr.type
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.listitem.ODSListItemProps
import com.telekom.odsystem.atoms.listitem.ODSListItemVariant
import java.util.UUID

enum class ODSListVariant {
    NUMBERED,
    BULLETED,
    ICON,
}

/**
 * Properties configuring a multilevel list.
 *
 * @property type Deprecated. Visual representation of list markers.
 * @property items Top-level list items.
 * @property variant Visual representation of list markers.
 */
data class ODSListProps(
    var variant: ODSListVariant = ODSListVariant.NUMBERED,
    var items: List<ODSListFirstLevelModel>? = null
)

/**
 * Model of a first level list item.
 *
 * @property id Unique identifier for diffing.
 * @property listItemProps Display properties for the item.
 * @property items Children at the next level.
 */
data class ODSListFirstLevelModel(
    var id: String = UUID.randomUUID().toString(), // Not exported by plugin
    var listItemProps: ODSListListItemProps,
    var items: List<ODSListSecondLevelModel> = listOf()
)

/**
 * Model of a second level list item.```
 *
 * @property id Unique identifier for diffing.
 * @property listItemProps Display properties for the item.
 * @property items Children at the next level.
 */
data class ODSListSecondLevelModel(
    var id: String = UUID.randomUUID().toString(), // Not exported by plugin
    var listItemProps: ODSListListItemProps,
    var items: List<ODSListThirdLevelModel> = listOf()
)

/**
 * Model of a third level list item.
 *
 * @property id Unique identifier for diffing.
 * @property listItemProps Display properties for the item.
 */
data class ODSListThirdLevelModel(
    var id: String = UUID.randomUUID().toString(), // Not exported by plugin
    var listItemProps: ODSListListItemProps,
)

/**
 * Properties describing a single list item.
 *
 * @property icon Optional icon used as prefix.
 * @property link Whether the item is clickable.
 * @property text Display text of the item.
 */
data class ODSListListItemProps(
    var icon: ODSIconModel? = null,
    var link: Boolean = false,
    var text: String? = null,
)

fun ODSListListItemProps.toODSFirstLevelListItemProps(
    variant: ODSListVariant,
    number: String
): ODSListItemProps {
    return ODSListItemProps(
        icon = icon,
        link = link,
        number = number,
        variant = firstLevelListItemPrefix(variant = variant),
        text = text,
    )
}

fun firstLevelListItemPrefix(variant: ODSListVariant): ODSListItemVariant {
    return when (variant) {
        ODSListVariant.NUMBERED -> ODSListItemVariant.NUMBER
        ODSListVariant.BULLETED -> ODSListItemVariant.BULLETPOINT
        ODSListVariant.ICON -> ODSListItemVariant.ICON
    }
}

fun ODSListListItemProps.toODSInnerLevelListItemProps(
    variant: ODSListVariant,
    number: String
): ODSListItemProps {
    return ODSListItemProps(
        icon = icon,
        link = link,
        number = number,
        variant = innerLevelListItemPrefix(variant = variant),
        text = text,
    )
}

fun innerLevelListItemPrefix(variant: ODSListVariant): ODSListItemVariant {
    return when (variant) {
        ODSListVariant.NUMBERED -> ODSListItemVariant.NUMBER
        ODSListVariant.BULLETED -> ODSListItemVariant.OUTLINE_BULLET
        ODSListVariant.ICON -> ODSListItemVariant.ICON
    }
}
