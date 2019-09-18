package ua.syt0r.furiganit.app.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.app.DialogCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import ua.syt0r.furiganit.R
import ua.syt0r.furiganit.model.repository.hisotry.remote.SyncAction
import java.lang.IllegalStateException

class HistoryFragment : Fragment() {

    private val historyViewModel: HistoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_history, container, false)

        val progressBar = root.findViewById<ProgressBar>(R.id.progress)

        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)

        val adapter = HistoryAdapter()
        recyclerView.adapter = adapter

        historyViewModel.subscribeOnHistory().observe(this, Observer { history ->

            when {

                history == null -> {
                    recyclerView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }

                history.isEmpty() -> {
                    recyclerView.visibility = View.GONE
                    progressBar.visibility = View.GONE

                }

                else -> {
                    recyclerView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    adapter.updateData(history)
                }

            }

        })

        historyViewModel.subscribeOnMessage().observe(this, Observer { errorText ->
            Snackbar.make(root, errorText, Snackbar.LENGTH_SHORT).show()
        })

        historyViewModel.subscribeOnEvents().observe(this, Observer { event ->

            when(event) {
                HistoryViewModel.Event.GO_TO_SIGNUP_SCREEN -> showSnackBarWithSignInAction()
                HistoryViewModel.Event.SELECT_SYNC_ACTION -> selectSyncActionDialog()
                null -> throw IllegalStateException("Unknown event")
            }

        })

        historyViewModel.fetchHistory()

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.history_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.sync) {
            historyViewModel.checkRemoteRepoState()
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    private fun showSnackBarWithSignInAction() {

        Snackbar.make(requireView(), getString(R.string.app_name), Snackbar.LENGTH_LONG)
                .setAction("Sign In") {
                    val navController = NavHostFragment.findNavController(this)
                    navController.navigate(R.id.settings_fragment)
                }
                .show()

    }

    private fun selectSyncActionDialog() {

        val context = requireContext()

        val bottomSheetDialog = BottomSheetDialog(context)

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_select_sync_action, null)
        bottomSheetDialog.setContentView(view)

        view.findViewById<Button>(R.id.merge).setOnClickListener {
            historyViewModel.sync(SyncAction.MERGE)
        }

        view.findViewById<Button>(R.id.upload).setOnClickListener {
            historyViewModel.sync(SyncAction.OVERWRITE_REMOTE)
        }

        view.findViewById<Button>(R.id.download).setOnClickListener {
            historyViewModel.sync(SyncAction.OVERWRITE_LOCAL)
        }

        bottomSheetDialog.show()

    }

}
