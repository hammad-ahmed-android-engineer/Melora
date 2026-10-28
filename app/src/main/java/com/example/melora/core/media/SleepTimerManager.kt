package com.example.melora.core.media

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class SleepTimerManager @Inject constructor() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var timerJob: Job? = null

    private val _remainingTimeSeconds = MutableStateFlow(0L)
    val remainingTimeSeconds: StateFlow<Long> = _remainingTimeSeconds.asStateFlow()

    private val _isTimerActive = MutableStateFlow(false)
    val isTimerActive: StateFlow<Boolean> = _isTimerActive.asStateFlow()

    fun startTimer(durationMinutes: Int, onTimerFinished: () -> Unit) {
        cancelTimer()
        val totalSeconds = durationMinutes * 60L
        _remainingTimeSeconds.value = totalSeconds
        _isTimerActive.value = true

        timerJob = scope.launch {
            var current = totalSeconds
            while (current > 0) {
                delay(1000L.milliseconds)
                current--
                _remainingTimeSeconds.value = current
            }
            _isTimerActive.value = false
            _remainingTimeSeconds.value = 0L
            onTimerFinished()
        }
    }

    fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
        _isTimerActive.value = false
        _remainingTimeSeconds.value = 0L
    }
}
