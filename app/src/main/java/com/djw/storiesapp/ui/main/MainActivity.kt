package com.djw.storiesapp.ui.main

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.djw.storiesapp.R
import com.djw.storiesapp.adapter.LoadingStateAdapter
import com.djw.storiesapp.adapter.StoryListAdapter
import com.djw.storiesapp.data.Result
import com.djw.storiesapp.data.response.ListStoryItem
import com.djw.storiesapp.databinding.ActivityMainBinding
import com.djw.storiesapp.ui.StoryViewModelFactory
import com.djw.storiesapp.ui.maps.MapsActivity
import com.djw.storiesapp.ui.signin.SigninActivity
import com.djw.storiesapp.ui.story.AddStoryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
    }

    private fun setupViewModel(){
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            binding.rvStory.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvStory.layoutManager = LinearLayoutManager(this)
        }

        val factory: StoryViewModelFactory = StoryViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory).get(
            MainViewModel::class.java
        )

        mainViewModel.isLogin().observe(this){
            if (!it){
                startActivity(Intent(this, SigninActivity::class.java))
                finish()
            }
        }

        mainViewModel.getToken().observe(this) { token ->
            this.token = token
            if (token.isNotEmpty()) {
                val adapter = StoryListAdapter()
                binding.rvStory.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        adapter.retry()
                    }
                )
                mainViewModel.getStories(token).observe(this){ result ->
                    adapter.submitData(lifecycle, result)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.add_story -> {
                val intent = Intent(this, AddStoryActivity::class.java)
                intent.putExtra(AddStoryActivity.EXTRA_TOKEN, token)
                startActivity(intent)
                true
            }
            R.id.maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra(MapsActivity.EXTRA_TOKEN, token)
                startActivity(intent)
                true
            }
            R.id.logout -> {
                mainViewModel.logout()
                true
            }
            else -> true
        }
    }
}