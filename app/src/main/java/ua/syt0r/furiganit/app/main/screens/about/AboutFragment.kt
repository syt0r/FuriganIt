package ua.syt0r.furiganit.app.main.screens.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_about.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ua.syt0r.furiganit.R

class AboutFragment : Fragment() {

    private val aboutViewModel: AboutViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_about, container, false)

        source_code_button.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/SYtor/FuriganIt"))
            startActivity(browserIntent)
        }

        return root
    }

}
