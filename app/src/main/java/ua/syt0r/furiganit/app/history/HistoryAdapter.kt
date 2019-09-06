package ua.syt0r.furiganit.app.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList
import ua.syt0r.furiganit.R
import ua.syt0r.furiganit.model.entity.HistoryItem

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var data: List<HistoryItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_history_item, parent, false))
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyItem = data[position]
        holder.textView.text = ""
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(history: List<HistoryItem>) {
        data = history
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.text)
        var dateTextView: TextView = itemView.findViewById(R.id.date)
    }

}
