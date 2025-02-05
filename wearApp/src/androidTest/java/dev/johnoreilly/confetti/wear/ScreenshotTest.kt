@file:OptIn(ExperimentalHorologistComposeLayoutApi::class, ExperimentalHorologistTilesApi::class)

package dev.johnoreilly.confetti.wear

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeSource
import androidx.wear.compose.material.TimeText
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.navscaffold.ExperimentalHorologistComposeLayoutApi
import com.google.android.horologist.compose.tools.TileLayoutPreview
import com.google.android.horologist.tiles.ExperimentalHorologistTilesApi
import dev.johnoreilly.confetti.GetConferencesQuery
import dev.johnoreilly.confetti.fragment.SessionDetails
import dev.johnoreilly.confetti.fragment.SessionDetails.Speaker
import dev.johnoreilly.confetti.fragment.SpeakerDetails
import dev.johnoreilly.confetti.navigation.ConferenceDayKey
import dev.johnoreilly.confetti.navigation.SessionDetailsKey
import dev.johnoreilly.confetti.utils.AndroidDateService
import dev.johnoreilly.confetti.wear.conferences.ConferencesUiState
import dev.johnoreilly.confetti.wear.conferences.ConferencesView
import dev.johnoreilly.confetti.wear.sessiondetails.SessionDetailView
import dev.johnoreilly.confetti.wear.sessiondetails.SessionDetailsUiState
import dev.johnoreilly.confetti.wear.sessions.SessionListView
import dev.johnoreilly.confetti.wear.sessions.SessionsUiState
import dev.johnoreilly.confetti.wear.tile.CurrentSessionsData
import dev.johnoreilly.confetti.wear.tile.CurrentSessionsTileRenderer
import dev.johnoreilly.confetti.wear.ui.ConfettiTheme
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.locale.LocaleTestRule
import kotlin.time.Duration.Companion.hours

class ScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<androidx.activity.ComponentActivity>()

    val sessionTime = LocalDateTime(2023, 1, 4, 9, 30)
    val date = sessionTime.date

    val sessionDetails = SessionDetails(
        id = "14997",
        title = "Kotlin DevRoom Welcoming Remarks",
        type = "talk",
        startsAt = sessionTime,
        endsAt = sessionTime.toInstant(TimeZone.UTC).plus(1.hours).toLocalDateTime(TimeZone.UTC),
        sessionDescription = "Welcoming participants to the Kotlin DevRoom @ FOSDEM 2023 - We're back in person!",
        language = "en-US",
        speakers = listOf(
            Speaker(
                __typename = "Speaker",
                speakerDetails = SpeakerDetails(
                    id = "6477",
                    name = "Nicola Corti",
                    photoUrl = null,
                    company = null,
                    companyLogoUrl = null,
                    city = null,
                    bio = null,
                    socials = listOf()
                )
            ), Speaker(
                __typename = "Speaker",
                speakerDetails = SpeakerDetails(
                    id = "7079",
                    name = "Martin Bonnin",
                    photoUrl = null,
                    company = null,
                    companyLogoUrl = null,
                    city = null,
                    bio = null,
                    socials = listOf()
                )
            ), Speaker(
                __typename = "Speaker",
                speakerDetails = SpeakerDetails(
                    id = "7934",
                    name = "Holger Steinhauer",
                    photoUrl = null,
                    company = null,
                    companyLogoUrl = null,
                    city = null,
                    bio = null,
                    socials = listOf()
                )
            )
        ),
        room = SessionDetails.Room(name = "UB5.230"),
        tags = listOf("Kotlin"),
        __typename = ""
    )

    @Test
    fun conferences() = screenshot(
        screenshotName = "a_conferences",
        checks = {
            composeTestRule.onNodeWithText("Fosdem 2023").assertIsDisplayed()
        }
    ) {
        ConferencesView(
            uiState = ConferencesUiState.Success(
                listOf(
                    GetConferencesQuery.Conference("0", emptyList(), "Fosdem 2023"),
                    GetConferencesQuery.Conference("1", emptyList(), "Droidcon London 2022"),
                    GetConferencesQuery.Conference("2", emptyList(), "DevFest Ukraine 2023"),
                )
            ),
            navigateToConference = {},
            columnState = ScalingLazyColumnDefaults.belowTimeText().create()
        )
    }

    @Test
    fun sessionList() {
        screenshot(
            screenshotName = "b_session_list",
            checks = {
                composeTestRule.onNodeWithText("Wednesday 9:30").assertIsDisplayed()
            }
        ) {
            SessionListView(
                uiState = SessionsUiState.Success(
                    ConferenceDayKey("fosdem", date),
                    sessionsByTime = listOf(
                        SessionsUiState.SessionAtTime(
                            sessionTime,
                            listOf(sessionDetails)
                        )
                    ),
                ),
                sessionSelected = {},
                columnState = ScalingLazyColumnDefaults.belowTimeText().create()
            )
        }
    }

    @Test
    fun sessionDetails() = screenshot(
        screenshotName = "c_session_details",
        checks = {
            composeTestRule.onNodeWithText("Wednesday 09:30").assertIsDisplayed()
        }
    ) {
        SessionDetailView(
            uiState = SessionDetailsUiState.Success(
                "wearconf",
                SessionDetailsKey("fosdem", "14997"),
                sessionDetails,
                TimeZone.UTC
            ),
            columnState = ScalingLazyColumnDefaults.belowTimeText().create(),
            navigateToSpeaker = {},
            formatter = { AndroidDateService().format(it, TimeZone.UTC, "eeee HH:mm") }
        )
    }

    @Test
    fun tilePreview() = screenshot(
        screenshotName = "d_tile",
        showScaffold = false,
    ) {
        val context = LocalContext.current

        val tileState = remember {
            CurrentSessionsData(
                "kotlinconf",
                sessionTime,
                listOf(
                    sessionDetails
                )
            )
        }
        val renderer = remember { CurrentSessionsTileRenderer(context) }

        TileLayoutPreview(tileState, tileState, renderer)
    }

    private fun screenshot(
        screenshotName: String,
        showScaffold: Boolean = true,
        checks: () -> Unit = {},
        block: @Composable () -> Unit
    ) {
        composeTestRule.setContent {
            if (showScaffold) {
                ConfettiTheme {
                    Scaffold(timeText = {
                        TimeText(timeSource = FixedTimeSource)
                    }) {
                        block()
                    }
                }
            } else {
                block()
            }
        }

        composeTestRule.onRoot().assertIsDisplayed()

        checks()

        val context = InstrumentationRegistry.getInstrumentation().context
        val isScreenRound = context.resources.configuration.isScreenRound
        val suffix = if (isScreenRound) "_round" else "_square"
        Screengrab.screenshot(
            screenshotName + suffix,
            WearScreenshotStrategy(isRoundDevice = isScreenRound)
        )
    }

    object FixedTimeSource : TimeSource {
        override val currentTime: String
            @Composable get() = "10:10"
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun configure() {
            val context = InstrumentationRegistry.getInstrumentation().context
            val isScreenRound = context.resources.configuration.isScreenRound

            Screengrab.setDefaultScreenshotStrategy(WearScreenshotStrategy(isScreenRound))
        }

        @ClassRule
        @JvmField
        val localeTestRule = LocaleTestRule()
    }
}