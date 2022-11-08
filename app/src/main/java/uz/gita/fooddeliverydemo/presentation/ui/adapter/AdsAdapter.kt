package uz.gita.fooddeliverydemo.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.gita.fooddeliverydemo.databinding.ItemAdsBinding

class AdsAdapter : RecyclerView.Adapter<AdsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemAdsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            when (absoluteAdapterPosition) {
                0 -> binding.txt.text = "ADVERTISEMENT5"
                6 -> binding.txt.text = "ADVERTISEMENT1"
                else -> binding.txt.text = "ADVERTISEMENT$absoluteAdapterPosition"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsAdapter.ViewHolder {
        return ViewHolder(ItemAdsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AdsAdapter.ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 7
    }
}