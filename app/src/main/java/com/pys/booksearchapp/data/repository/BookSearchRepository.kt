package com.pys.booksearchapp.data.repository

import com.pys.booksearchapp.data.model.SearchResponse
import retrofit2.Response


interface BookSearchRepository {

    suspend fun searchBooks(
        query: String,
        sort : String,
        page : Int,
        size : Int
    ) : Response<SearchResponse>
}