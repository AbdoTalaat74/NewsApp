package com.example.newsapp.ui.searchNews

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.ArticleListener
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.base.BaseFragment
import com.example.newsapp.databinding.FragmentSearchNewsBinding
import com.example.newsapp.db.ArticleDateBase
import com.example.newsapp.models.Article
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : BaseFragment<FragmentSearchNewsBinding, SearchNewsViewModel>(),
    Navigator {

    private lateinit var adapter: NewsAdapter
    private val TAG = "SearchNewsFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToLiveData()
        initRecyclerView()
        viewModel.navigator = this
        var job: Job? = null
        viewDataBinding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.searchNews(it.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }

                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        adapter.differ.submitList(it.articles)
                        val totalPages = it.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchNewsPage == totalPages

                        if (isLastPage) {
                            viewDataBinding.rvSearchNews.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.e(TAG, "Error: $it")
                    }
                }
            }
        }


    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SearchNewsViewModel {
        val vmFactory = SearchNewsVmFactory(
            NewsRepository(ArticleDateBase(requireContext())),
            requireContext().applicationContext as Application
        )
        return ViewModelProvider(this, vmFactory)[SearchNewsViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search_news
    }


    private fun initRecyclerView() {
        adapter = NewsAdapter(ArticleListener {
            viewModel.navigateToArticleFragment(it)
        })
        viewDataBinding.rvSearchNews.adapter = adapter
        viewDataBinding.rvSearchNews.layoutManager = LinearLayoutManager(activity)
        viewDataBinding.rvSearchNews.addOnScrollListener(this@SearchNewsFragment.scrollListener)

    }

    private fun hideProgressBar() {
        viewDataBinding.paginationProgressBar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        viewDataBinding.paginationProgressBar.visibility = View.VISIBLE
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
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.searchNews(viewDataBinding.etSearch.text.toString())
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
            SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(
                article
            )
        )
    }
}