package dev.johnoreilly.confetti.sessiondetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.johnoreilly.confetti.fragment.SpeakerDetails
import dev.johnoreilly.confetti.ui.component.SocialIcon
import dev.johnoreilly.confetti.R
import dev.johnoreilly.confetti.fullNameAndCompany

@Composable
fun SessionSpeakerInfo(
    modifier: Modifier = Modifier,
    speaker: SpeakerDetails,
    onSocialLinkClick: (SpeakerDetails.Social, SpeakerDetails) -> Unit
) {
    Column(modifier.padding(top = 16.dp)) {
        Row {
            AsyncImage(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                placeholder = painterResource(R.drawable.ic_person_black_24dp),
                error = painterResource(R.drawable.ic_person_black_24dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(speaker.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = speaker.name,
            )

            Column(Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = speaker.fullNameAndCompany(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                speaker.city?.let { city ->
                    Text(
                        text = city,
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                speaker.bio?.let { bio ->
                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = bio,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }


                Row(
                    Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    speaker.socials.forEach { socialsItem ->
                        SocialIcon(
                            modifier = Modifier.size(24.dp),
                            socialItem = socialsItem,
                            onClick = { onSocialLinkClick(socialsItem, speaker) }
                        )
                    }
                }
            }
        }
    }
}

