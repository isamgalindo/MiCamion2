package com.example.micamion2

import androidx.annotation.StringDef
import org.threeten.bp.LocalDateTime
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date
import java.util.Locale

class AccessPoint {

    var country: String = ""
    var city: String = ""
    var address: String = ""
    var before: String = "" // "dd/mm/yyyy" format
    var after:String=""





    override fun toString(): String {
        return "$country $city $address $before $after"

    }
}