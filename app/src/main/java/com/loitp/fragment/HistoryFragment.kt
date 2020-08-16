package com.loitp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.base.BaseFragment
import com.core.common.Constants
import com.core.utilities.LActivityUtil
import com.loitp.R
import com.loitp.activity.MapActivity
import com.loitp.adapter.BookAdapter
import com.loitp.model.Movie
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.frm_history.*
import java.util.*

class HistoryFragment : BaseFragment() {

    private val movieList = ArrayList<Movie>()
    private var mAdapter: BookAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = BookAdapter(
                moviesList = movieList,
                callback = object : BookAdapter.Callback {
                    override fun onClick(movie: Movie, position: Int) {
                    }

                    override fun onLoadMore() {

                    }
                })
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = mAdapter
        prepareMovieData()

        btRecord.setSafeOnClickListener {
            activity?.let { a ->
                val intent = Intent(a, MapActivity::class.java)
                startActivity(intent)
                LActivityUtil.tranIn(context = a)
            }
        }
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_history
    }

    override fun setTag(): String? {
        return javaClass.simpleName
    }

    private fun prepareMovieData() {
        var cover: String
        for (i in 0..99) {
            cover = if (i % 2 == 0) {
                Constants.URL_IMG_1
            } else {
                Constants.URL_IMG_2
            }
            val movie = Movie("Loitp $i", "Action & Adventure $i", "Year: $i", cover)
            movieList.add(movie)
        }
        mAdapter?.notifyDataSetChanged()
    }
}
