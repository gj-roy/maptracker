package com.loitp.db

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loitp.R
import com.loitp.model.History
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.view_item_room_history.view.*

class RoomHistoryAdapter : RecyclerView.Adapter<RoomHistoryAdapter.ViewHolder>() {

    private val listHistory = ArrayList<History>()

    var onClickRootView: ((History) -> Unit)? = null
    var onClickUpDate: ((History) -> Unit)? = null
    var onClickDelete: ((History) -> Unit)? = null

    fun setListHistory(listHistory: List<History>) {
        this.listHistory.clear()
        this.listHistory.addAll(listHistory)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun bind(history: History) {
            itemView.tvHistory.text = "${history.id} - ${history.fileName}"
            itemView.rootView.setSafeOnClickListener {
                onClickRootView?.invoke(history)
            }
            itemView.ivUpdate.setSafeOnClickListener {
                onClickUpDate?.invoke(history)
            }
            itemView.ivDelete.setSafeOnClickListener {
                onClickDelete?.invoke(history)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_item_room_history, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listHistory[position])
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }
}
