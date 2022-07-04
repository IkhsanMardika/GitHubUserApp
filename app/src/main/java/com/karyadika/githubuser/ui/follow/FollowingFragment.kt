package com.karyadika.githubuser.ui.follow

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.karyadika.githubuser.R
import com.karyadika.githubuser.utils.Result
import com.karyadika.githubuser.data.model.Users
import com.karyadika.githubuser.databinding.FragmentFollowBinding
import com.karyadika.githubuser.ui.adapter.UserAdapter
import com.karyadika.githubuser.ui.detail.DetailActivity
import com.karyadika.githubuser.utils.ViewStateCallback

class FollowingFragment : Fragment(R.layout.fragment_follow), ViewStateCallback<List<Users>> {

    private var _binding : FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FollowingViewModel by viewModels()
    private lateinit var adapter: UserAdapter
    private lateinit var username: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowBinding.bind(view)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvUser.setHasFixedSize(true)
            rvUser.layoutManager = LinearLayoutManager(activity)
            rvUser.adapter = adapter
        }

        username = activity?.intent?.getStringExtra(DetailActivity.EXTRA_USERNAME).toString()

        viewModel.getUserFollowing(username).observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> onLoading()
                is Result.Error -> onFailed(it.message)
                is Result.Success -> it.data?.let { result ->
                    onSuccess(result)
                }
            }
        }

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(users: Users) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USERNAME, users.username)
                startActivity(intent)
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSuccess(data: List<Users>) {
        adapter.setData(data)
        binding.apply {
            rvUser.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
            tvNoFollow.visibility = View.INVISIBLE
        }
    }

    override fun onLoading() {
        binding.apply {
            rvUser.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            tvNoFollow.visibility = View.INVISIBLE
        }
    }

    override fun onFailed(message: String?) {
        binding.apply {
            if (message == null) {
                tvNoFollow.text = getString(R.string.no_following)
                tvNoFollow.visibility = View.VISIBLE
            } else {
                tvNoFollow.text = message
                tvNoFollow.visibility = View.VISIBLE
            }
            progressBar.visibility = View.INVISIBLE
            rvUser.visibility = View.INVISIBLE
        }
    }


}