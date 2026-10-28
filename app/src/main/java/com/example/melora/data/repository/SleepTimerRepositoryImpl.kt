package com.example.melora.data.repository

import com.example.melora.core.media.SleepTimerManager
import com.example.melora.domain.repository.SleepTimerRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SleepTimerRepositoryImpl @Inject constructor(
    private val sleepTimerManager: SleepTimerManager
) : SleepTimerRepository {

    override val remainingTimeSeconds: StateFlow<Long> = sleepTimerManager.remainingTimeSeconds
    override val isTimerActive: StateFlow<Boolean> = sleepTimerManager.isTimerActive

    override fun startTimer(durationMinutes: Int, onTimerFinished: () -> Unit) {
        sleepTimerManager.startTimer(durationMinutes, onTimerFinished)
    }

    override fun cancelTimer() {
        sleepTimerManager.cancelTimer()
    }
}
