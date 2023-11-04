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

    var loadOwner: Int = -1
    var trailer: Int = -1
    var load: Int = -1
    var pickup: Int = -1
    var dropoff: Int = -1
    @Status
    var status: String = TOASSIGN

    override fun toString(): String {
        return "$loadOwner $trailer $load $pickup $dropoff $status"
    }
}
