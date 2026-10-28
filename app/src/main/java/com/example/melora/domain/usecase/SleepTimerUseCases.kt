package com.example.melora.domain.usecase

import com.example.melora.domain.repository.SleepTimerRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetSleepTimerRemainingTimeUseCase @Inject constructor(
    private val sleepTimerRepository: SleepTimerRepository
) {
    operator fun invoke(): StateFlow<Long> = sleepTimerRepository.remainingTimeSeconds
}

class GetIsSleepTimerActiveUseCase @Inject constructor(
    private val sleepTimerRepository: SleepTimerRepository
) {
    operator fun invoke(): StateFlow<Boolean> = sleepTimerRepository.isTimerActive
}

class StartSleepTimerUseCase @Inject constructor(
    private val sleepTimerRepository: SleepTimerRepository
) {
    operator fun invoke(durationMinutes: Int, onTimerFinished: () -> Unit) {
        sleepTimerRepository.startTimer(durationMinutes, onTimerFinished)
    }
}

class CancelSleepTimerUseCase @Inject constructor(
    private val sleepTimerRepository: SleepTimerRepository
) {
    operator fun invoke() {
        sleepTimerRepository.cancelTimer()
    }
}
