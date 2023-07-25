package com.example.newsapp.ui.savedNews

import com.example.newsapp.models.Article

interface Navigator {
    fun onNavigateToArticleFragment(article: Article)
}