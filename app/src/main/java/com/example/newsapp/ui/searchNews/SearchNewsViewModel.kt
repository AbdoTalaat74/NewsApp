package com.example.newsapp.ui.searchNews

import android.content.Context
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

class SearchNewsViewModel(ctx:Context):BaseViewModel<Navigator>() {

    private val newsRepository = NewsRepository(ArticleDateBase(ctx))

    lateinit var navigator: Navigator
    private val _searchNews = MutableLiveData<Resource<NewsResponse>>()
    val searchNews: LiveData<Resource<NewsResponse>>
        get() = _searchNews

    private val searchNewsPage = 1

     fun searchNews(searchQuery:String) = viewModelScope.launch {
        _searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuery,searchNewsPage)
        _searchNews.postValue(handleSearchNews(response))
    }

    private fun handleSearchNews(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun navigateToArticleFragment(article: Article){
        navigator.onNavigateToArticleFragment(article)
    }


}