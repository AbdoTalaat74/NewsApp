package com.example.newsapp.repository

import com.example.newsapp.db.ArticleDateBase
import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.models.Article

class NewsRepository(private val db: ArticleDateBase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun saveArticle(article: Article) = db.getArticleDao().upsert(article)

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
    fun getSavedArticles() = db.getArticleDao().getAllArticles()

    suspend fun isArticleSaved(articleUrl: String) =
        db.getArticleDao().isArtAlreadySaved(articleUrl)

}