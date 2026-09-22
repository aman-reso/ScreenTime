package com.telekom.odsystem.charts.core.common

/** Represents a point in a coordinate system. */
@JvmInline
value class Point internal constructor(private val packedValue: Long) {
  /** The _x_ coordinate. */
  val x: Float
    get() = unpackFloat1(packedValue)

  /** The _y_ coordinate. */
  val y: Float
    get() = unpackFloat2(packedValue)

  constructor(x: Float, y: Float) : this(packFloats(x, y))

  /**
   * Copies this [Point], updating one or both of the coordinates. If providing new values for both
   * [x] and [y], consider creating a new [Point] using one of the helper functions instead.
   */
  fun copy(x: Float = this.x, y: Float = this.y): Point = Point(x, y)

  override fun toString(): String = "Point(x=$x, y=$y)"
}
