package com.example.newsapp.ui.savedNews

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.R
import com.example.newsapp.base.BaseFragment
import com.example.newsapp.databinding.FragmentSavedNewsBinding

class SavedNewsFragment:BaseFragment<FragmentSavedNewsBinding,SavedNewsViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SavedNewsViewModel {
        val vmFactory = SavedNewsVmFactory(requireContext())
        return ViewModelProvider(this,vmFactory)[SavedNewsViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_saved_news
    }
}