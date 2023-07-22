package com.example.newsapp.ui.breakingNews

import android.os.Bundle
import com.example.newsapp.models.Article

interface Navigator {
    fun onNavigateToArticleFragment(article: Article)
}