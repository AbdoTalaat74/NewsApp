package com.example.newsapp.ui.breakingNews

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BreakingNewsVMFactory (private val ctx: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BreakingNewsViewModel::class.java)) {
            return BreakingNewsViewModel(ctx) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}