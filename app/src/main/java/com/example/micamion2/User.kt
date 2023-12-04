package com.example.micamion2

import androidx.annotation.StringDef

class User {
    @StringDef(LOAD_OWNER, DRIVER, TRAILER_OWNER)
    annotation class UserType

    companion object {
        const val LOAD_OWNER = "LO"
        const val DRIVER = "DR"
        const val TRAILER_OWNER = "TO"
    }

    var id: Int=0
    var username: String? = null
    var name: String = ""
    var last_name: String = ""
    var email: String = ""
    var password: String = ""
    var phone: String = ""
    @UserType
    var userType: String = LOAD_OWNER

    override fun toString(): String {
        return " $id $name $last_name $email $phone $userType"
    }
}
