package com.example.newsapp.ui.searchNews

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.ArticleListener
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.base.BaseFragment
import com.example.newsapp.databinding.FragmentSearchNewsBinding
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment:BaseFragment<FragmentSearchNewsBinding,SearchNewsViewModel>() {

    private lateinit var adapter:NewsAdapter
    private val TAG = "SearchNewsFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        var job:Job? = null
        viewDataBinding.etSearch.addTextChangedListener {editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
            editable?.let {
                if (it.toString().isNotEmpty()){
                    viewModel.searchNews(it.toString())
                }
            }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner){response ->
            when(response){
                is Resource.Loading ->{
                    showProgressBar()
                }
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let {
                        adapter.differ.submitList(it.articles)
                        it.articles.forEach { articles ->
                            Log.e(TAG,articles.title)
                        }
                    }
                }
                is Resource.Error->{
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
        val vmFactory = SearchNewsVmFactory(requireContext())
        return ViewModelProvider(this,vmFactory)[SearchNewsViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search_news
    }



    private fun initRecyclerView() {
        adapter = NewsAdapter(ArticleListener {
            Log.e(TAG,it.title)
        })
        viewDataBinding.rvSearchNews.adapter = adapter
        viewDataBinding.rvSearchNews.layoutManager = LinearLayoutManager(activity)

    }

    private fun hideProgressBar() {
        viewDataBinding.paginationProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        viewDataBinding.paginationProgressBar.visibility = View.VISIBLE
    }
}