package com.karyadika.githubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.karyadika.githubuser.R
import com.karyadika.githubuser.utils.Result
import com.karyadika.githubuser.data.model.Users
import com.karyadika.githubuser.databinding.ActivityMainBinding
import com.karyadika.githubuser.ui.adapter.UserAdapter
import com.karyadika.githubuser.ui.detail.DetailActivity
import com.karyadika.githubuser.ui.favorite.FavoriteActivity
import com.karyadika.githubuser.ui.setting.SettingActivity
import com.karyadika.githubuser.utils.ViewStateCallback

class MainActivity : AppCompatActivity(),ViewStateCallback<List<Users>> {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var userAdapter: UserAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        searchUsers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.favorite -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.setting -> {
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun searchUsers() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.searchUsername

        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(input: String): Boolean {
                    clearFocus()
                    viewModel.searchUsers(input).observe(this@MainActivity){
                        when(it){
                            is Result.Error -> onFailed(it.message)
                            is Result.Loading -> onLoading()
                            is Result.Success -> it.data?.let { user ->
                                onSuccess(user)
                            }
                        }
                    }
                    return true
                }
                override fun onQueryTextChange(inputNew: String): Boolean {
                    return false
                }
            })
        }

    }

    private fun setupAdapter() {
        userAdapter = UserAdapter()
        binding.recyclerView.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }
        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(users: Users) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USERNAME, users.username)
                startActivity(intent)
            }
        })
    }

    override fun onSuccess(data: List<Users>) {
        userAdapter.setData(data)
        binding.apply {
            progressBar.visibility = View.INVISIBLE
            recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onLoading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.INVISIBLE
        }
    }

    override fun onFailed(message: String?) {
        binding.apply {
            if (message == null) {
                Toast.makeText(this@MainActivity, "User Not Found!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
            progressBar.visibility = View.INVISIBLE
            recyclerView.visibility = View.INVISIBLE
        }
    }


}
