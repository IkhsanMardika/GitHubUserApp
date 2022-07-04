package com.karyadika.githubuser.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.karyadika.githubuser.R
import com.karyadika.githubuser.utils.Result
import com.karyadika.githubuser.data.model.Users
import com.karyadika.githubuser.databinding.ActivityFavoriteBinding
import com.karyadika.githubuser.ui.adapter.UserAdapter
import com.karyadika.githubuser.ui.detail.DetailActivity
import com.karyadika.githubuser.utils.ViewStateCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity(),ViewStateCallback<List<Users>> {

    private var _binding : ActivityFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var userAdapter: UserAdapter
    private val viewModel by viewModels<FavoriteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
        }

        supportActionBar?.title = getString(R.string.favorite)

        setupAdapter()
        showData()
    }

    override fun onResume() {
        super.onResume()
        showData()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


    private fun showData() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getAllFavoriteUsers().observe(this@FavoriteActivity){
                when(it) {
                    is Result.Loading -> onLoading()
                    is Result.Error -> onFailed(it.message)
                    is Result.Success -> it.data?.let { it1 ->
                        onSuccess(it1)
                    }
                }
            }
        }

    }

    private fun setupAdapter() {
        userAdapter = UserAdapter()
        binding.rvFavorite.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@FavoriteActivity, LinearLayoutManager.VERTICAL, false)
        }
        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(users: Users) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USERNAME, users.username)
                startActivity(intent)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onSuccess(data: List<Users>) {
        userAdapter.setData(data)
        binding.apply {
            progressBar.visibility = View.INVISIBLE
            tvFavoriteError.visibility = View.INVISIBLE
        }
    }

    override fun onLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onFailed(message: String?) {
        if (message == null) {
            binding.apply {
                progressBar.visibility = View.INVISIBLE
                tvFavoriteError.visibility = View.VISIBLE
                tvFavoriteError.text = getString(R.string.no_fav)
                rvFavorite.visibility = View.INVISIBLE
            }
        }
    }


}