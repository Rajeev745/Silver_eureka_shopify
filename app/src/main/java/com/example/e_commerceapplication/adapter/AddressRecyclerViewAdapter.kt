package com.example.e_commerceapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceapplication.databinding.AddressItemLytBinding
import com.example.e_commerceapplication.models.Address

class AddressRecyclerViewAdapter : RecyclerView.Adapter<AddressRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: AddressItemLytBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(address: Address) {
            binding.apply {
                cityName.text = address.city
                stateName.text = address.state
                countryName.text = address.country
                mobileNumber.text = address.mobileNumber
                houseNumberTxt.text = address.houseNumbre
                pinNumber.text = address.pinCode
                localArea.text = address.localArea
                streetNumber.text = address.street
            }
        }
    }

    val diffCallback = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AddressItemLytBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val address = differ.currentList[position]

        holder.bind(address)
        holder.itemView.setOnClickListener {
            onClick?.invoke(address)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((Address) -> Unit)? = null
}