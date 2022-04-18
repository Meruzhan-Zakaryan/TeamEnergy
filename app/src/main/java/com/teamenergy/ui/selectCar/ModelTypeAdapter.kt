package com.teamenergy.ui.selectCar

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamenergy.databinding.ItemCarTypeBinding
import com.teamenergy.proxy.domain.CarModel
import com.teamenergy.proxy.network.masterData.CarModelDto

class ModelTypeAdapter(private val onItemClick: (item: CarModel) -> Unit) : RecyclerView.Adapter<ModelTypeAdapter.ModelViewHolder>() {

    private val items = mutableListOf<CarModel>()

    private lateinit var context: Context
    private lateinit var layoutInflater: LayoutInflater

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
        layoutInflater = LayoutInflater.from(context)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(items: List<CarModel>?) {
        items?.let {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder =
        ModelViewHolder(ItemCarTypeBinding.inflate(layoutInflater, parent, false))

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class ModelViewHolder(val binding: ItemCarTypeBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                adapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let { bindingAdapterPosition ->
                    onItemClick(items[bindingAdapterPosition])
                }
            }
        }

        fun bind(item: CarModel) {
            binding.nameTextView.text = item.name
        }
    }
}