package com.telekom.odsystem.charts.core.cartesian

import com.telekom.odsystem.charts.core.cartesian.data.CartesianChartModel

/** Defines when an automatic scroll should be performed. */
public fun interface AutoScrollCondition {
    /**
     * Given a chart’s new and old models, defines whether an automatic scroll should be performed.
     */
    public fun shouldScroll(oldModel: CartesianChartModel?, newModel: CartesianChartModel): Boolean

    public companion object {
        /** Prevents any automatic scrolling from occurring. */
        public val Never: AutoScrollCondition = AutoScrollCondition { _, _ -> false }

        /**
         * Triggers an automatic scroll when the size of the model increases (that is, the contents of
         * the chart become wider).
         */
        public val OnModelGrowth: AutoScrollCondition = AutoScrollCondition { oldModel, newModel ->
            oldModel != null &&
                    (newModel.models.size > oldModel.models.size || newModel.width > oldModel.width)
        }
    }
}
