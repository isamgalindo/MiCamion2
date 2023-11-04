package com.example.micamion2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalTime

class DarkModeViewModel: ViewModel() {
    val currentTheme: MutableLiveData<Boolean> = MutableLiveData(isAfter6pm())

    fun updateTheme() {
        currentTheme.value = isAfter6pm()
    }

    private fun isAfter6pm(): Boolean {
        return LocalTime.now().isAfter(LocalTime.of(18, 0))
    }

}