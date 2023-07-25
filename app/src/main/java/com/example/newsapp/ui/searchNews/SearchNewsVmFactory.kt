package com.example.newsapp.ui.searchNews

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.repository.NewsRepository

class SearchNewsVmFactory (private val newsRepository: NewsRepository,private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchNewsViewModel::class.java)) {
            return SearchNewsViewModel(newsRepository,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}