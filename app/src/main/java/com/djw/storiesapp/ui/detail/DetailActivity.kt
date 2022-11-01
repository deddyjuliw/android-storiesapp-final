package com.djw.storiesapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.djw.storiesapp.*
import com.djw.storiesapp.data.response.ListStoryItem
import com.djw.storiesapp.databinding.ActivityDetailBinding
import java.util.Locale
import java.util.TimeZone

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = resources.getString(R.string.app_nameDetail)

        //val format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        // val formatted = "dd MMM yyyy | HH:mm"

        val story = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY)
        binding.apply {
            tvDetailName.text = story?.name
            tvCreated.text = withDateFormat(story?.createdAt.toString())
            tvDetailDescription.text = story?.description
        }
        Glide.with(this)
            .load(story?.photoUrl)
            .apply(
                RequestOptions.placeholderOf(R.drawable.ic_load).error(R.drawable.ic_error)
            )
            .into(binding.ivDetailPhoto)
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}