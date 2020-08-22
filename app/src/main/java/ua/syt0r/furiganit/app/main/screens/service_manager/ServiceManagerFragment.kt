package ua.syt0r.furiganit.app.main.screens.service_manager

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
import ua.syt0r.furiganit.R
import ua.syt0r.furiganit.core.service.FuriganaService

class ServiceManagerFragment : Fragment() {

    private val serviceManagerViewModel: ServiceManagerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(serviceManagerViewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_service_manager, container, false)

        val button = root.findViewById<Button>(R.id.button)
        val hintTextView = root.findViewById<TextView>(R.id.text)

        serviceManagerViewModel.subscribeOnServiceState()
                .observe(
                        this.viewLifecycleOwner,
                        Observer { state ->
                            button.setText(state.buttonTextResId)
                            hintTextView.setText(state.hintTextResId)
                        }
                )

        button.setOnClickListener {
            when (serviceManagerViewModel.subscribeOnServiceState().value) {
                ServiceManagerState.CANT_DRAW_OVERLAY -> requestDrawOverlayPermission()
                ServiceManagerState.STOPPED -> FuriganaService.startService(requireContext())
                ServiceManagerState.LAUNCHING -> Snackbar.make(root, R.string.wait, Snackbar.LENGTH_SHORT).show()
                ServiceManagerState.RUNNING -> FuriganaService.stopService(requireContext())
            }
        }

        return root
    }

    private fun requestDrawOverlayPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + requireContext().packageName))
            startActivityForResult(intent, 0)
        }
    }

}
