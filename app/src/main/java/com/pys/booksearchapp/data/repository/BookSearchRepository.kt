package com.pys.booksearchapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.pys.booksearchapp.data.model.Book
import com.pys.booksearchapp.data.model.SearchResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface BookSearchRepository {

    suspend fun searchBooks(
        query: String,
        sort : String,
        page : Int,
        size : Int
    ) : Response<SearchResponse>

    // Room
    suspend fun insertBooks(book: Book)

    suspend fun deleteBooks(book : Book)

    fun getFavoriteBooks() : Flow<List<Book>>

    // Data Store
    suspend fun saveSortMode(mode : String)

    suspend fun getSortMode() : Flow<String>

    // Paging
    fun getFavoritePagingBooks() : Flow<PagingData<Book>>
}