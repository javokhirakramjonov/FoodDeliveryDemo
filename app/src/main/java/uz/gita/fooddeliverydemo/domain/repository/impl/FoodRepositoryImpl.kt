package uz.gita.fooddeliverydemo.domain.repository.impl

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import uz.gita.fooddeliverydemo.data.foodData.AdsData
import uz.gita.fooddeliverydemo.data.foodData.FoodData
import uz.gita.fooddeliverydemo.data.foodData.RestaurantData
import uz.gita.fooddeliverydemo.data.foodData.SearchData
import uz.gita.fooddeliverydemo.domain.repository.FoodRepository
import uz.gita.fooddeliverydemo.domain.repository.LocalRepository
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val fireStore: FirebaseFirestore,
    private val localData: LocalRepository
) : FoodRepository {

    private lateinit var foodsCached: List<FoodData>
    private lateinit var restaurantsCached: List<RestaurantData>
    private lateinit var foodsByRestaurantCached: ArrayList<ArrayList<FoodData>>

    override suspend fun getAds(success: (List<AdsData>) -> Unit, failure: (Throwable) -> Unit) {
        //todo
    }

    override suspend fun getRestaurants(success: (List<RestaurantData>) -> Unit, failure: (Throwable) -> Unit) {
        if (!::restaurantsCached.isInitialized) {
            fireStore.collection("restaurants").orderBy("id").get()
                .addOnSuccessListener {
                    val list = it.toObjects(RestaurantData::class.java)
                    restaurantsCached = list
                    foodsByRestaurantCached = ArrayList()
                    for (i in 0..list.size)
                        foodsByRestaurantCached.add(ArrayList())
                    success.invoke(list)
                }
                .addOnFailureListener {
                    failure.invoke(it)
                }
        } else {
            success.invoke(restaurantsCached)
        }
    }

    override suspend fun getFoods(success: (List<FoodData>) -> Unit, failure: (Throwable) -> Unit) {
        if (!::foodsCached.isInitialized) {
            fireStore.collection("foods").orderBy("id").get()
                .addOnSuccessListener {
                    val list = it.toObjects(FoodData::class.java)
                    foodsCached = list
                    foodsCached.forEach { food ->
                        foodsByRestaurantCached[food.restaurant_id!!].add(food)
                    }
                    success.invoke(list)
                }
                .addOnFailureListener {
                    failure.invoke(it)
                }
        } else {
            success.invoke(foodsCached)
        }
    }

    override suspend fun getFoodsByRestaurant(
        list: List<Int>,
        success: (List<FoodData>) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        if (list.isEmpty()) {
            success.invoke(foodsCached)
            return
        }
        val temp = ArrayList<FoodData>()
        for (i in list)
            temp.addAll(foodsByRestaurantCached[i])
        success.invoke(temp)
    }

    override suspend fun setFavourite(id: Int, liked: Boolean, success: (Unit) -> Unit, failure: (Throwable) -> Unit) {
        val phone = localData.getUser()
        if (liked) {
            firebaseDatabase.reference.child("favourites").child("$id$phone").setValue(
                hashMapOf(
                    "id" to id,
                    "user" to phone
                )
            )
                .addOnSuccessListener {
                    success.invoke(Unit)
                }
                .addOnFailureListener {
                    failure.invoke(it)
                }
        } else {
            firebaseDatabase.reference.child("favourites").child("$id$phone").removeValue()
                .addOnSuccessListener {
                    success.invoke(Unit)
                }
                .addOnFailureListener {
                    failure.invoke(it)
                }
        }
    }

    override suspend fun getFavourite(success: (List<Int>) -> Unit, failure: (Throwable) -> Unit) {
        val phone = localData.getUser()
        firebaseDatabase.reference.child("favourites").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Int>()
                snapshot.children.forEach { food ->
                    val data = food.value as HashMap<*, *>
                    if ((data["user"] as String) == phone)
                        list.add((data["id"] as Long).toInt())
                }
                success.invoke(list)
            }

            override fun onCancelled(error: DatabaseError) {
                failure.invoke(error.toException())
            }

        })
    }

    override suspend fun changeCountFood(
        id: Int,
        count: Int,
        success: (Unit) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        val phone = localData.getUser()
        when (count) {
            0 -> {
                firebaseDatabase.reference.child("basket").child("$id$phone").removeValue()
                    .addOnSuccessListener {
                        success.invoke(Unit)
                    }
                    .addOnFailureListener {
                        failure.invoke(it)
                    }
            }
            else -> {
                firebaseDatabase.reference.child("basket").child("$id$phone").setValue(
                    hashMapOf(
                        "id" to id,
                        "user" to phone,
                        "count" to count
                    )
                )
                    .addOnSuccessListener {
                        success.invoke(Unit)
                    }
                    .addOnFailureListener {
                        failure.invoke(it)
                    }
            }
        }
    }

    override suspend fun getFoodCount(success: (List<Pair<Int, Int>>) -> Unit, failure: (Throwable) -> Unit) {
        val phone = localData.getUser()
        firebaseDatabase.reference.child("basket").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Pair<Int, Int>>()
                snapshot.children.forEach { food ->
                    val data = food.value as HashMap<*, *>
                    if ((data["user"] as String) == phone)
                        list.add(Pair((data["id"] as Long).toInt(), (data["count"] as Long).toInt()))
                }
                success.invoke(list)
            }

            override fun onCancelled(error: DatabaseError) {
                failure.invoke(error.toException())
            }

        })
    }

    override suspend fun getFoodBySearch(
        text: String,
        success: (List<FoodData>) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        if (::foodsCached.isInitialized) {
            success.invoke(filterFoods(text, foodsCached))
        } else {
            fireStore.collection("foods").orderBy("id").get()
                .addOnSuccessListener {
                    val list = it.toObjects(FoodData::class.java)
                    foodsCached = list
                    foodsCached.forEach { food ->
                        foodsByRestaurantCached[food.restaurant_id!!].add(food)
                    }
                    success.invoke(filterFoods(text, foodsCached))
                }
                .addOnFailureListener {
                    failure.invoke(it)
                }
        }
    }

    override suspend fun getRecentSearchedFoods(success: (List<SearchData>) -> Unit, failure: (Throwable) -> Unit) {
        val phone = localData.getUser()
        fireStore.collection("recentSearched").whereEqualTo("user", phone).get()
            .addOnSuccessListener {
                val temp = it.toObjects(SearchData::class.java)
                success.invoke(temp)
            }
            .addOnFailureListener {
                failure.invoke(it)
            }
    }

    override suspend fun addFoodToRecentSearched(
        data: SearchData,
        success: (Unit) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        val phone = localData.getUser()
        fireStore.collection("recentSearched").document("${data.text}$phone").set(data.copy(user = phone))
            .addOnSuccessListener {
                success.invoke(Unit)
            }
            .addOnFailureListener {
                failure.invoke(it)
            }
    }

    override suspend fun deleteFoodFromRecentSearched(
        data: SearchData,
        success: (Unit) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        val phone = localData.getUser()
        fireStore.collection("recentSearched").document("${data.text}$phone").delete()
            .addOnSuccessListener {
                success.invoke(Unit)
            }
            .addOnFailureListener {
                failure.invoke(it)
            }
    }

    private fun filterFoods(text: String, list: List<FoodData>): List<FoodData> {
        val filtered = ArrayList<FoodData>()
        list.forEach {
            if (it.name!!.lowercase().contains(text.lowercase())) {
                filtered.add(it)
            }
        }
        return filtered
    }

}