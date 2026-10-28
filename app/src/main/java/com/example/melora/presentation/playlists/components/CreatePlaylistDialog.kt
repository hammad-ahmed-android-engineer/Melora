package com.example.melora.presentation.playlists.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import com.example.melora.core.designsystem.components.MeloraTextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.melora.core.designsystem.components.MeloraDialog
import com.example.melora.core.designsystem.components.MeloraButton
import com.example.melora.core.designsystem.components.MeloraTextField
import com.example.melora.R
import com.example.melora.core.designsystem.AppTypography

/**
 * Dialog for creating a new playlist.
 */
@Composable
fun CreatePlaylistDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit = {},
    onCreate: (String) -> Unit = onConfirm
) {
    val submitAction = if (onConfirm != {}) onConfirm else onCreate
    var nameText by remember { mutableStateOf("") }

    MeloraDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.new_playlist)
    ) {
        MeloraTextField(
            value = nameText,
            onValueChange = { nameText = it },
            label = stringResource(R.string.playlist_name),
            singleLine = true,
            modifier = Modifier.padding(bottom = 16.dp)
        )
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
                text = stringResource(R.string.create),
                onClick = {
                    if (nameText.isNotBlank()) {
                        submitAction(nameText.trim())
                    }
                }
            )
        }
    }
}
