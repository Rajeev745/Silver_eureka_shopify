package com.example.e_commerceapplication.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceapplication.R
import com.example.e_commerceapplication.databinding.OrderListDetailLayoutBinding
import com.example.e_commerceapplication.models.Order

class OrderListRecyclerViewAdapter :
    RecyclerView.Adapter<OrderListRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: OrderListDetailLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            binding.apply {
                orderNameText.text = order.orderStatus
                orderDateText.text = order.date
                when (order.orderStatus) {
                    "Ordered" -> {
                        changeOrderStateColor(
                            colorImageOrder, colorImageOrder.context.resources.getColor(
                                R.color.g_orange_yellow
                            )
                        )
                    }
                    "Returned" -> {
                        changeOrderStateColor(
                            colorImageOrder, colorImageOrder.context.resources.getColor(
                                R.color.g_light_red
                            )
                        )
                    }
                    "Confirmed" -> {
                        changeOrderStateColor(
                            colorImageOrder, colorImageOrder.context.resources.getColor(
                                R.color.g_green
                            )
                        )
                    }
                    "Canceled" -> {
                        changeOrderStateColor(
                            colorImageOrder, colorImageOrder.context.resources.getColor(
                                R.color.g_red
                            )
                        )
                    }
                    "Shipped" -> {
                        changeOrderStateColor(
                            colorImageOrder, colorImageOrder.context.resources.getColor(
                                R.color.g_blue
                            )
                        )
                    }
                    else -> {
                        changeOrderStateColor(
                            colorImageOrder, colorImageOrder.context.resources.getColor(
                                R.color.g_black
                            )
                        )
                    }
                }

            }
        }
    }

    val differCallback = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            OrderListDetailLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = differ.currentList[position]
        holder.bind(order)
        holder.itemView.setOnClickListener {
            onClick?.invoke(order)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private fun changeOrderStateColor(imageView: ImageView, color: Int) {
        imageView.imageTintList = ColorStateList.valueOf(color)
    }

    var onClick: ((Order) -> Unit)? = null

}