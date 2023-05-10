package com.pys.booksearchapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pys.booksearchapp.data.api.RetrofitInstance.api
import com.pys.booksearchapp.data.db.BookSearchDatabase
import com.pys.booksearchapp.data.model.Book
import com.pys.booksearchapp.data.model.SearchResponse
import com.pys.booksearchapp.data.repository.BookSearchRepositoryImpl.PreferencesKeys.SORT_MODE
import com.pys.booksearchapp.util.Constants.PAGING_SIZE
import com.pys.booksearchapp.util.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import retrofit2.Response
import java.io.IOException

class BookSearchRepositoryImpl(private val db : BookSearchDatabase, private val dataStore : DataStore<Preferences>) : BookSearchRepository{
    override suspend fun searchBooks(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): Response<SearchResponse> {
        return api.searchBooks(query, sort, page, size)
    }

    override suspend fun insertBooks(book: Book) {
        db.bookSearchDao().insertBook(book)
    }

    override suspend fun deleteBooks(book: Book) {
        db.bookSearchDao().deleteBook(book)
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return db.bookSearchDao().getFavoriteBooks()
    }

    // DataStore
    private object PreferencesKeys {
        val SORT_MODE = stringPreferencesKey("sort_mode") //저장할 타입이 String 이므로
    }

    override suspend fun saveSortMode(mode: String) {
        dataStore.edit { prefs ->
            prefs[SORT_MODE] = mode
        }
    }

    override suspend fun getSortMode(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if(exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { prefs ->
                prefs[SORT_MODE] ?: Sort.ACCURACY.value
            }
    }

    override fun getFavoritePagingBooks(): Flow<PagingData<Book>> {
        val pagingSourceFactory = { db.bookSearchDao().getFavoritePagingBooks() }

        return Pager(
            config = PagingConfig(
                pageSize = PAGING_SIZE,
                enablePlaceholders = false,
                maxSize = PAGING_SIZE * 3
            ),
            pagingSourceFactory = pagingSourceFactory //팩토리를 통해 전달
        ).flow //.flow로 flow화 시키기
    }
}