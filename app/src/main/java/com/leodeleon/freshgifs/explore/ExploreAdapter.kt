package com.leodeleon.freshgifs.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.leodeleon.domain.entities.Giphy
import com.leodeleon.freshgifs.databinding.ItemGifVerticalBinding

class ExploreAdapter: PagedListAdapter<Giphy, ExploreAdapter.GifViewHolder>(ExploreAdapter.DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val binding = ItemGifVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GifViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        holder.binding.gif = getItem(position)
    }

    fun getGif(position: Int): Giphy?{
        return getItem(position)
    }

    class GifViewHolder(val binding: ItemGifVerticalBinding): RecyclerView.ViewHolder(binding.root)

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<Giphy>(){
            override fun areItemsTheSame(oldItem: Giphy, newItem: Giphy): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Giphy, newItem: Giphy): Boolean {
                return oldItem == newItem
            }
        }
    }

}