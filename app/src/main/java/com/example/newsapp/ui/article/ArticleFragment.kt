package com.example.newsapp.ui.article

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.base.BaseFragment
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.db.ArticleDateBase
import com.example.newsapp.repository.NewsRepository

class ArticleFragment:BaseFragment<FragmentArticleBinding,ArticleViewModel>() {

    private val args: ArticleFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeToLiveData()
        val article = args.selectedArticle
        viewDataBinding.article = article
        viewDataBinding.viewModel = viewModel
        viewDataBinding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        viewDataBinding.fab.setOnClickListener {
            viewModel.saveArticle(article)
        }

    }


    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): ArticleViewModel {

        return ViewModelProvider(this,ArticleVMFactory(NewsRepository(ArticleDateBase(requireContext()))))[ArticleViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_article
    }
}