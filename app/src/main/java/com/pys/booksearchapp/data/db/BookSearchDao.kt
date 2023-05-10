package com.pys.booksearchapp.data.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.pys.booksearchapp.data.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookSearchDao {

    //동일한 데이터가 있을 시 덮어씀
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM books")
    fun getFavoriteBooks() : Flow<List<Book>>

    @Query("SELECT * FROM books")
    fun getFavoritePagingBooks() : PagingSource<Int, Book>
}