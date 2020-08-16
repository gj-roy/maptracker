package com.loitp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.core.utilities.LImageUtil
import com.core.utilities.LScreenUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.model.Movie
import kotlinx.android.synthetic.main.view_row_item_book.view.*

class BookAdapter(
        private val moviesList: ArrayList<Movie>,
        private val callback: Callback?)
    : RecyclerView.Adapter<BookAdapter.MovieViewHolder>() {

    interface Callback {
        fun onClick(movie: Movie, position: Int)
        fun onLoadMore()
    }

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie) {
            itemView.textView.text = movie.title
            itemView.textView.visibility = View.VISIBLE
            LImageUtil.load(context = itemView.ivMap.context, url = movie.cover, imageView = itemView.ivMap)
            itemView.rootView.setOnClickListener {
                callback?.onClick(movie, bindingAdapterPosition)
            }
            if (bindingAdapterPosition == moviesList.size - 1) {
                callback?.onLoadMore()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_row_item_book, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(moviesList[position])
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }
}
