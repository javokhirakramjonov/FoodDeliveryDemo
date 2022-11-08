package uz.gita.fooddeliverydemo.presentation.ui.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.gita.fooddeliverydemo.R
import uz.gita.fooddeliverydemo.data.foodData.FoodData
import uz.gita.fooddeliverydemo.databinding.ItemFoodInFavouriteBinding

class FavouritesAdapter : RecyclerView.Adapter<FavouritesAdapter.ViewHolder>() {

    private var selectListener: ((FoodData) -> Unit)? = null
    private var deleteListener: ((Int) -> Unit)? = null
    private var addBasketListener: ((Int) -> Unit)? = null

    private var list = ArrayList<FoodData>()

    fun setSelectListener(listener: ((FoodData) -> Unit)) {
        selectListener = listener
    }

    fun setDeleteListener(listener: ((Int) -> Unit)) {
        deleteListener = listener
    }

    fun setAddBasketListener(listener: ((Int) -> Unit)) {
        addBasketListener = listener
    }

    fun submitList(foods: List<FoodData>) {
        list.clear()
        list.addAll(foods)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemFoodInFavouriteBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                mainContainer.setOnClickListener {
                    selectListener!!.invoke(list[absoluteAdapterPosition])
                }
                buttonTrash.setOnClickListener {
                    Log.d("TTT", "clicked delete")
                    deleteListener!!.invoke(list[absoluteAdapterPosition].id!!)
                }
                buttonAddToBasket.setOnClickListener {
                    addBasketListener!!.invoke(list[absoluteAdapterPosition].id!!)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind() {
            with(list[absoluteAdapterPosition]) {
                if (count > 0) {
                    binding.buttonAddToBasket.isEnabled = false
                    binding.backgroundBasket.background.setTint(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.gray
                        )
                    )
                } else {
                    binding.buttonAddToBasket.isEnabled = true
                    binding.backgroundBasket.background.setTint(
                        ContextCompat.getColor(
                            binding.root.context, R.color
                                .primary
                        )
                    )
                }

                Glide.with(binding.root)
                    .load(Uri.parse(image))
                    .error(R.drawable.test_food)
                    .into(binding.imageFood)
                binding.textNameFood.text = name
                binding.textCostFood.text = "$ $cost"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesAdapter.ViewHolder {
        return ViewHolder(ItemFoodInFavouriteBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FavouritesAdapter.ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}