package com.teamenergy.ui.selectCar

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamenergy.databinding.ItemCarTypeBinding
import com.teamenergy.proxy.domain.Car
import com.teamenergy.proxy.network.masterData.CarDto

class CarTypeAdapter(private val onItemClick: (item: Car) -> Unit) : RecyclerView.Adapter<CarTypeAdapter.CarViewHolder>() {

    private val items = mutableListOf<Car>()

    private lateinit var context: Context
    private lateinit var layoutInflater: LayoutInflater

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
        layoutInflater = LayoutInflater.from(context)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(items: List<Car>?) {
        items?.let {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder =
        CarViewHolder(ItemCarTypeBinding.inflate(layoutInflater, parent, false))

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class CarViewHolder(val binding: ItemCarTypeBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                adapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let { bindingAdapterPosition ->
                    onItemClick(items[bindingAdapterPosition])
                }
            }
        }

        fun bind(item: Car) {
            binding.nameTextView.text = item.name
        }
    }
}