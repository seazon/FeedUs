package com.seazon.feedme.lib.utils

fun Int?.orZero(): Int = this ?: 0
fun Long?.orZero(): Long = this ?: 0
fun Double?.orZero(): Double = this ?: 0.0
fun Float?.orZero(): Float = this ?: 0f