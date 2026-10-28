package com.example.melora.core.designsystem.components

import androidx.compose.foundation.clickable
import com.example.melora.core.utils.clickableDebounced
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Arrangement
import com.example.melora.core.designsystem.components.MeloraTextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.melora.core.designsystem.components.MeloraDialog
import com.example.melora.core.designsystem.components.MeloraRadioButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.melora.R
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.AppTypography
import com.example.melora.core.utils.capitalizeFirst
import com.example.melora.domain.model.SortDirection
import com.example.melora.domain.model.SortField
import com.example.melora.domain.model.SortOption

/**
 * Dialog component for choosing library sort options.
 */
@Composable
fun SortDialog(
    currentSortOption: SortOption,
    onSortSelected: (SortOption) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedField by remember { mutableStateOf(currentSortOption.field) }
    var selectedDirection by remember { mutableStateOf(currentSortOption.direction) }

    MeloraDialog(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.sort_songs)
    ) {
        Column {
            AppTypography.TitleSmall(text = stringResource(R.string.sort_by))
            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
            SortField.entries.forEach { field ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickableDebounced { selectedField = field }
                        .padding(vertical = AppDimensions.paddingTiny),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MeloraRadioButton(
                        selected = (selectedField == field),
                        onClick = { selectedField = field }
                    )
                    Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
                    AppTypography.BodyMedium(text = field.name.lowercase().replace('_', ' ').capitalizeFirst())
                }
            }
            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
            AppTypography.TitleSmall(text = stringResource(R.string.direction))
            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
            SortDirection.entries.forEach { direction ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickableDebounced { selectedDirection = direction }
                        .padding(vertical = AppDimensions.paddingTiny),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MeloraRadioButton(
                        selected = (selectedDirection == direction),
                        onClick = { selectedDirection = direction }
                    )
                    Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
                    AppTypography.BodyMedium(text = direction.name.lowercase().capitalizeFirst())
                }
            }
        }
        Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            MeloraTextButton(
                text = stringResource(R.string.cancel),
                onClick = onDismiss
            )
            Spacer(modifier = Modifier.width(AppDimensions.paddingSmall))
            MeloraTextButton(
                text = stringResource(R.string.apply),
                onClick = {
                    onSortSelected(SortOption(selectedField, selectedDirection))
                    onDismiss()
                }
            )
        }
    }
}
