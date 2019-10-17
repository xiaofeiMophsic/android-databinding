package com.example.android.databinding.basicsample.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.databinding.basicsample.databinding.ItemArticleBinding
import com.example.android.databinding.basicsample.model.Article
import com.example.android.databinding.basicsample.util.executeAfter

class ArticleAdapter(private val lifecycleOwner: LifecycleOwner) : ListAdapter<Article, ArticleViewHolder>(ArticleDiff) {

    private val mArticleList = ArrayList<Article>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding =  ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding, lifecycleOwner)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class ArticleViewHolder(
        private val binding: ItemArticleBinding,
        private val lifecycleOwner: LifecycleOwner
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article) {
        binding.executeAfter {
            binding.lifecycleOwner = this@ArticleViewHolder.lifecycleOwner
            binding.article = article
        }
    }
}

object ArticleDiff: DiffUtil.ItemCallback<Article>(){
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
            areContentsTheSame(oldItem, newItem) && oldItem.niceDate == newItem.niceDate

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem.title == newItem.title && oldItem.author == newItem.author

}