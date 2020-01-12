package com.lukasstancikas.gamesapp.feature.gamedetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.lukasstancikas.gamesapp.R
import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.Screenshot
import kotlinx.android.synthetic.main.item_game.view.*
import kotlinx.android.synthetic.main.item_screenshot.view.*

class ScreenshotAdapter : RecyclerView.Adapter<ScreenshotAdapter.MyViewHolder>() {
    private val items = mutableListOf<Screenshot>()
    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_screenshot, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener?.onClick(items[position])
        }
        Glide
            .with(holder.itemView.context)
            .load(items[position].trimmedUrl())
            .placeholder(R.drawable.ic_photo)
            .error(R.drawable.ic_error)
            .centerInside()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.itemView.gameDetailsScreenshot)
    }

    override fun getItemCount() = items.size

    fun setItems(newItems: List<Screenshot>) {
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
        fun onClick(item: Screenshot)
    }

    private class ItemDiffCallback(val oldItems: List<Screenshot>, val newItems: List<Screenshot>) :
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