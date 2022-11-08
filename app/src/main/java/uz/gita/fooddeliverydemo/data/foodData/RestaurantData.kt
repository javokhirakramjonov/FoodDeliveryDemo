package uz.gita.fooddeliverydemo.data.foodData

data class RestaurantData(
    val id: Int? = 0,
    val name: String? = "",
    val image: String? = "",

    var selected: Boolean = false
)