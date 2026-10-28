package com.example.melora.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface SleepTimerRepository {
    val remainingTimeSeconds: StateFlow<Long>
    val isTimerActive: StateFlow<Boolean>
    fun startTimer(durationMinutes: Int, onTimerFinished: () -> Unit)
    fun cancelTimer()
}
