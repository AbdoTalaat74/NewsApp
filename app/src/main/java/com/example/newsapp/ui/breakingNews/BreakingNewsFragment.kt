package com.example.newsapp.ui.breakingNews

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.ArticleListener
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.base.BaseFragment
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.db.ArticleDateBase
import com.example.newsapp.models.Article
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsapp.utils.Resource

class BreakingNewsFragment : BaseFragment<FragmentBreakingNewsBinding, BreakingNewsViewModel>(),
    Navigator {
    lateinit var adapter: NewsAdapter
    private val TAG = "BreakingNewsFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        subscribeToLiveData()
        viewModel.navigator = this
        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        adapter.differ.submitList(it.articles)
                        val totalPages = it.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages

                        if (isLastPage) {
                            viewDataBinding.rvBreakingNews.setPadding(0, 0, 0, 0)
                        }
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
            NewsRepository(ArticleDateBase(requireContext())),
            requireContext().applicationContext as Application
        )
        return ViewModelProvider(this, vmFactory)[BreakingNewsViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_breaking_news
    }

    private fun initRecyclerView() {
        adapter = NewsAdapter(ArticleListener {
            viewModel.navigateToArticleFragment(it)
        })
        viewDataBinding.rvBreakingNews.adapter = adapter
        viewDataBinding.rvBreakingNews.layoutManager = LinearLayoutManager(activity)
        viewDataBinding.rvBreakingNews.addOnScrollListener(this@BreakingNewsFragment.scrollListener)

    }

    private fun hideProgressBar() {
        viewDataBinding.paginationProgressBar.visibility = GONE
        isLoading = false
    }

    private fun showProgressBar() {
        viewDataBinding.paginationProgressBar.visibility = VISIBLE
        isLoading = true
    }


    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getBreakingNews("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }


    override fun onNavigateToArticleFragment(article: Article) {
        findNavController().navigate(
            BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(
                article
            )
        )
    }


}