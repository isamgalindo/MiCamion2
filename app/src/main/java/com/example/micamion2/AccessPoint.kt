package com.example.micamion2

import androidx.annotation.StringDef
import org.threeten.bp.LocalDateTime
import java.time.LocalTime

class AccessPoint {

    var country: String = ""
    var city: String = ""
    var address: String = ""



    override fun toString(): String {
        return "$country $city $address"

    }
}