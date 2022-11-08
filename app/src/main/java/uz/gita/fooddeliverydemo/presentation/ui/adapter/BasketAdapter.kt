package uz.gita.fooddeliverydemo.presentation.ui.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import uz.gita.fooddeliverydemo.R
import uz.gita.fooddeliverydemo.data.foodData.FoodData
import uz.gita.fooddeliverydemo.databinding.ItemFoodInBasketBinding

class BasketAdapter : RecyclerView.Adapter<BasketAdapter.ViewHolder>() {


    private var changeCountListener: ((Int, Int) -> Unit)? = null
    private var deleteFoodListener: ((Int) -> Unit)? = null
    private var getDescriptionListener: ((FoodData) -> Unit)? = null

    fun setOldCount(oldCount: Pair<Int, Int>) {
        var index = 0
        list.forEach {
            if (it.id == oldCount.first) {
                it.count = oldCount.second
                notifyItemChanged(index)
                return@forEach
            }
            index++
        }
    }

    fun setChangeCountListener(listener: (Int, Int) -> Unit) {
        changeCountListener = listener
    }

    fun setDeleteFoodListener(listener: (Int) -> Unit) {
        deleteFoodListener = listener
    }

    fun setGetDescriptionListener(listener: (FoodData) -> Unit) {
        getDescriptionListener = listener
    }

    private var list = ArrayList<FoodData>()

    fun submitList(foods: List<FoodData>) {
        list.clear()
        list.addAll(foods)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemFoodInBasketBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.foodSelect.setOnClickListener {
                getDescriptionListener!!.invoke(list[absoluteAdapterPosition])
            }
            binding.buttonPlus.setOnClickListener {
                list[absoluteAdapterPosition].count++
                changeCountListener!!.invoke(list[absoluteAdapterPosition].id!!, list[absoluteAdapterPosition].count)
            }
            binding.buttonMinus.setOnClickListener {
                list[absoluteAdapterPosition].count--
                if (list[absoluteAdapterPosition].count == 1)
                    binding.buttonMinus.isEnabled = false
                changeCountListener!!.invoke(list[absoluteAdapterPosition].id!!, list[absoluteAdapterPosition].count)
            }
            binding.buttonTrash.setOnClickListener {
                deleteFoodListener!!.invoke(list[absoluteAdapterPosition].id!!)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind() {
            with(list[absoluteAdapterPosition]) {
                Glide.with(binding.root)
                    .load(Uri.parse(image))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.test_food)
                    .into(binding.imageFood)
                binding.textNameFood.text = name
                binding.textCostFood.text = "$ $cost"
                binding.textQuantity.setText("$count")
                binding.buttonMinus.isEnabled = count > 0
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketAdapter.ViewHolder {
        return ViewHolder(ItemFoodInBasketBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BasketAdapter.ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}