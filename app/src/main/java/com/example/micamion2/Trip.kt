package com.example.micamion2

import androidx.annotation.StringDef

class Trip {
    @StringDef(DELIVERED, INPROGESS, TOASSIGN, CANCELLED)
    annotation class Status

    companion object {
        const val DELIVERED = "DE"
        const val INPROGESS = "IP"
        const val TOASSIGN = "TA"
        const val CANCELLED = "CA"

    }

    var loadOwner: Int = 0
    var trailer: Int = 0
    var load: Int = 0
    var pickUp: Int = 0
    var dropOff: Int = 0
    @Status
    var status: String = TOASSIGN

    override fun toString(): String {
        return "$loadOwner $trailer $load $pickUp $dropOff $status"
    }
}
