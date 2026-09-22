package com.telekom.odsystem.slots.groupoftags

import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps

/**
 * Properties describing a pair of tags placed side by side.
 *
 * @property leadingTagProps Tag shown at the start of the group.
 * @property trailingTagProps Tag shown at the end of the group.
 */
data class ODSGroupOfTagsProps(
    var leadingTagProps: ODSTagStaticProps? = null,
    var trailingTagProps: ODSTagStaticProps? = null
)
