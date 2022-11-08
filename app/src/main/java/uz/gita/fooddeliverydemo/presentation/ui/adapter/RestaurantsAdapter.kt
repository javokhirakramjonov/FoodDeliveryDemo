package uz.gita.fooddeliverydemo.presentation.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.gita.fooddeliverydemo.R
import uz.gita.fooddeliverydemo.data.foodData.RestaurantData
import uz.gita.fooddeliverydemo.databinding.ItemRestaurantBinding

class RestaurantsAdapter : RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {

    private var restaurantSelectedListener: ((List<RestaurantData>) -> Unit)? = null
    private var list = ArrayList<RestaurantData>()

    fun setRestaurantSelectedListener(listener: (List<RestaurantData>) -> Unit) {
        restaurantSelectedListener = listener
    }

    fun submitList(restaurants: List<RestaurantData>) {
        list.clear()
        list.addAll(restaurants)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.container.setOnClickListener {
                list[absoluteAdapterPosition].selected = !list[absoluteAdapterPosition].selected
                binding.container.isSelected = list[absoluteAdapterPosition].selected
                restaurantSelectedListener!!.invoke(list)
            }
        }

        fun bind() {
            binding.container.isSelected = list[absoluteAdapterPosition].selected
            Glide.with(binding.root)
                .load(Uri.parse(list[absoluteAdapterPosition].image))
                .placeholder(R.drawable.test_food)
                .into(binding.imageRestaurant)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsAdapter.ViewHolder {
        return ViewHolder(ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RestaurantsAdapter.ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}