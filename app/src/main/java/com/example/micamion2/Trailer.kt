package com.example.micamion2

import androidx.annotation.StringDef

class Trailer {
    @StringDef(LOAD_OWNER, DRIVER, TRAILER_OWNER)
    annotation class Status

    companion object {
        const val LOAD_OWNER = "LO"
        const val DRIVER = "DR"
        const val TRAILER_OWNER = "TO"
    }

    var plates: String = ""
    var capacity: Int = 0
    var volume: Int? = null
    var pickUp: String = ""
    var dropOff: String = ""
    var type: String = ""
    var driver: Int? = null
    var owner: Int? = null
    @Status
    var status: String = LOAD_OWNER

    override fun toString(): String {
        return "$plates $capacity $volume $pickUp $dropOff $type $driver $owner"
    }
}
