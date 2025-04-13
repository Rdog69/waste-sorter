package com.random.example.wastesorterapp.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.random.example.wastesorterapp.R
import com.random.example.wastesorterapp.model.WasteClassification.WasteType
import com.random.example.wastesorterapp.model.WasteItem

class WasteItemAdapter(private val onItemClick: (WasteItem) -> Unit) :
    ListAdapter<WasteItem, WasteItemAdapter.WasteItemViewHolder>(WasteItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WasteItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_waste, parent, false)
        return WasteItemViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: WasteItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WasteItemViewHolder(
        itemView: View,
        private val onItemClick: (WasteItem) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val iconImageView: ImageView = itemView.findViewById(R.id.iv_waste_type_icon)
        private val nameTextView: TextView = itemView.findViewById(R.id.tv_waste_name)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tv_waste_description)
        private val typeTextView: TextView = itemView.findViewById(R.id.tv_waste_type)

        private var currentWasteItem: WasteItem? = null

        init {
            itemView.setOnClickListener {
                currentWasteItem?.let {
                    onItemClick(it)
                }
            }
        }

        fun bind(wasteItem: WasteItem) {
            currentWasteItem = wasteItem
            nameTextView.text = wasteItem.name
            descriptionTextView.text = wasteItem.description

            // Set type name
            val context = itemView.context
            typeTextView.text = wasteItem.wasteType.name.capitalize()

            // Set icon and background color based on waste type
            when (wasteItem.wasteType) {
                WasteType.RECYCLABLE -> {
                    iconImageView.setImageResource(R.drawable.recyclable) // Add this drawable
                    typeTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.recyclable)) // Define color in colors.xml
                }
                WasteType.COMPOST -> {
                    iconImageView.setImageResource(R.drawable.leaf) // Add this drawable
                    typeTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.compost)) // Define color in colors.xml
                }
                WasteType.LANDFILL -> {
                    iconImageView.setImageResource(R.drawable.trash) // Add this drawable
                    typeTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.landfill)) // Define color in colors.xml
                }
            }
        }
    }

    class WasteItemDiffCallback : DiffUtil.ItemCallback<WasteItem>() {
        override fun areItemsTheSame(oldItem: WasteItem, newItem: WasteItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WasteItem, newItem: WasteItem): Boolean {
            return oldItem == newItem
        }
    }
}

// Extension function to capitalize first letter of strings
fun String.capitalize(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}