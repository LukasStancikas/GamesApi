package com.lukasstancikas.gamesapp.feature.gamelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.lukasstancikas.gamesapp.R
import com.lukasstancikas.gamesapp.model.Cover
import com.lukasstancikas.gamesapp.model.Game
import kotlinx.android.synthetic.main.item_game.view.*

class GameAdapter : RecyclerView.Adapter<GameAdapter.MyViewHolder>() {
    private val items = mutableListOf<Game>()
    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.itemGameTitle.text = items[position].name
        holder.itemView.itemGameRating.text = items[position].rating.toString()
        holder.itemView.itemGameSummary.text = items[position].summary
        holder.itemView.setOnClickListener {
            itemClickListener?.onClick(items[position])
        }
        Glide
            .with(holder.itemView.context)
            .load(items[position].cover?.trimmedCoverUrl())
            .placeholder(R.drawable.ic_photo)
            .error(R.drawable.ic_error)
            .centerInside()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.itemView.itemGameCover)
    }

    override fun getItemCount() = items.size

    fun setItems(newItems: List<Game>) {
        val diffResult = DiffUtil.calculateDiff(ItemDiffCallback(items, newItems))
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface OnItemClickListener {
        fun onClick(item: Game)
    }

    private class ItemDiffCallback(val oldItems: List<Game>, val newItems: List<Game>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].id == newItems[newItemPosition].id
        }

        override fun getOldListSize(): Int {
            return oldItems.size
        }

        override fun getNewListSize(): Int {
            return newItems.size
        }
    }
}