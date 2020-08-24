package ua.syt0r.furiganit.app.main

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import ua.syt0r.furiganit.core.review.ReviewManager

class MainViewModel(
    private val reviewManager: ReviewManager
) : ViewModel() {

    fun tryToShowReviewFlow(activity: Activity) {
        reviewManager.tryToShowReviewDialog(activity).launchIn(viewModelScope)
    }

}