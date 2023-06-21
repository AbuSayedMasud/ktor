package com.example.productappwithktor.api

import ApiService
import com.example.productappwithktor.model.Categories
import com.example.productappwithktor.model.ProductList
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


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
    override suspend fun getProduct(): ProductList {
        return try {
            val response = client.get<HttpResponse>("https://dummyjson.com/products")
            if (response.status == HttpStatusCode.OK) {
                val json = response.readText()
                val productList = Gson().fromJson(json, ProductList::class.java)
                productList
            } else {
                ProductList(0, emptyList(), 0, 0)
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            ProductList(0, emptyList(), 0, 0)
        }
    }

}
