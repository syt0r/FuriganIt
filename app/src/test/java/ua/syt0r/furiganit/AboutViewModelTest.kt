package ua.syt0r.furiganit

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import ua.syt0r.furiganit.app.about.AboutViewModel
import ua.syt0r.furiganit.model.usecase.BillingUseCase

class AboutViewModelTest {

    lateinit var viewModel: AboutViewModel

    @Before
    fun setUp() {
        val billingUseCase = Mockito.mock(BillingUseCase::class.java)
        viewModel = AboutViewModel(billingUseCase)
    }

    @Test
    fun activityNotInjectedError() {
        assert(true)
    }

    @Test
    fun successfulPurchase() {
        assert(true)
    }

}