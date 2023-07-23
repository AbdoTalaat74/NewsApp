package com.example.newsapp.ui.article

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.newsapp.base.BaseViewModel
import com.example.newsapp.db.ArticleDateBase
import com.example.newsapp.models.Article
import com.example.newsapp.repository.NewsRepository
import kotlinx.coroutines.launch

class ArticleViewModel(ctx: Context) : BaseViewModel<Navigator>() {

    private val newsRepository = NewsRepository(ArticleDateBase(ctx))
    fun saveArticle(article: Article) = viewModelScope.launch {
        val existingArticleCount = newsRepository.isArticleSaved(article.url)
        if (existingArticleCount >0){
            // Article already exists
            messageLiveData.postValue("Article already saved Previously")
        }else{
            // Article doesn't exist
            newsRepository.saveArticle(article)
            messageLiveData.postValue("Article Added Successfully")
        }

    }


}

