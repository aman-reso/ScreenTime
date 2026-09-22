package com.telekom.odsystem.organisms.barchart

/**
 * Defines the shape of the bars in the ODSBarChart.
 */
enum class ODSBarItemShape {
    PILLED,
    SQUARED,
}

/**
 * Defines the orientation of the bars in the ODSBarChart.
 */
enum class ODSBarItemDirection {
    HORIZONTAL,
    VERTICAL,
}

/**
 * Properties for the ODSBarChart component.
 *
 * @property barItemsList List of [ODSBarItemProps] representing the data points in the bar chart. Defaults to an empty list.
 * @property shape The shape of the bars in the chart. Can be [ODSBarItemShape.PILLED] or [ODSBarItemShape.SQUARED]. Defaults to [ODSBarItemShape.PILLED].
 * @property direction The orientation of the bars in the chart. Can be [ODSBarItemDirection.HORIZONTAL] or [ODSBarItemDirection.VERTICAL]. Defaults to [ODSBarItemDirection.VERTICAL].
 * @property showTopLabels Whether to display labels at the top of the chart. Defaults to true.
 * @property showBottomLabels Whether to display labels at the bottom of the chart. Defaults to true.
 * @property showLeftLabels Whether to display labels on the left side of the chart. Defaults to true.
 * @property showRightLabels Whether to display labels on the right side of the chart. Defaults to true.
 * @property stepValue The value for each step on the axis. If null, the step value will be calculated automatically. Defaults to null.
 * @property stepCount The number of steps on the axis. If null, the step count will be calculated automatically. Defaults to null.
 * @property zoomEnabled Whether zooming is enabled for the chart. Defaults to true.
 * @property scrollEnabled Whether scrolling is enabled for the chart. Defaults to true.
 */
data class ODSBarChartProps(
    var barItemsList: List<ODSBarItemProps> = emptyList(),
    val shape: ODSBarItemShape = ODSBarItemShape.PILLED,
    var direction: ODSBarItemDirection = ODSBarItemDirection.VERTICAL,
    var showTopLabels: Boolean = true,
    val showBottomLabels: Boolean = true,
    val showLeftLabels: Boolean = true,
    val showRightLabels: Boolean = true,
    val stepValue: Double? = null,
    val stepCount: Int? = null,
    val zoomEnabled: Boolean = true,
    val scrollEnabled: Boolean = true

)

/**
 * Represents the properties of a single bar item in the ODSBarChart.
 *
 * @property xValue The numerical value for the x-axis. Used for positioning the bar.
 * @property xLabel The label to be displayed on the x-axis for this bar item.
 * @property yValue The numerical value for the y-axis. Used for determining the height/width of the bar.
 * @property yLabel The label to be displayed on the y-axis for this bar item.
 */
data class ODSBarItemProps(
    val xValue: Double? = null,
    val xLabel: String? = null,
    val yValue: Double? = null,
    val yLabel: String? = null,
)
