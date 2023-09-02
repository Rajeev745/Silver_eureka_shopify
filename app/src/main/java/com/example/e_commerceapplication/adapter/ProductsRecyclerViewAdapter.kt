package com.example.e_commerceapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerceapplication.databinding.BestProductItemRecyclerViewBinding
import com.example.e_commerceapplication.models.Product

class ProductsRecyclerViewAdapter(private val screenWidth: Int) :
    RecyclerView.Adapter<ProductsRecyclerViewAdapter.ProductsViewHolder>() {

    private var addToCartProductItemClickListener: AdapterAddToCartClickListener? = null

    inner class ProductsViewHolder(private val binding: BestProductItemRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.image).into(productImage)
                productName.text = product.name
                productPrice.text = product.price.toString() + "Rs"
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

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val itemWidth = screenWidth / 2
        val itemView = BestProductItemRecyclerViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).apply {
            root.layoutParams.width = itemWidth
        }

        return ProductsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((Product) -> Unit)? = null

    fun setOnItemClickListener(listener: AdapterAddToCartClickListener) {
        addToCartProductItemClickListener = listener
    }
}