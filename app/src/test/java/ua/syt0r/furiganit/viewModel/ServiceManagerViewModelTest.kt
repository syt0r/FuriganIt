package ua.syt0r.furiganit.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import ua.syt0r.furiganit.app.serviceManager.ServiceManagerState
import ua.syt0r.furiganit.app.serviceManager.ServiceManagerViewModel
import ua.syt0r.furiganit.mock
import ua.syt0r.furiganit.model.repository.serviceState.ServiceState
import ua.syt0r.furiganit.model.repository.serviceState.ServiceStateRepository
import ua.syt0r.furiganit.model.usecase.OverlayDrawabilityChecker
import ua.syt0r.furiganit.model.usecase.implementation.ServiceManagerStateMapperImpl

class ServiceManagerViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock lateinit var serviceStateRepository: ServiceStateRepository
    @Mock lateinit var overlayDrawChecker: OverlayDrawabilityChecker
    val serviceStateMapper = ServiceManagerStateMapperImpl()

    lateinit var mutableServiceState: MutableLiveData<ServiceState>

    lateinit var serviceManagerViewModel: ServiceManagerViewModel

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        mutableServiceState = MutableLiveData()
        Mockito.`when`(serviceStateRepository.subscribeOnStatus()).then {
            mutableServiceState
        }

        serviceManagerViewModel = ServiceManagerViewModel(
                serviceStateRepository,
                overlayDrawChecker,
                serviceStateMapper)

    }

    @Test
    fun testCantDrawOverlayState() {

        //Given

        mutableServiceState.postValue(ServiceState.RUNNING)

        Mockito.`when`(overlayDrawChecker.canDrawOverlay()).then { false }

        val observer: Observer<ServiceManagerState> = mock()

        //When

        serviceManagerViewModel.subscribeOnServiceState().observeForever(observer)

        //Then

        Mockito.verify(observer, Mockito.times(1))
                .onChanged(ServiceManagerState.CANT_DRAW_OVERLAY)

    }

    @Test
    fun testOtherStates() {

        //Given

        Mockito.`when`(overlayDrawChecker.canDrawOverlay()).then { true }
        val observer: Observer<ServiceManagerState> = mock()

        //When

        serviceManagerViewModel.subscribeOnServiceState().observeForever(observer)

        mutableServiceState.postValue(ServiceState.STOPPED)
        mutableServiceState.postValue(ServiceState.LAUNCHING)
        mutableServiceState.postValue(ServiceState.RUNNING)

        //Then

        Mockito.verify(observer, Mockito.times(1))
                .onChanged(ServiceManagerState.STOPPED)

        Mockito.verify(observer, Mockito.times(1))
                .onChanged(ServiceManagerState.LAUNCHING)

        Mockito.verify(observer, Mockito.times(1))
                .onChanged(ServiceManagerState.RUNNING)

    }

}