package com.example.melora.core.designsystem.components.dialogs

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
import com.example.melora.domain.model.Song

/**
 * Dialog for renaming a song's title.
 */
@Composable
fun RenameSongDialog(
    song: Song,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var titleText by remember { mutableStateOf(song.title) }

    MeloraDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.rename_song)
    ) {
        MeloraTextField(
            value = titleText,
            onValueChange = { titleText = it },
            label = stringResource(R.string.rename),
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
                text = stringResource(R.string.save),
                onClick = {
                    if (titleText.isNotBlank()) {
                        onConfirm(titleText.trim())
                    }
                }
            )
        }
    }
}
