package com.example.melora.core.designsystem.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import com.example.melora.core.designsystem.components.MeloraTextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.melora.core.designsystem.components.MeloraDialog
import com.example.melora.core.designsystem.components.MeloraButton
import com.example.melora.R
import com.example.melora.domain.model.Song
import com.example.melora.core.designsystem.AppTypography

/**
 * Reusable confirmation dialog for deleting items or songs.
 */
@Composable
fun DeleteConfirmDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    MeloraDialog(
        onDismissRequest = onDismiss,
        title = title
    ) {
        AppTypography.BodyMedium(text = message, modifier = Modifier.padding(bottom = 16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            MeloraTextButton(
                text = stringResource(R.string.cancel),
                onClick = onDismiss
            )
            Spacer(modifier = Modifier.width(8.dp))
            MeloraButton(
                text = stringResource(R.string.delete),
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            )
        }
    }
}

@Composable
fun DeleteConfirmDialog(
    song: Song,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    DeleteConfirmDialog(
        title = stringResource(R.string.delete_song),
        message = stringResource(R.string.delete_song_confirm, song.title),
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}
