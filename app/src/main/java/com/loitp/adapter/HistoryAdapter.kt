package com.loitp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.core.common.Constants
import com.core.utilities.LImageUtil
import com.google.ads.interactivemedia.v3.internal.it
import com.loitp.R
import com.loitp.model.History
import com.loitp.util.ImageUtil
import kotlinx.android.synthetic.main.view_item_history.view.*

class HistoryAdapter(
        private val moviesList: ArrayList<History>,
        private val callback: Callback?)
    : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    interface Callback {
        fun onClick(history: History, position: Int)
        fun onLoadMore()
    }

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(history: History) {
            itemView.tvDistance.text = history.distance
            itemView.tvAvgSpeed.text = history.avgSpeed
            itemView.tvTimer.text = history.timer

            val file = ImageUtil.getFile(context = itemView.ivMap.context, fileName = history.fileName)
            LImageUtil.load(context = itemView.ivMap.context, imageFile = file, imageView = itemView.ivMap)

            itemView.rootView.setOnClickListener {
                callback?.onClick(history, bindingAdapterPosition)
            }
            if (bindingAdapterPosition == moviesList.size - 1) {
                callback?.onLoadMore()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_item_history, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(moviesList[position])
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }
}
