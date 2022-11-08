package uz.gita.fooddeliverydemo.data.foodData

import java.io.Serializable

data class FoodData(
    val id: Int? = 0,
    val restaurant_id: Int? = 0,
    val name: String? = "",
    val image: String? = "",
    val description: String? = "",
    val cost: Double? = 0.0,

    var restaurant_name: String? = "",
    var sub_id: Int = 0,
    var favourite: Boolean = false,
    var count: Int = 0
) : Serializable