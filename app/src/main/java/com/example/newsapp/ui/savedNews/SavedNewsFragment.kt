package com.example.newsapp.ui.savedNews

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.ArticleListener
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.base.BaseFragment
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.db.ArticleDateBase
import com.example.newsapp.models.Article
import com.example.newsapp.repository.NewsRepository
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment:BaseFragment<FragmentSavedNewsBinding,SavedNewsViewModel>(),Navigator {
    private lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        viewModel.navigator = this
        viewModel.getSavedArticles().observe(viewLifecycleOwner){
            newsAdapter.differ.submitList(it)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view,"Article deleted Successfully",Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }

        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(viewDataBinding.rvSavedNews)

        }

    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): SavedNewsViewModel {
        val vmFactory = SavedNewsVmFactory(NewsRepository(ArticleDateBase(requireContext())))
        return ViewModelProvider(this,vmFactory)[SavedNewsViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_saved_news
    }

    private fun initRecyclerView(){
        newsAdapter = NewsAdapter(ArticleListener {
            viewModel.navigateToArticleFragment(it)
        })
        viewDataBinding.rvSavedNews.layoutManager = LinearLayoutManager(activity)
        viewDataBinding.rvSavedNews.adapter = newsAdapter
    }

    override fun onNavigateToArticleFragment(article: Article) {
        findNavController().navigate(SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article))
    }

}