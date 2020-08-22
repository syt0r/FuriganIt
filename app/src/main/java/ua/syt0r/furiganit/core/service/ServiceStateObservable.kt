package ua.syt0r.furiganit.core.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class ServiceStateObservable(context: Context) {

	companion object {
		private const val LAUNCHING_STATE = "ua.syt0r.furiganit.core.service.launch"
		private const val RUNNING_STATE = "ua.syt0r.furiganit.core.service.run"
		private const val STOPPED_STATE = "ua.syt0r.furiganit.core.service.stop"
	}

	private val localBroadcastManager: LocalBroadcastManager = LocalBroadcastManager.getInstance(context)
	private val serviceListener = ServiceListener()

	private val mutableServiceStatus = MutableLiveData<ServiceState>()

	fun setState(state: ServiceState) {
		val intent = Intent()
		when (state) {
			ServiceState.LAUNCHING -> intent.action = LAUNCHING_STATE
			ServiceState.RUNNING -> intent.action = RUNNING_STATE
			ServiceState.STOPPED -> intent.action = STOPPED_STATE
		}
		localBroadcastManager.sendBroadcast(intent)
	}

	fun subscribeToStateChanges(): LiveData<ServiceState> {

		val intentFilter = IntentFilter()
		intentFilter.addAction(LAUNCHING_STATE)
		intentFilter.addAction(RUNNING_STATE)
		intentFilter.addAction(STOPPED_STATE)
		localBroadcastManager.registerReceiver(serviceListener, intentFilter)

		return mutableServiceStatus
	}

	fun unsubscribe() {
		localBroadcastManager.unregisterReceiver(serviceListener)
	}

	private inner class ServiceListener : BroadcastReceiver() {

		override fun onReceive(context: Context, intent: Intent) {
			if (intent.action != null) {
				when (intent.action) {
					LAUNCHING_STATE -> mutableServiceStatus.setValue(ServiceState.LAUNCHING)
					RUNNING_STATE -> mutableServiceStatus.setValue(ServiceState.RUNNING)
					STOPPED_STATE -> mutableServiceStatus.setValue(ServiceState.STOPPED)
				}
			}
		}

	}


}
