package com.example.micamion2

import androidx.annotation.StringDef

class Trailer {
    @StringDef(INTRANSIT, AVAILABLE, UNAVAILABLE)
    annotation class Status
    @StringDef(FLATBED, DRYVAN, REEFER, LOWBOY, STEPDECK, OTHER, ANY)
    annotation class Type
    companion object {
        const val AVAILABLE = "AV"
        const val INTRANSIT = "IT"
        const val UNAVAILABLE= "UN"
        const val FLATBED = "FB"
        const val DRYVAN = "DV"
        const val REEFER= "RF"
        const val LOWBOY = "LB"
        const val STEPDECK = "SD"
        const val OTHER= "OT"
        const val ANY= "AN"
    }
    var plates: String = ""
    var capacity: Int = 0
    var volume: Int = 0
    var pickup: String = ""
    var dropoff: String = ""
    var driver: Int? = null
    var owner: Int? = null
    @Type
    var type: String = FLATBED
    @Status
    var status: String = INTRANSIT


    override fun toString(): String {
        return "$plates $capacity $volume $pickup $dropoff $type $driver $owner"
    }
}
