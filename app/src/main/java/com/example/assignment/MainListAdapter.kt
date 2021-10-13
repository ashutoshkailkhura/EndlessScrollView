package com.example.assignment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.assignment.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainListAdapter(private val context: Context) :
    ListAdapter<User.Data, RecyclerView.ViewHolder>(Companion) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addAndSubmitList(it: MutableList<User.Data>?) {
        adapterScope.launch {
            val items = when (it) {
                null -> emptyList<User.Data>()
                else -> currentList + it
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }


    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private val VIEW_TYPE_THANK_YOU = 3

    companion object : DiffUtil.ItemCallback<User.Data>() {
        override fun areItemsTheSame(oldItem: User.Data, newItem: User.Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User.Data, newItem: User.Data): Boolean {
            return oldItem == newItem
        }
    }

    inner class ItemViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val img: ImageView = item.findViewById(R.id.user_imageView)
        val name: TextView = item.findViewById(R.id.user_name_textView)
        val itemNumber: TextView = item.findViewById(R.id.item_number)
        val email: TextView = item.findViewById(R.id.user_email_textView)
    }

    inner class LoadingViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val loading: ProgressBar = item.findViewById(R.id.progressBar2)
    }

    inner class ThankYouViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val thanks: TextView = item.findViewById(R.id.thanks_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_ITEM ->
                ItemViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
                )
            VIEW_TYPE_LOADING -> LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            )
            VIEW_TYPE_THANK_YOU -> ThankYouViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_thanks, parent, false)
            )

            else -> throw IllegalArgumentException("Error")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MainListAdapter.ItemViewHolder -> {
                val user = getItem(position)
                holder.name.text = "${user.firstName} ${user.lastName}"
                holder.email.text = "${user.email}"
                holder.itemNumber.text = "${position+1}"
                Glide.with(context)
                    .load(user.avatar)
                    .apply(
                        RequestOptions().placeholder(R.drawable.booked_circle)
                            .error(R.drawable.booked_circle)
                    )
                    .into(holder.img)
            }
            is MainListAdapter.LoadingViewHolder -> {
                holder.loading.visibility = View.VISIBLE
            }
            is MainListAdapter.ThankYouViewHolder -> {
                holder.thanks.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    fun showThankYou() {
        TODO("Not yet implemented")
    }


}