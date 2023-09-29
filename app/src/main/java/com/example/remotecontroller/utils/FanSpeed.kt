package com.example.remotecontroller.utils

import com.example.remotecontroller.R

enum class FanSpeed(val labelNumber: Int, val labelWord: Int) {
    OFF(R.string.fan_off_no, R.string.fan_off),
    LOW(R.string.fan_low_no, R.string.fan_low),
    MEDIUM(R.string.fan_medium_no, R.string.fan_medium),
    HIGH(R.string.fan_high_no, R.string.fan_high),
    HIGHEST(R.string.fan_highest_no, R.string.fan_highest);

    fun next() = when (this) {
        OFF -> LOW
        LOW -> MEDIUM
        MEDIUM -> HIGH
        HIGH -> HIGHEST
        HIGHEST -> OFF
    }
}