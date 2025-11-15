package com.example.rickandmortybrowser.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmortybrowser.R
import com.example.rickandmortybrowser.model.Character

class CharacterAdapter(
    private val items: MutableList<Character>
) : RecyclerView.Adapter<CharacterAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvMeta: TextView = itemView.findViewById(R.id.tvMeta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_character, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val c = items[position]
        holder.tvName.text = c.name
        holder.tvMeta.text = "${c.species} • ${c.status}"

        Glide.with(holder.itemView)
            .load(c.imageUrl)
            .into(holder.ivAvatar)
    }

    override fun getItemCount(): Int = items.size

    fun addAll(newItems: List<Character>) {
        val start = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(start, newItems.size)
    }
}
