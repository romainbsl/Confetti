package dev.johnoreilly.confetti.work

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import dev.johnoreilly.confetti.ConferenceRefresh

class WorkManagerConferenceRefresh(
    private val workManager: WorkManager
): ConferenceRefresh {
    override fun refresh(conference: String) {
        if (conference.isNotEmpty()) {
            workManager.enqueueUniqueWork(
                RefreshWorker.WorkRefresh(conference),
                ExistingWorkPolicy.KEEP,
                RefreshWorker.oneOff(conference)
            )
        }
    }
}

fun setupDailyRefresh(workManager: WorkManager) {
    workManager.enqueueUniquePeriodicWork(
        RefreshWorker.WorkDaily,
        ExistingPeriodicWorkPolicy.UPDATE,
        RefreshWorker.dailyRefresh()
    )
}