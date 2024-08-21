package com.example.quotes.repository

import com.example.quotes.Model.Category
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CategoryRepository {
    suspend fun loadCategories(): MutableList<Category> {
        return try {
            val categories = mutableListOf<Category>()
            val documents = FirebaseFirestore.getInstance().collection("Categories")
                .get().await()
            for (document in documents) {
                val category = Category(
                    id = document.id,
                    name = document.getString("name") ?: ""
                )
                categories.add(category)
            }
            categories
        } catch (e: Exception) {
            mutableListOf()
        }

    }


}