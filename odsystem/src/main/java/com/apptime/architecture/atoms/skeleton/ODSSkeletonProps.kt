package com.telekom.odsystem.atoms.skeleton

/**
 * Defines the visual variants for an [ODSSkeletonProps].
 *
 * This enum controls the appearance of a skeleton loader component,
 * allowing for different visual placeholders while content is loading.
 */
enum class ODSSkeletonVariant {
    /** A full-width or full-size skeleton representation. */
    FULL,
    /** A small-sized skeleton representation. */
    SMALL,
    /** A medium-sized skeleton representation. */
    MEDIUM,
    /** A large-sized skeleton representation. */
    LARGE,
}

/**
 * Represents the properties for configuring an ODS Skeleton component.
 *
 * Skeleton loaders provide a visual placeholder for content that is still loading,
 * improving perceived performance and user experience.
 *
 * @property variant The [ODSSkeletonVariant] that defines the visual style or size
 *                   of the skeleton placeholder. Defaults to [ODSSkeletonVariant.FULL].
 */
data class ODSSkeletonProps(
    var variant: ODSSkeletonVariant = ODSSkeletonVariant.FULL,
)
