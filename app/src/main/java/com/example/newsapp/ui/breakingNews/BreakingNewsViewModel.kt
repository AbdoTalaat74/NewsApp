package com.example.newsapp.ui.breakingNews

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_BLUETOOTH
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
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

class BreakingNewsViewModel(
    private val newsRepository: NewsRepository,
    private val application: Application
) :
    BaseViewModel<Navigator>() {
    lateinit var navigator: Navigator

    private var countryCode = "us"


    private val _breakingNews = MutableLiveData<Resource<NewsResponse>>()
    val breakingNews: LiveData<Resource<NewsResponse>>
        get() = _breakingNews

    var breakingNewsPage = 1

    private var breakingNewsResponse: NewsResponse? = null
    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {

            response.body()?.let {
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = it
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }


    init {
        getBreakingNews(countryCode)
    }

    fun navigateToArticleFragment(article: Article) {
        navigator.onNavigateToArticleFragment(article)
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(TRANSPORT_WIFI) -> true
            actNw.hasTransport(TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(TRANSPORT_ETHERNET) -> true
            actNw.hasTransport(TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }


    private suspend fun safeBreakingNewsCall(countryCode: String) {
        _breakingNews.postValue(Resource.Loading())
        try {
            if (isNetworkAvailable()) {
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                _breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                _breakingNews.postValue(Resource.Error("No internet connection"))
                messageLiveData.postValue("No internet connection")
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _breakingNews.postValue(Resource.Error("Network Failure"))
                    messageLiveData.postValue("Network Failure")
                }

                else -> {
                    _breakingNews.postValue(Resource.Error("Conversion Error"))
                    messageLiveData.postValue("Conversion Error")
                }
            }
        }
    }


}