package com.tesji.citamedica.model

data class Schedule(
    val morning: ArrayList<HourInterval>,
    val afternoon: ArrayList<HourInterval>
)
