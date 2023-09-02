package com.example.e_commerceapplication.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceapplication.databinding.ColorRecyclerViewItemBinding

class ColorRecyclerviewAdapter : RecyclerView.Adapter<ColorRecyclerviewAdapter.ViewHolder>() {

    var selectedItem = -1

    inner class ViewHolder(val binding: ColorRecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(color: String, position: Int) {
            val imageColor: Int
            if (color == "#000") {
                imageColor = Color.parseColor("#000444")
            } else {
                imageColor = Color.parseColor(color)
            }
            binding.colorImage.setImageDrawable(ColorDrawable(imageColor))

            if (selectedItem == position) {
                binding.imageSelelctedSign.visibility = View.VISIBLE
            } else {
                binding.imageSelelctedSign.visibility = View.INVISIBLE
            }
        }
    }

    val diffUtil = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ColorRecyclerViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val color = differ.currentList[position]
        holder.bind(color, position)

        holder.itemView.setOnClickListener {
            if (selectedItem >= 0) {
                notifyItemChanged(holder.adapterPosition)
            }

            selectedItem = holder.adapterPosition
            notifyItemChanged(selectedItem)

            onClick?.invoke(color)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((String) -> Unit)? = null
}