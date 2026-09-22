import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.icon.ODSIconModel

enum class ODSListRowNavigationVariant {
    /** Default list row with text only. */
    STANDARD,

    /** Row with a leading icon. */
    ICON,

    /** Row displaying an image. */
    IMAGE,
}

/**
 * Properties used to configure an ODS navigation list row.
 *
 * @property descriptionText Secondary text shown below the label.
 * @property descriptionTitle Title displayed above the description text.
 * @property labelText Primary label text.
 * @property variant Visual variant representing the row type.
 * @property showDescriptionTitle Indicates whether to display the description title.
 * @property image Optional image shown instead of an icon.
 * @property icon Optional icon displayed in the row.
 * @property label Primary label text. If `labelText` is set, it will be used as the label.
 */
data class ODSListRowNavigationProps(
    var descriptionText: String? = null,
    var descriptionTitle: String? = null,
    var labelText: String? = null,
    var label: String? = null,
    var icon: ODSIconModel? = null,
    var showDescriptionTitle: Boolean = true,
    var variant: ODSListRowNavigationVariant = ODSListRowNavigationVariant.STANDARD,
    var image: ODSImageModel? = null // Not exported from the plugin
)
