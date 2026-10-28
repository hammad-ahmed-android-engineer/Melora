package com.example.melora.presentation.player.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lyrics
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import com.example.melora.core.designsystem.components.MeloraIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.melora.R
import com.example.melora.core.designsystem.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerTopBar(
    sourceName: String = "From your music library",
    onBackClick: () -> Unit,
    onOpenSongInfo: () -> Unit,
    onOpenAddToPlaylist: () -> Unit,
    onOpenLyrics: () -> Unit,
    onOpenSpeed: () -> Unit,
    onOpenSleepTimer: () -> Unit,
    onOpenEqualizer: () -> Unit,
    onShare: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AppTypography.LabelSmall(
                    text = stringResource(R.string.now_playing).uppercase(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
                AppTypography.TitleSmall(
                    text = sourceName,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        navigationIcon = {
            MeloraIconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            Box {
                MeloraIconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.cd_more_options),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { AppTypography.BodyMedium(text = stringResource(R.string.add_to_playlist)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.AutoMirrored.Filled.PlaylistAdd, contentDescription = null)
                        },
                        onClick = {
                            showMenu = false
                            onOpenAddToPlaylist()
                        }
                    )

                    DropdownMenuItem(
                        text = { AppTypography.BodyMedium(text = "Lyrics") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lyrics, contentDescription = null)
                        },
                        onClick = {
                            showMenu = false
                            onOpenLyrics()
                        }
                    )

                    DropdownMenuItem(
                        text = { AppTypography.BodyMedium(text = stringResource(R.string.speed_and_pitch)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Speed, contentDescription = null)
                        },
                        onClick = {
                            showMenu = false
                            onOpenSpeed()
                        }
                    )

                    DropdownMenuItem(
                        text = { AppTypography.BodyMedium(text = stringResource(R.string.cd_sleep_timer)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Timer, contentDescription = null)
                        },
                        onClick = {
                            showMenu = false
                            onOpenSleepTimer()
                        }
                    )

                    DropdownMenuItem(
                        text = { AppTypography.BodyMedium(text = stringResource(R.string.cd_equalizer)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Equalizer, contentDescription = null)
                        },
                        onClick = {
                            showMenu = false
                            onOpenEqualizer()
                        }
                    )

                    DropdownMenuItem(
                        text = { AppTypography.BodyMedium(text = stringResource(R.string.song_info)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Info, contentDescription = null)
                        },
                        onClick = {
                            showMenu = false
                            onOpenSongInfo()
                        }
                    )

                    DropdownMenuItem(
                        text = { AppTypography.BodyMedium(text = stringResource(R.string.share)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null)
                        },
                        onClick = {
                            showMenu = false
                            onShare()
                        }
                    )
                }
            }
        }
    )
}

