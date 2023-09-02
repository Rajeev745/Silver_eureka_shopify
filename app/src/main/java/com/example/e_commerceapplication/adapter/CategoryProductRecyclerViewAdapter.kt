package com.example.e_commerceapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerceapplication.databinding.CategoryRecyclerViewItemBinding
import com.example.e_commerceapplication.models.Product

class CategoryProductRecyclerViewAdapter : RecyclerView.Adapter<CategoryProductRecyclerViewAdapter.ViewHolder>() {

    private var addToCartProductItemClickListener: AdapterAddToCartClickListener? = null

    inner class ViewHolder(val binding: CategoryRecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.image).into(categoryImage)
                categoryProductName.text = product.name
                categoryProductPrice.text = product.price.toString() + "Rs"
                addToCartBtn.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        addToCartProductItemClickListener?.addToCartItemClickListener(
                            position,
                            product
                        )
                    }
                }
            }
        }
    }

    val diffUtil = object : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CategoryRecyclerViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setOnItemClickListener(listener: AdapterAddToCartClickListener) {
        addToCartProductItemClickListener = listener
    }
}