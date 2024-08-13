package com.example.transcribeapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class GenericRvAdapter<T, VB : ViewBinding>(
    private val inflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val viewHolderBinder: VB.(T, Int) -> Unit,
    diffCallback: DiffUtil.ItemCallback<T> = DefaultDiffCallback()
) : ListAdapter<T, GenericRvAdapter<T, VB>.ViewHolder>(diffCallback) {

    private var onItemClickListener: ((T, Int) -> Unit)? = null

    fun setonItemClickListener(Listener: (T, Int) -> Unit) {
        onItemClickListener = Listener
    }

    inner class ViewHolder(private val binding: VB) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            binding.apply {
                viewHolderBinder.invoke(this, item, adapterPosition)
                itemView.setOnClickListener {
                    onItemClickListener?.invoke(item, adapterPosition)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = inflater.invoke(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    private class DefaultDiffCallback<T> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any) = oldItem == newItem

        override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any) = true

    }

}

