package com.example.productappwithktor.api

import ApiService
import com.example.productappwithktor.model.Categories
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*


class CategoryServiceImp(private val client: HttpClient) : ApiService {
    override suspend fun getCategory(): Categories {
        return try {
            val json = client.get<String>("https://dummyjson.com/products/categories")
            val categoryList = json.substring(1, json.length - 1).split(",")
            Categories().apply { addAll(categoryList) }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            Categories()
        }
    }
}
