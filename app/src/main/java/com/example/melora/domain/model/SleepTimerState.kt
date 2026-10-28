package com.example.melora.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class SleepTimerState(
    val remainingTimeSeconds: Long = 0L,
    val isTimerActive: Boolean = false
)
