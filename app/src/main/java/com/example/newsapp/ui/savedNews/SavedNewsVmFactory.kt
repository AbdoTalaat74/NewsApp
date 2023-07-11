package com.example.newsapp.ui.savedNews

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SavedNewsVmFactory (private val ctx: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedNewsViewModel::class.java)) {
            return SavedNewsViewModel(ctx) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}