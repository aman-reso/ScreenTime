package com.telekom.odsystem.atoms.timersegment

enum class ODSTimerSegmentStatus {
    IDLE,
    IN_PROGRESS,
    COMPLETE,
}

data class ODSTimerSegmentProps(
    var status: ODSTimerSegmentStatus = ODSTimerSegmentStatus.IDLE,
    var duration: Int = 0,
)
