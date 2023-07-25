package com.example.newsapp.ui.breakingNews

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.base.BaseViewModel
import com.example.newsapp.db.ArticleDateBase
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class BreakingNewsViewModel(private val newsRepository: NewsRepository) :
    BaseViewModel<Navigator>() {
    lateinit var navigator: Navigator

    var countryCode = "us"


    private val _breakingNews = MutableLiveData<Resource<NewsResponse>>()
    val breakingNews:LiveData<Resource<NewsResponse>>
        get() = _breakingNews

    private val breakingNewsPage = 1

    private fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        _breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)
        _breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }


    init {
        getBreakingNews(countryCode)
    }

    fun navigateToArticleFragment(article: Article){
        navigator.onNavigateToArticleFragment(article)
    }

}