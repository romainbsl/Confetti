@file:OptIn(
    ExperimentalHorologistComposeLayoutApi::class,
    ExperimentalHorologistBaseUiApi::class, ExperimentalHorologistAuthDataApi::class
)

package dev.johnoreilly.confetti.wear.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.Text
import com.google.android.horologist.auth.data.ExperimentalHorologistAuthDataApi
import com.google.android.horologist.base.ui.ExperimentalHorologistBaseUiApi
import com.google.android.horologist.base.ui.components.StandardChip
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScalingLazyColumnState
import com.google.android.horologist.compose.navscaffold.ExperimentalHorologistComposeLayoutApi
import dev.johnoreilly.confetti.wear.ui.ConfettiTheme
import dev.johnoreilly.confetti.wear.ui.previews.WearPreviewDevices
import dev.johnoreilly.confetti.wear.ui.previews.WearPreviewFontSizes

@Composable
fun SettingsListView(
    uiState: SettingsUiState,
    conferenceCleared: () -> Unit,
    navigateToGoogleSignIn: () -> Unit,
    navigateToGoogleSignOut: () -> Unit,
    columnState: ScalingLazyColumnState
) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        columnState = columnState,
    ) {
        item {
            ListHeader {
                Text(text = "Settings")
            }
        }

        item {
            StandardChip(
                label = "Change Conference",
                onClick = conferenceCleared,
                enabled = !((uiState as? SettingsUiState.Success)?.conference.isNullOrBlank())
            )
        }

        item {
            if ((uiState as? SettingsUiState.Success)?.authUser == null) {
                StandardChip(
                    label = "Sign In",
                    onClick = navigateToGoogleSignIn,
                )
            } else {
                StandardChip(
                    label = "Sign Out",
                    onClick = navigateToGoogleSignOut,
                )
            }
        }
    }
}

@WearPreviewDevices
@WearPreviewFontSizes
@Composable
fun SettingsListViewPreview() {
    ConfettiTheme {
        SettingsListView(
            conferenceCleared = {},
            columnState = ScalingLazyColumnDefaults.belowTimeText().create(),
            navigateToGoogleSignIn = { },
            navigateToGoogleSignOut = { },
            uiState = SettingsUiState.Success(null, "conf")
        )
    }
}
