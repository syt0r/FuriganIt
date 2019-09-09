package ua.syt0r.furiganit.app.serviceManager

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import ua.syt0r.furiganit.app.service.FuriganaService
import ua.syt0r.furiganit.R

class ServiceManagerFragment : Fragment() {

    private val serviceManagerViewModel: ServiceManagerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(serviceManagerViewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_service_manager, container, false)

        val button = root.findViewById<Button>(R.id.button)
        val textView = root.findViewById<TextView>(R.id.text)

        serviceManagerViewModel.subscribeOnServiceState().observe(this, Observer { status ->

            when (status) {

                ServiceManagerState.CANT_DRAW_OVERLAY -> {

                    button.setText(R.string.go_to_settings)
                    textView.setText(R.string.got_to_sett_hint)

                    button.setOnClickListener { requestDrawOverlayPermission() }
                }

                ServiceManagerState.STOPPED -> {

                    button.setText(R.string.start)
                    textView.setText(R.string.start_hint)

                    button.setOnClickListener {
                        val intent = Intent(context, FuriganaService::class.java)
                        root.context.startService(intent)
                    }
                }

                ServiceManagerState.LAUNCHING -> {

                    button.setText(R.string.starting)
                    textView.setText(R.string.starting_hint)

                    button.setOnClickListener {
                        Snackbar.make(root,R.string.wait, Snackbar.LENGTH_SHORT).show()
                    }
                }

                ServiceManagerState.RUNNING -> {

                    button.setText(R.string.stop)
                    textView.setText(R.string.stop_hint)

                    button.setOnClickListener {
                        val intent = Intent(context, FuriganaService::class.java)
                        root.context.stopService(intent)
                    }
                }

            }

        })

        return root
    }

    private fun requestDrawOverlayPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context!!.packageName))
            startActivityForResult(intent, 0)
        }
    }

}
