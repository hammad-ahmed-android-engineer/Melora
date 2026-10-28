package com.example.melora

import android.animation.ObjectAnimator
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.melora.core.designsystem.components.CustomVolumeMeter
import com.example.melora.presentation.main.MainViewModel
import com.example.melora.presentation.navigation.MeloraNavHost
import com.example.melora.core.designsystem.MeloraTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val audioManager: AudioManager by lazy {
        getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val fadeOut = ObjectAnimator.ofFloat(
                splashScreenView.view,
                View.ALPHA,
                1f,
                0f
            ).apply {
                interpolator = AccelerateDecelerateInterpolator()
                duration = 320L
                doOnEnd { splashScreenView.remove() }
            }
            fadeOut.start()
        }

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            MeloraTheme(themeMode = uiState.appPreferences.themeMode) {
                Box(modifier = Modifier.fillMaxSize()) {
                    MeloraNavHost(
                        playbackState = uiState.playbackState,
                        onTogglePlayPause = { viewModel.togglePlayPause() },
                        onSkipNext = { viewModel.skipToNext() },
                        onPermissionGranted = { viewModel.refreshLibrary() },
                        modifier = Modifier.fillMaxSize()
                    )

                    CustomVolumeMeter(
                        volumePercent = uiState.volumeState.volumePercent,
                        isVisible = uiState.volumeState.isVisible,
                        onVolumeChange = { newPercent ->
                            val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).coerceAtLeast(1)
                            val target = (newPercent * max).roundToInt().coerceIn(0, max)
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, target, 0)
                            viewModel.showVolume(newPercent)
                        }
                    )
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            val direction = if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                AudioManager.ADJUST_RAISE
            } else {
                AudioManager.ADJUST_LOWER
            }
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, direction, 0)
            val current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).coerceAtLeast(1)
            viewModel.showVolume(current.toFloat() / max.toFloat())
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            return true
        }
        return super.onKeyUp(keyCode, event)
    }
}
