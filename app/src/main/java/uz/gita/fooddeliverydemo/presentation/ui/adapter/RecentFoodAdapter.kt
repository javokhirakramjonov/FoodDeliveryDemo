package uz.gita.fooddeliverydemo.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.fooddeliverydemo.data.foodData.SearchData
import uz.gita.fooddeliverydemo.databinding.ItemFoodInRecentBinding

class RecentFoodAdapter : ListAdapter<SearchData, RecentFoodAdapter.ViewHolder>(TextDiffUtil) {

    private var selectListener: ((SearchData) -> Unit)? = null
    private var deleteListener: ((SearchData) -> Unit)? = null

    fun setSelectListener(listener: (SearchData) -> Unit) {
        selectListener = listener
    }

    fun setDeleteListener(listener: (SearchData) -> Unit) {
        deleteListener = listener
    }

    private object TextDiffUtil : DiffUtil.ItemCallback<SearchData>() {
        override fun areItemsTheSame(oldItem: SearchData, newItem: SearchData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SearchData, newItem: SearchData): Boolean {
            return oldItem == newItem
        }

    }

    inner class ViewHolder(private val binding: ItemFoodInRecentBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.containerSelect.setOnClickListener {
                selectListener!!.invoke(getItem(absoluteAdapterPosition))
            }
            binding.buttonDelete.setOnClickListener {
                deleteListener!!.invoke(getItem(absoluteAdapterPosition))
            }
        }

        fun bind() {
            binding.textName.text = getItem(absoluteAdapterPosition).text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentFoodAdapter.ViewHolder {
        return ViewHolder(ItemFoodInRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecentFoodAdapter.ViewHolder, position: Int) {
        holder.bind()
    }

}