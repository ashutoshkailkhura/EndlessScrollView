package com.example.assignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    var initialPage: Int = 1
    var pastVisiblesItems = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    private lateinit var adapter: MainListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.user_list)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swiperefresh)

        adapter = MainListAdapter(this)
        recyclerView.adapter = adapter

        viewModel.loadingStatus.observe(this, {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.INVISIBLE
            }
        })

        viewModel.user.observe(this, {
            adapter.addAndSubmitList(it)
            swipeRefreshLayout.isRefreshing = false
        })

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                visibleItemCount = layoutManager.childCount
                totalItemCount = layoutManager.itemCount
                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    if (viewModel.apiTotalItemCount.value == totalItemCount) {
                        adapter.showThankYou()
                    } else {
                        initialPage++
                        getUserData(initialPage, 5)
                    }
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            initialPage = 0
            viewModel.setupData()
            getUserData(initialPage, 5)
        }

        viewModel.getUserData(initialPage, 5)
    }

    private fun getUserData(page: Int, perPage: Int) {
        adapter.notifyItemInserted(totalItemCount - 1)
        viewModel.getUserData(page, perPage)
        adapter.notifyItemRemoved(totalItemCount)
    }
}