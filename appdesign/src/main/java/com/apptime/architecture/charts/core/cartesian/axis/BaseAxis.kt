package com.telekom.odsystem.charts.core.cartesian.axis

import android.graphics.RectF
import androidx.compose.runtime.Immutable
import com.telekom.odsystem.charts.core.cartesian.data.CartesianValueFormatter
import com.telekom.odsystem.charts.core.common.MeasuringContext
import com.telekom.odsystem.charts.core.common.component.LineComponent
import com.telekom.odsystem.charts.core.common.component.TextComponent
import com.telekom.odsystem.charts.core.common.orZero
import setAll

/**
 * A base [Axis] implementation. This is extended by [HorizontalAxis] and [VerticalAxis].
 *
 * @property line used for the axis line.
 * @property label used for the labels.
 * @property labelRotationDegrees the label rotation (in degrees).
 * @property valueFormatter formats the values.
 * @property tick used for the ticks.
 * @property tickLengthDp the tick length (in dp).
 * @property guideline used for the guidelines.
 * @property size determines how the [BaseAxis] sizes itself.
 * @property titleComponent the title [TextComponent].
 * @property title the title text.
 */
abstract class BaseAxis<P : Axis.Position>(
    protected val line: LineComponent?,
    protected val label: TextComponent?,
    protected val labelRotationDegrees: Float,
    protected val valueFormatter: CartesianValueFormatter,
    protected val tick: LineComponent?,
    protected val tickLengthDp: Float,
    protected val guideline: LineComponent?,
    protected val size: Size,
    protected val titleComponent: TextComponent?,
    protected val title: CharSequence?,
) : Axis<P> {
    private val restrictedBounds: MutableList<RectF> = mutableListOf()

    override val bounds: RectF = RectF()

    protected val MeasuringContext.lineThickness: Float
        get() = line?.thicknessDp.orZero.pixels

    protected val MeasuringContext.tickThickness: Float
        get() = tick?.thicknessDp.orZero.pixels

    protected val MeasuringContext.guidelineThickness: Float
        get() = guideline?.thicknessDp.orZero.pixels

    protected val MeasuringContext.tickLength: Float
        get() = if (tick != null) tickLengthDp.pixels else 0f

    override fun setRestrictedBounds(vararg bounds: RectF?) {
        restrictedBounds.setAll(bounds.filterNotNull())
    }

    protected fun isNotInRestrictedBounds(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
    ): Boolean =
        restrictedBounds.none {
            it.contains(left, top, right, bottom) || it.intersects(left, top, right, bottom)
        }

    override fun equals(other: Any?): Boolean =
        this === other ||
                other is BaseAxis<*> &&
                position == other.position &&
                line == other.line &&
                label == other.label &&
                labelRotationDegrees == other.labelRotationDegrees &&
                valueFormatter == other.valueFormatter &&
                tick == other.tick &&
                tickLengthDp == other.tickLengthDp &&
                guideline == other.guideline &&
                size == other.size &&
                titleComponent == other.titleComponent &&
                title == other.title

    override fun hashCode(): Int {
        var result = line.hashCode()
        result = 31 * result + position.hashCode()
        result = 31 * result + label.hashCode()
        result = 31 * result + labelRotationDegrees.hashCode()
        result = 31 * result + valueFormatter.hashCode()
        result = 31 * result + tick.hashCode()
        result = 31 * result + tickLengthDp.hashCode()
        result = 31 * result + guideline.hashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + titleComponent.hashCode()
        result = 31 * result + title.hashCode()
        return result
    }

    /**
     * Determines how a [BaseAxis] sizes itself.
     * - For [VerticalAxis], this defines the width.
     * - For [HorizontalAxis], this defines the height.
     */
    @Immutable
    sealed class Size {
        /**
         * Allows a [BaseAxis] to adopt its preferred size from the given range.
         *
         * @property minDp the minimum size (in dp).
         * @property maxDp the maximum size (in dp).
         */
        class Auto(val minDp: Float = 0f, val maxDp: Float = Float.MAX_VALUE) :
            Size() {
            override fun equals(other: Any?): Boolean =
                this === other || other is Auto && minDp == other.minDp && maxDp == other.maxDp

            override fun hashCode(): Int = 31 * minDp.hashCode() + maxDp.hashCode()
        }

        /**
         * Instructs a [BaseAxis] to adopt the given size.
         *
         * @property valueDp the size (in dp).
         */
        class Fixed(val valueDp: Float) : Size() {
            override fun equals(other: Any?): Boolean =
                this === other || other is Fixed && valueDp == other.valueDp

            override fun hashCode(): Int = valueDp.hashCode()
        }

        /**
         * The axis will use a fraction of the available space.
         *
         * @property fraction the fraction of the available space that the axis should use.
         */
        class Fraction(val fraction: Float) : Size() {
            init {
                require(fraction in MIN..MAX) {
                    "Expected a value in the interval [$MIN, $MAX]. Got $fraction."
                }
            }

            override fun equals(other: Any?): Boolean =
                this === other || other is Fraction && fraction == other.fraction

            override fun hashCode(): Int = fraction.hashCode()

            private companion object {
                const val MIN = 0f
                const val MAX = 0.5f
            }
        }

        /**
         * The axis will measure the size of its label component ([label]) for the given [String]
         * ([text]), and it will use this size. In the case of [VerticalAxis], the width of the axis
         * line and the tick length will also be considered.
         */
        class Text(val text: CharSequence) : Size() {
            override fun equals(other: Any?): Boolean =
                this === other || other is Text && text == other.text

            override fun hashCode(): Int = text.hashCode()
        }

        /** Provides access to [BaseAxis.Size] factory functions. */
        companion object
    }
}
