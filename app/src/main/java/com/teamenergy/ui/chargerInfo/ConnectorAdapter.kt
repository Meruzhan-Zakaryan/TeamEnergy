package com.teamenergy.ui.chargerInfo

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teamenergy.R
import com.teamenergy.databinding.ItemChargerTypeBinding
import com.teamenergy.proxy.domain.Connector

class ConnectorAdapter() : RecyclerView.Adapter<ConnectorAdapter.ConnectorViewHolder>() {

    private val items = mutableListOf<Connector>()

    private lateinit var context: Context
    private lateinit var layoutInflater: LayoutInflater

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
        layoutInflater = LayoutInflater.from(context)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(items: List<Connector>?) {
        items?.let {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectorViewHolder =
        ConnectorViewHolder(ItemChargerTypeBinding.inflate(layoutInflater, parent, false))

    override fun onBindViewHolder(holder: ConnectorViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class ConnectorViewHolder(val binding: ItemChargerTypeBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(item: Connector) {
            binding.chargerTypeTextView.text = item.connectorType
            when(item.status){
               "Հասանելի" ->{
                   binding.chargerStateTextView.setTextColor(ContextCompat.getColor(binding.chargerStateTextView.context,R.color.green_color))
                   binding.chargerStateTextView.text = "Available"
               }
                "Զբաղված"->{
                    binding.chargerStateTextView.setTextColor(ContextCompat.getColor(binding.chargerStateTextView.context,R.color.yellow_color))
                    binding.chargerStateTextView.text = "Busy"
                }
                "Անհասանելի"->{
                    binding.chargerStateTextView.setTextColor(ContextCompat.getColor(binding.chargerStateTextView.context,R.color.red_color))
                    binding.chargerStateTextView.text = "Disabled"
                }
            }
            binding.kilovatTextView.text = item.power.toString() + "kw"
            binding.tarifTextView.text = item.price.toString()
        }
    }
}