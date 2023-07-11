package com.example.newsapp.ui.breakingNews

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.R
import com.example.newsapp.base.BaseFragment
import com.example.newsapp.databinding.FragmentBreakingNewsBinding

class BreakingNewsFragment:BaseFragment<FragmentBreakingNewsBinding,BreakingNewsViewModel>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): BreakingNewsViewModel {
        val vmFactory = BreakingNewsVMFactory(requireContext())
        return ViewModelProvider(this,vmFactory)[BreakingNewsViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_breaking_news
    }
}