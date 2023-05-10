package com.pys.booksearchapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.pys.booksearchapp.data.model.Book
import com.pys.booksearchapp.databinding.ItemBookPreviewBinding
import com.pys.booksearchapp.ui.viewModel.BookSearchViewModel

class BookSearchPagingAdapter : PagingDataAdapter<Book, BookSearchViewHolder>(BookDiffCallback) {

    override fun onBindViewHolder(holder: BookSearchViewHolder, position: Int) {
        val pageBook = getItem(position)
        pageBook?.let { book ->
            holder.bind(book)
            holder.itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(book)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookSearchViewHolder {
        return BookSearchViewHolder(
            ItemBookPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    private var onItemClickListener: ((Book) -> Unit)? = null
    fun setOnItemClickListener(listener : (Book) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val BookDiffCallback = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.isbn == newItem.isbn
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }
        }
    }
}