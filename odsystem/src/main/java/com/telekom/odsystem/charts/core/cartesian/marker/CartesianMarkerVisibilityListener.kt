package com.telekom.odsystem.charts.core.cartesian.marker

import androidx.compose.runtime.Immutable

/** Allows for listening to [CartesianMarker] visibility changes. */
@Immutable
public interface CartesianMarkerVisibilityListener {
  /** Called when the specified [CartesianMarker] is shown. */
  public fun onShown(marker: CartesianMarker, targets: List<CartesianMarker.Target>) {}

  /** Called when the specified [CartesianMarker]’s [CartesianMarker.Target]s change. */
  public fun onUpdated(marker: CartesianMarker, targets: List<CartesianMarker.Target>) {}

  /** Called when the specified [CartesianMarker] is hidden. */
  public fun onHidden(marker: CartesianMarker) {}
}
