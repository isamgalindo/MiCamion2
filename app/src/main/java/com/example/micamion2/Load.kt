package com.example.micamion2

import androidx.annotation.StringDef

class Load {
    @StringDef(FLATBED, DRYVAN, REEFER, LOWBOY, STEPDECK, OTHER, ANY)
    annotation class TrailerType

    companion object {
        const val FLATBED = "FB"
        const val DRYVAN = "DR"
        const val REEFER = "RF"
        const val LOWBOY = "LB"
        const val STEPDECK = "SD"
        const val OTHER = "OT"
        const val ANY = "AN"

    }

    var id:Int? = null
    var type: String = ""
    var weight: Int = 0
    var volume: Int = 0
    @TrailerType
    var trailerType: String = FLATBED

    override fun toString(): String {
        return "$type $weight $volume $trailerType $id"
        }
}