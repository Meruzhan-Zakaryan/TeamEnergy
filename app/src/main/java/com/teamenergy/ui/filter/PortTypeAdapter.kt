package com.teamenergy.ui.filter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.teamenergy.databinding.ItemConnectorPortTypeBinding
import com.teamenergy.proxy.domain.ConnectorType
import com.teamenergy.proxy.domain.Status

class PortTypeAdapter(val onConnectorTypeSelected: (ConnectorType) -> Unit) : RecyclerView.Adapter<PortTypeAdapter.PortTypeViewHolder>() {

    private val items = mutableListOf<ConnectorType>()

    private lateinit var context: Context
    private lateinit var layoutInflater: LayoutInflater

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
        layoutInflater = LayoutInflater.from(context)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(items: List<ConnectorType>?) {
        items?.let {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortTypeViewHolder =
        PortTypeViewHolder(ItemConnectorPortTypeBinding.inflate(layoutInflater, parent, false))

    override fun onBindViewHolder(holder: PortTypeViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class PortTypeViewHolder(val binding: ItemConnectorPortTypeBinding) : RecyclerView.ViewHolder(binding.root) {

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
                   onConnectorTypeSelected(items[it])
                }
                notifyDataSetChanged()
            }
        }

        fun bind(item: ConnectorType) {
            binding.checkbox.isChecked = item.isChecked == true
             binding.checkbox.text = item.value
        }
    }
}