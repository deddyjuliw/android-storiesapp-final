package com.djw.storiesapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
// import com.djw.storiesapp.DateFormatter
import com.djw.storiesapp.R
import com.djw.storiesapp.data.response.ListStoryItem
import com.djw.storiesapp.databinding.ItemStoryBinding
import com.djw.storiesapp.ui.detail.DetailActivity
import com.djw.storiesapp.withDateFormat
// import com.djw.storiesapp.withDateFormat
import java.text.DateFormat
import java.util.TimeZone

class StoryListAdapter: PagingDataAdapter<ListStoryItem, StoryListAdapter.ListViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null){
            holder.bind(data)
        }
    }

    //override fun getItemCount(): Int = listStory.size

    class ListViewHolder(private var binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(story: ListStoryItem) {
            // val format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            //val formatted = "dd MMM yyyy | HH:mm"
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_load).error(R.drawable.ic_error)
                )
                .into(binding.ivItemPhoto)
            binding.apply {
                tvItemName.text = story.name
                tvRvCreate.text = withDateFormat(story.createdAt.toString())
                tvRvDesc.text = story.description
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STORY, story)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivItemPhoto, "photo"),
                        Pair(binding.tvItemName, "name"),
                        Pair(binding.tvRvCreate, "create"),
                        Pair(binding.tvRvDesc, "desc")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {

            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}