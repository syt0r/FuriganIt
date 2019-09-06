package ua.syt0r.furiganit.model.repository.status

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class ServiceStatusRepository(context: Context) {

    private val localBroadcastManager: LocalBroadcastManager = LocalBroadcastManager.getInstance(context)
    private val serviceListener = ServiceListener()

    private val mutableServiceStatus = MutableLiveData<ServiceStatus>()

    fun setStatus(status: ServiceStatus) {
        val intent = Intent()
        when (status) {
            ServiceStatus.LAUNCHING -> intent.action = LAUNCHING_STATUS
            ServiceStatus.RUNNING -> intent.action = RUNNING_STATUS
            ServiceStatus.STOPPED -> intent.action = STOPPED_STATUS
        }
        localBroadcastManager.sendBroadcast(intent)
    }

    fun subscribeOnStatus(): LiveData<ServiceStatus> {

        val intentFilter = IntentFilter()
        intentFilter.addAction(LAUNCHING_STATUS)
        intentFilter.addAction(RUNNING_STATUS)
        intentFilter.addAction(STOPPED_STATUS)
        localBroadcastManager.registerReceiver(serviceListener, intentFilter)

        return mutableServiceStatus
    }

    fun unsubscribe() {
        localBroadcastManager.unregisterReceiver(serviceListener)
    }

    private inner class ServiceListener : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            if (intent.action != null)
                when (intent.action) {
                    LAUNCHING_STATUS -> mutableServiceStatus.setValue(ServiceStatus.LAUNCHING)
                    RUNNING_STATUS -> mutableServiceStatus.setValue(ServiceStatus.RUNNING)
                    STOPPED_STATUS -> mutableServiceStatus.setValue(ServiceStatus.STOPPED)
                }

        }

    }

    companion object {
        private const val LAUNCHING_STATUS = "ua.syt0r.furiganit.app.service.launch"
        private const val RUNNING_STATUS = "ua.syt0r.furiganit.app.service.run"
        private const val STOPPED_STATUS = "ua.syt0r.furiganit.app.service.stop"
    }

}
