package com.karyadika.githubuser.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.karyadika.githubuser.R
import com.karyadika.githubuser.utils.Result
import com.karyadika.githubuser.data.model.Users
import com.karyadika.githubuser.databinding.ActivityDetailBinding
import com.karyadika.githubuser.ui.adapter.SectionPagerAdapter
import com.karyadika.githubuser.utils.ViewStateCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity(),ViewStateCallback<Users?>{

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
        }

        showDetail()
        setViewPager()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showDetail() {
        val username = intent.getStringExtra(EXTRA_USERNAME)

        supportActionBar?.title = username

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getDetailUsers(username.toString()).observe(this@DetailActivity){
                when(it){
                    is Result.Loading -> onLoading()
                    is Result.Error -> onFailed(it.message)
                    is Result.Success -> onSuccess(it.data)
                }
            }
        }
    }

    private fun setViewPager() {
        val sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.detailViewPager
        viewPager.adapter = sectionPagerAdapter

        val tabs: TabLayout = binding.detailTab
        TabLayoutMediator(tabs, viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITTLES[position])
        }.attach()
    }

    private fun isFavoriteUser(fav: Boolean) {
        if (fav) {
            binding.fabFav.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.fabFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    override fun onSuccess(data: Users?) {
        binding.apply {
            progressBar.visibility = View.GONE
            txtDetailName.text = data?.name
            txtDetailCompany.text = data?.company
            txtDetailLocation.text = data?.location
            txtDetailDate.text = data?.createdAt
            Glide.with(this@DetailActivity)
                .load(data?.avatarUrl)
                .into(binding.detailImg)

            fabFav.visibility = View.VISIBLE

            if (data?.isFavorite == true){
                isFavoriteUser(true)
            } else {
                isFavoriteUser(false)
            }

            fabFav.setOnClickListener {
                if (data?.isFavorite == true) {
                    data.isFavorite = false
                    viewModel.deleteFromFavorite(data)
                    isFavoriteUser(false)
                    Toast.makeText(this@DetailActivity,"User deleted from favorite", Toast.LENGTH_SHORT).show()
                } else {
                    data?.isFavorite = true
                    data?.let { it1 -> viewModel.saveAsFavorite(it1) }
                    isFavoriteUser(true)
                    Toast.makeText(this@DetailActivity, "User added to favorite", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onLoading() {
        binding.apply{
            progressBar.visibility = View.VISIBLE
            fabFav.visibility = View.INVISIBLE
        }
    }

    override fun onFailed(message: String?) {
        binding.fabFav.visibility = View.INVISIBLE
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        @StringRes
        private val TAB_TITTLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }

}
