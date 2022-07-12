package com.example.easyfood.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.databinding.ActivityMealBinding
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.models.Meal
import com.example.easyfood.viewModels.MealViewModel
import com.example.easyfood.viewModels.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding

    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealYoutube: String

    private lateinit var mealViewModel: MealViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)

        mealViewModel = ViewModelProvider(this,MealViewModelFactory(mealDatabase)).get(MealViewModel::class.java)

        getMealInfoFromIntent()

        hideViews()
        mealViewModel.getMealDetail(mealId)
        observeMealDetailsLiveData()
    }

    private var mealToSave : Meal? = null
    private fun observeMealDetailsLiveData() {
        mealViewModel.observeMealDetailLiveData().observe(this,object: Observer<Meal>{
            override fun onChanged(t: Meal?) {
                showViews()
                val meal = t

                mealToSave = t

                if (meal != null) {
                    binding.tvCategories.text = "Category: ${meal.strCategory}"
                    binding.tvArea.text = "Area: ${meal!!.strArea}"
                    binding.tvInstructionsSpace.text = meal.strInstructions

                    mealYoutube = meal.strYoutube
                }

            }

        })
    }

    private fun showViews() {
        binding.progressHorizontal.visibility = View.INVISIBLE
        binding.favBtn.visibility = View.VISIBLE
        binding.tvInstructionsSpace.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategories.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
    }

    private fun getMealInfoFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID).toString()
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME).toString()
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_IMG).toString()

        setInformationInViews()
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun hideViews(){
        binding.progressHorizontal.visibility = View.VISIBLE
        binding.favBtn.visibility = View.INVISIBLE
        binding.tvInstructionsSpace.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvCategories.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
    }

    fun onYoutubeButtonClicked(view: android.view.View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mealYoutube))
        startActivity(intent)
    }

    fun onFavClick(view: android.view.View) {
        mealToSave?.let {
            mealViewModel.insertMeal(it)
            Toast.makeText(this,"${it.strMeal} Saved!",Toast.LENGTH_SHORT).show()
        }
    }
}