package sku.challenge.atmanatodoapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sku.challenge.atmanatodoapp.databinding.ListItemBinding
import sku.challenge.atmanatodoapp.vo.Item

class ListViewAdapter(
    private val listener: ItemButtonListener
) : ListAdapter<Item, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListItemViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ListItemViewHolder).bind(item)
    }

    class ListItemViewHolder(
        private val binding: ListItemBinding,
        private val listener: ItemButtonListener
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.editImageButton.setOnClickListener {
                val position = adapterPosition
                listener.edit(position)
            }

            binding.deleteImageButton.setOnClickListener {
                val position = adapterPosition
                listener.delete(position)
            }
            if (isRemoteList()) {
                hideImageButtons()
            }
        }

        fun bind(item: Item) {
            binding.idTextview.text = item.id.toString()
            binding.emailTextview.text = item.email
            binding.nameTextview.text = "${item.firstName} ${item.lastName}"

            binding.executePendingBindings()
        }

        private fun hideImageButtons() {
            binding.editImageButton.visibility = View.GONE
            binding.deleteImageButton.visibility = View.GONE
        }

        private fun isRemoteList() = listener == ItemButtonListener.NULL_ITEM_BUTTON_LISTENER

    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Item>() {

            override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem

        }
    }
}