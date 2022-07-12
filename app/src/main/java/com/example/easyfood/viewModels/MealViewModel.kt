package com.example.easyfood.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.models.Meal
import com.example.easyfood.models.MealList
import com.example.easyfood.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(val mealDatabase: MealDatabase): ViewModel() {
    private var mealDetailLiveData = MutableLiveData<Meal>()

    fun getMealDetail(id: String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null){
                    mealDetailLiveData.value = response.body()!!.meals[0]
                }
                else
                    return;
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Error from MealViewModel",t.message.toString())
            }

        })
    }

    fun observeMealDetailLiveData() : LiveData<Meal>{
        return mealDetailLiveData
    }

    fun insertMeal(meal : Meal){
        viewModelScope.launch(Dispatchers.IO) {
            mealDatabase.mealDao().upsert(meal)
        }
    }

}