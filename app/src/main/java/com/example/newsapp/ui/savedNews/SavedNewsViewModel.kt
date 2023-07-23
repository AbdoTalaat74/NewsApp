package com.example.newsapp.ui.savedNews

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.newsapp.base.BaseViewModel
import com.example.newsapp.db.ArticleDateBase
import com.example.newsapp.models.Article
import com.example.newsapp.repository.NewsRepository
import kotlinx.coroutines.launch

class SavedNewsViewModel(ctx:Context):BaseViewModel<Navigator>() {

    private val newsRepository = NewsRepository(ArticleDateBase(ctx))

    fun getSavedArticles() = newsRepository.getSavedArticles()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.saveArticle(article)
        messageLiveData.postValue("Article Added Successfully")
    }
}