package com.example.newsapp.ui.breakingNews

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.ArticleListener
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.base.BaseFragment
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.utils.Resource

class BreakingNewsFragment : BaseFragment<FragmentBreakingNewsBinding, BreakingNewsViewModel>() {
    lateinit var adapter: NewsAdapter
    private val TAG = "BreakingNewsFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        adapter.differ.submitList(it.articles)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.e(TAG, "Error: $it")
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }

            }
        }
    }


    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): BreakingNewsViewModel {

        val vmFactory = BreakingNewsVMFactory(
            requireContext(),

            )
        return ViewModelProvider(this, vmFactory)[BreakingNewsViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_breaking_news
    }

    private fun initRecyclerView() {
        adapter = NewsAdapter(ArticleListener {
            Log.e(TAG,it.title)
        })
        viewDataBinding.rvBreakingNews.adapter = adapter
        viewDataBinding.rvBreakingNews.layoutManager = LinearLayoutManager(activity)

    }

    private fun hideProgressBar() {
        viewDataBinding.paginationProgressBar.visibility = GONE
    }

    private fun showProgressBar() {
        viewDataBinding.paginationProgressBar.visibility = VISIBLE
    }

}