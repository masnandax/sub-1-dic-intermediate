package com.example.storyapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.R
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.auth.UserPreferences
import com.example.storyapp.databinding.ActivityHomeBinding
import com.example.storyapp.ui.addStory.AddStoryActivity
import com.example.storyapp.ui.detail.DetailActivity
import com.example.storyapp.ui.login.LoginActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var dataPreferences: UserPreferences
    private lateinit var binding : ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private val listStory = ArrayList<ListStoryItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)

        dataPreferences = UserPreferences(this)

        binding.apply {
            val myRecyclerView: RecyclerView = binding.rvStory
            binding.rvStory.layoutManager = layoutManager
            binding.rvStory.setHasFixedSize(true)
            val adapter = HomeAdapter(listStory)
            myRecyclerView.adapter = adapter
        }

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.listStory(this)
        homeViewModel.listStory.observe(this) { storyItem ->
            if (storyItem != null) {
                setListStory(storyItem)
            }
        }

        homeViewModel.isLoading.observe(this){
            showLoading(it)
        }

        Log.d("Result", "msg : $listStory")

        binding.actionAdd.setOnClickListener{
            startActivity(Intent(this@HomeActivity, AddStoryActivity::class.java))
        }
    }

    private fun setListStory(storyItem: List<ListStoryItem>) {
        for(story in storyItem) {
            val idStory = story.id
            val imagesStory = story.photoUrl
            val dateStory = story.createdAt
            val nameStory = story.name

            val inputStory = ListStoryItem(idStory,imagesStory, dateStory, nameStory)
            listStory.add(inputStory)
        }

        val adapter = HomeAdapter(listStory)
        binding.rvStory.adapter = adapter

        adapter.setOnItemClickCallback(object: HomeAdapter.OnItemClickCallback {
            override fun onItemClicked(story: ListStoryItem) {
                showSelectedStory(story)
            }
        })
    }

    private fun showSelectedStory(story: ListStoryItem) {
        Toast.makeText(this, "Kamu memilih " + story.name, Toast.LENGTH_SHORT).show()

        val moveDetailUser = Intent(this@HomeActivity, DetailActivity::class.java)
        moveDetailUser.putExtra(DetailActivity.EXTRA_ID, story.id)

        val img: ImageView = findViewById(R.id.images_story)
        val user: TextView = findViewById(R.id.user_story)

        val optionsCompat: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                androidx.core.util.Pair(img, "profile"),
                androidx.core.util.Pair(user, "name")
            )

        startActivity(moveDetailUser, optionsCompat.toBundle())
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_logout -> {
                dataPreferences.clear()
                Toast.makeText(this@HomeActivity, "Berhasil Logout", Toast.LENGTH_SHORT).show()
                moveLogin()
            }
            R.id.language_settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun moveLogin() {
        val loginIntent = Intent(this@HomeActivity, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }

    override fun onRestart() {
        super.onRestart()
        homeViewModel.listStory(this)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        homeViewModel.listStory(this)
        finish()
    }
}