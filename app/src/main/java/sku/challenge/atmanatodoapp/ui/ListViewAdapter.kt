package sku.challenge.atmanatodoapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sku.challenge.atmanatodoapp.databinding.ListItemBinding
import sku.challenge.atmanatodoapp.vo.Item

class ListViewAdapter(
    private val onItemClick: (position: Int) -> Unit
) : ListAdapter<Item, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListItemViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ListItemViewHolder).bind(item)
    }

    class ListItemViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.listItemCardView.setOnClickListener {
                onClick()
            }
        }

        fun bind(item: Item) {
            binding.idTextview.text = item.id.toString()
            binding.emailTextview.text = item.email
            binding.nameTextview.text = "${item.firstName} ${item.lastName}"

            binding.executePendingBindings()
        }

        private fun onClick() {
            TODO()
        }

    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Item>() {

            override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem

        }
    }
}