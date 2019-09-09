package ua.syt0r.furiganit.app.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ua.syt0r.furiganit.R

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

        historyViewModel.fetchHistory()

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.history_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.sync) {
            historyViewModel.sync()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
