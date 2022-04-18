package com.teamenergy.ui.filter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teamenergy.R
import com.teamenergy.databinding.ItemConnectorPortTypeBinding
import com.teamenergy.proxy.domain.Status

class ChargerStatusAdapter(val onStatusSelected: (Status) -> Unit) : RecyclerView.Adapter<ChargerStatusAdapter.ChargerStatusViewHolder>() {

    private val items = mutableListOf<Status>()

    private lateinit var context: Context
    private lateinit var layoutInflater: LayoutInflater

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
        layoutInflater = LayoutInflater.from(context)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(items: List<Status>?) {
        items?.let {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChargerStatusViewHolder =
        ChargerStatusViewHolder(ItemConnectorPortTypeBinding.inflate(layoutInflater, parent, false))

    override fun onBindViewHolder(holder: ChargerStatusViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    inner class ChargerStatusViewHolder(val binding: ItemConnectorPortTypeBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.checkbox.setOnClickListener { view ->
                val isChecked = (view as? CompoundButton)?.isChecked
                adapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let {
                    if (isChecked == true) {
                        for (item in items) {
                            item.isChecked = false
                        }
                        items[adapterPosition].isChecked = true
                    } else {
                        items[adapterPosition].isChecked = false
                    }
                    onStatusSelected(items[adapterPosition])
                }
                notifyDataSetChanged()
            }
        }

        fun bind(item: Status) {
            binding.checkbox.isChecked = item.isChecked == true
            when (item.value) {
                "Հասանելի" -> {
                    binding.checkbox.text = "Available"
                }
                "Զբաղված" -> {
                    binding.checkbox.text = "Busy"
                }
                "Անհասանելի" -> {
                    binding.checkbox.text = "Disabled"
                }
                "Սխալ" -> {
                    binding.checkbox.text = "Mistake"
                }
            }
        }
    }
}