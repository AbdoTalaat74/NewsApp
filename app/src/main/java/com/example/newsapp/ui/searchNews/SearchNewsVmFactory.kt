package com.example.newsapp.ui.searchNews

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchNewsVmFactory (private val ctx: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchNewsViewModel::class.java)) {
            return SearchNewsViewModel(ctx) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}