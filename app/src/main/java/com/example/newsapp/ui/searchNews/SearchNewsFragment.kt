package com.example.newsapp.ui.searchNews

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.R
import com.example.newsapp.base.BaseFragment
import com.example.newsapp.databinding.FragmentSearchNewsBinding

class SearchNewsFragment:BaseFragment<FragmentSearchNewsBinding,SearchNewsViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SearchNewsViewModel {
        val vmFactory = SearchNewsVmFactory(requireContext())
        return ViewModelProvider(this,vmFactory)[SearchNewsViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search_news
    }
}