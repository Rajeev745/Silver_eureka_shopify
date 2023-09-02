package com.example.e_commerceapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerceapplication.databinding.CartListItemBinding
import com.example.e_commerceapplication.models.CartProduct

class CartProductsAdapter : RecyclerView.Adapter<CartProductsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CartListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.image).into(cartProductImage)
                cartProductName.text = cartProduct.product.name
                cartProductPrice.text = cartProduct.product.price.toString()
                cartQuantityCounter.text = cartProduct.quantity.toString()
            }
        }
    }

    val diffCallback = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CartListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartProduct = differ.currentList[position]

        holder.bind(cartProduct)

        holder.itemView.setOnClickListener {
            onClick?.invoke(cartProduct)
        }

        holder.binding.cartIncreaseQuantity.setOnClickListener {
            onIncreaseClick?.invoke(cartProduct)
        }

        holder.binding.cartDecreaseQuantity.setOnClickListener {
            onDecreaseClick?.invoke(cartProduct)
        }

        holder.binding.cartDeleteQuantity.setOnClickListener {
            onDeleteClick?.invoke(cartProduct)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((CartProduct) -> Unit)? = null
    var onIncreaseClick: ((CartProduct) -> Unit)? = null
    var onDecreaseClick: ((CartProduct) -> Unit)? = null
    var onDeleteClick: ((CartProduct) -> Unit)? = null
}