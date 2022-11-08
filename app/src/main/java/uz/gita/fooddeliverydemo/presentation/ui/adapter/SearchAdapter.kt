package uz.gita.fooddeliverydemo.presentation.ui.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import uz.gita.fooddeliverydemo.R
import uz.gita.fooddeliverydemo.data.foodData.FoodData
import uz.gita.fooddeliverydemo.databinding.ItemFoodBinding

class SearchAdapter : ListAdapter<FoodData, SearchAdapter.ViewHolder>(FoodDiffUtil) {

    object FoodDiffUtil : DiffUtil.ItemCallback<FoodData>() {
        override fun areItemsTheSame(oldItem: FoodData, newItem: FoodData): Boolean {
            return oldItem.id == newItem.id && oldItem.favourite == newItem.favourite && oldItem.count == newItem.count
        }

        override fun areContentsTheSame(oldItem: FoodData, newItem: FoodData): Boolean {
            return oldItem == newItem
        }

    }

    private var addBasketListener: ((Int) -> Unit)? = null
    private var selectListener: ((FoodData) -> Unit)? = null
    private var clickFavouriteListener: ((FoodData) -> Unit)? = null

    fun setAddBasketListener(listener: ((Int) -> Unit)) {
        addBasketListener = listener
    }

    fun setSelectListener(listener: (FoodData) -> Unit) {
        selectListener = listener
    }

    fun setClickFavouriteListener(listener: (FoodData) -> Unit) {
        clickFavouriteListener = listener
    }

    inner class ViewHolder(private val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonLike.setOnClickListener {
                getItem(absoluteAdapterPosition).favourite = !getItem(absoluteAdapterPosition).favourite

                if (getItem(absoluteAdapterPosition).favourite)
                    binding.buttonLike.setImageResource(R.drawable.ic_active_like)
                else
                    binding.buttonLike.setImageResource(R.drawable.ic_like)

                clickFavouriteListener!!.invoke(getItem(absoluteAdapterPosition))
            }

            binding.selectItem.setOnClickListener {
                selectListener!!.invoke(getItem(absoluteAdapterPosition))
            }

            binding.buttonBasket.setOnClickListener {
                addBasketListener!!.invoke(getItem(absoluteAdapterPosition).id!!)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind() {
            with(getItem(absoluteAdapterPosition)) {
                if (getItem(absoluteAdapterPosition).favourite)
                    binding.buttonLike.setImageResource(R.drawable.ic_active_like)
                else
                    binding.buttonLike.setImageResource(R.drawable.ic_like)
                if (count > 0) {
                    binding.buttonBasket.isEnabled = false
                    binding.frameLayout.background.setTint(ContextCompat.getColor(binding.root.context, R.color.gray))
                } else {
                    binding.buttonBasket.isEnabled = true
                    binding.frameLayout.background.setTint(
                        ContextCompat.getColor(
                            binding.root.context, R.color
                                .primary
                        )
                    )
                }
                binding.containerFood.visibility = View.VISIBLE
                binding.textRestaurantName.visibility = View.GONE

                Glide.with(binding.root)
                    .load(Uri.parse(image))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.test_food)
                    .into(binding.imageFood)
                binding.textNameFood.text = name
                binding.textCostFood.text = "$ $cost"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        return ViewHolder(ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        holder.bind()
    }
}