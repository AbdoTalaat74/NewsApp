package com.example.newsapp.ui.searchNews

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.base.BaseViewModel
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class SearchNewsViewModel(
    private val newsRepository: NewsRepository,
    private val application: Application
) : BaseViewModel<Navigator>() {


    lateinit var navigator: Navigator
    private val _searchNews = MutableLiveData<Resource<NewsResponse>>()
    val searchNews: LiveData<Resource<NewsResponse>>
        get() = _searchNews

    var searchNewsPage = 1
    private var searchNewsResponse: NewsResponse? = null

    private var newSearchQuery:String? = null
    private var oldSearchQuery:String? = null

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)

    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if(searchNewsResponse == null || newSearchQuery != oldSearchQuery) {
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                } else {
                    searchNewsPage++
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun navigateToArticleFragment(article: Article) {
        navigator.onNavigateToArticleFragment(article)
    }


    private suspend fun safeSearchNewsCall(searchQuery: String) {
        newSearchQuery = searchQuery
        _searchNews.postValue(Resource.Loading())
        try {
            if(isNetworkAvailable()) {
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                _searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                _searchNews.postValue(Resource.Error("No internet connection"))
                messageLiveData.postValue("No internet connection")
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException ->{_searchNews.postValue(Resource.Error("Network Failure"))
                    messageLiveData.postValue("No internet connection")
                }
                else -> {
                    _searchNews.postValue(Resource.Error("Conversion Error"))
                    messageLiveData.postValue("Conversion Error")
                }
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

}