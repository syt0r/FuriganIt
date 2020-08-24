package ua.syt0r.furiganit.core.review

import android.app.Activity
import android.content.Context
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import ua.syt0r.furiganit.core.app_data.AppDataRepository
import java.lang.ref.WeakReference

class ReviewManager(
    context: Context,
    private val appDataRepository: AppDataRepository
) {

    companion object {
        private const val SUCCESSFULLY_FURIGANIZED_TEXTS_TO_SHOW_REVIEW = 20
    }

    private val manager by lazy {
        ReviewManagerFactory.create(context)
    }


    fun tryToShowReviewDialog(activity: Activity): Flow<ReviewFlowResult> {
        return if (shouldShowReviewDialog()) {
            showReviewFlow(activity)
                .onEach {
                    if (it == ReviewFlowResult.SUCCESS) {
                        appDataRepository.furiganizedTextsCountSinceReview = 0
                    }
                }
        } else {
            flowOf(ReviewFlowResult.SKIP)
        }

    }

    private fun shouldShowReviewDialog(): Boolean {
        return appDataRepository.furiganizedTextsCountSinceReview > SUCCESSFULLY_FURIGANIZED_TEXTS_TO_SHOW_REVIEW
    }

    private fun showReviewFlow(activity: Activity) = callbackFlow<ReviewFlowResult> {
        val activityReference = WeakReference(activity)

        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { response ->
            if (response.isSuccessful) {

                val reviewInfo = response.result
                val activity = activityReference.get() ?: return@addOnCompleteListener
                val flow = manager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener {
                    offer(ReviewFlowResult.SUCCESS)
                }

            } else {
                offer(ReviewFlowResult.ERROR)
            }
        }

        awaitClose { }
    }

}