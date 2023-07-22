package com.example.newsapp.repository

import com.example.newsapp.db.ArticleDateBase
import com.example.newsapp.api.RetrofitInstance

class NewsRepository(val db: ArticleDateBase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery:String,pageNumber: Int)=
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)


}