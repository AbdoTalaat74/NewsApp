package com.example.newsapp.ui.searchNews

import com.example.newsapp.models.Article

interface Navigator {
    fun onNavigateToArticleFragment(article: Article)
}