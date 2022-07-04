package com.karyadika.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.karyadika.githubuser.data.model.Users
import com.karyadika.githubuser.databinding.UserItemBinding

class UserAdapter() : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private val userData = ArrayList<Users>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setData(items: List<Users>) {
        userData.clear()
        userData.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userData[position])
    }

    override fun getItemCount(): Int = userData.size

    inner class ViewHolder(var binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(usersItem: Users) {
            binding.apply {
                Glide.with(itemView)
                    .load(usersItem.avatarUrl)
                    .into(binding.imgAvatar)
                txtUsername.text = usersItem.username
                txtHtmlUrl.text = usersItem.htmlUrl
            }
            binding.root.setOnClickListener {
                onItemClickCallback.onItemClicked(usersItem)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(users: Users)
    }


}