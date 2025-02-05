package dev.johnoreilly.confetti.wear.sessiondetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.johnoreilly.confetti.R
import dev.johnoreilly.confetti.fragment.SpeakerDetails
import dev.johnoreilly.confetti.fullNameAndCompany
import dev.johnoreilly.confetti.navigation.SpeakerDetailsKey

@Composable
fun SessionSpeakerInfo(
    conference: String,
    modifier: Modifier = Modifier,
    speaker: SpeakerDetails,
    navigateToSpeaker: (SpeakerDetailsKey) -> Unit
) {
    Column(modifier = modifier) {
        Row(Modifier.clickable {
            navigateToSpeaker(SpeakerDetailsKey(conference, speaker.id)) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                placeholder = painterResource(R.drawable.ic_person_black_24dp),
                error = painterResource(R.drawable.ic_person_black_24dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(speaker.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = speaker.name,
            )

            Spacer(modifier = Modifier.size(4.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = speaker.fullNameAndCompany(),
                    style = MaterialTheme.typography.body1,
                )
            }
        }
    }
}

