package com.example.easyfood.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.easyfood.models.Meal

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meal : Meal)

    @Delete
    suspend fun deleteMeal(meal : Meal)

    @Query("SELECT * FROM meal_information ORDER BY idMeal")
    fun getAllMeals() : LiveData<List<Meal>>
}