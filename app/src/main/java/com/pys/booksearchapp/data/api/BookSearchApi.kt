package com.pys.booksearchapp.data.api

import com.pys.booksearchapp.data.model.SearchResponse
import com.pys.booksearchapp.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface BookSearchApi {

    @Headers("Authoroization : KakaAk $API_KEY")
    @GET("v3/search/book")
    suspend fun searchBooks(
        @Query("query") query: String,
        @Query("sort") sort : String,
        @Query("page") page : Int,
        @Query("size") size : Int
    ) : Response<SearchResponse>
}