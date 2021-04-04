package com.example.pythonrun

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(
    var list: List<Page>,
    private val clickListener: (Page) -> Unit
) :
    RecyclerView.Adapter<SearchAdapter.ArticleHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ArticleHolder(
            inflater.inflate(
                R.layout.item_page,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        holder.bind(list[position],clickListener)
    }

    fun submitList(list: List<Page>) {
        val duffResult = DiffUtil.calculateDiff(ArticleDiffUtilCallback(this.list, list))
        duffResult.dispatchUpdatesTo(this)
        this.list = list
    }

    class ArticleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Page, clickListener: (Page) -> Unit) {
            itemView.findViewById<TextView>(R.id.vTitle).text = item.title
            itemView.setOnClickListener {
                clickListener(item)
            }
        }
    }

    class ArticleDiffUtilCallback(
        private val oldList: List<Page>,
        private val newList: List<Page>
    ) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }
}
