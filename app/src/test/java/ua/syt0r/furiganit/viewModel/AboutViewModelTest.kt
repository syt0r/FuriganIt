package ua.syt0r.furiganit.viewModel

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ua.syt0r.furiganit.app.about.AboutViewModel
import ua.syt0r.furiganit.app.about.BillingManager
import ua.syt0r.furiganit.model.usecase.TextLocalizerUseCase

class AboutViewModelTest {

    @Mock lateinit var billingManager: BillingManager
    @Mock lateinit var textLocalizer: TextLocalizerUseCase

    lateinit var aboutViewModel: AboutViewModel

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        val aboutViewModel = AboutViewModel(billingManager, textLocalizer)

    }

    @Test
    fun testPurchase() {

        //Given


        //When

        //aboutViewModel.purchase(Mockito.any(Activity::class.java))

        //Then

        //Mockito.verify(textLocalizer.getMessage(Mockito.anyInt()), Mockito.times(1))

    }

}