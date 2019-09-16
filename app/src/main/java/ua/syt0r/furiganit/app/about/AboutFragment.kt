package ua.syt0r.furiganit.app.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import ua.syt0r.furiganit.R

class AboutFragment : Fragment() {

    private val aboutViewModel: AboutViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_about, container, false)

        val purchaseLayout: ViewGroup = root.findViewById(R.id.purchase_layout)
        purchaseLayout.visibility = View.GONE

        root.findViewById<View>(R.id.source_code_button).setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/SYtor/FuriganIt"))
            startActivity(browserIntent)
        }

        root.findViewById<View>(R.id.support_button).setOnClickListener {
            aboutViewModel.purchase(requireActivity())
        }

        aboutViewModel.subscribeOnBillingAvailability().observe(this, Observer { isAvailable ->

            if(isAvailable == true)
                purchaseLayout.visibility = View.VISIBLE
            else
                purchaseLayout.visibility = View.GONE

        })

        aboutViewModel.subscribeOnMessage().observe(this, Observer {
            Snackbar.make(root, it, Snackbar.LENGTH_SHORT).show()
        })

        aboutViewModel.checkBillingAvailability()

        return root
    }


}
