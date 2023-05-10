package com.pys.booksearchapp.ui.viewModel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pys.booksearchapp.data.model.Book
import com.pys.booksearchapp.data.model.SearchResponse
import com.pys.booksearchapp.data.repository.BookSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookSearchViewModel(private val bookSearchRepository: BookSearchRepository, private val savedStateHandle: SavedStateHandle) : ViewModel() {

    // API
    private val _searchResult = MutableLiveData<SearchResponse>()
    val searchResult : LiveData<SearchResponse> get() = _searchResult

    fun searchBooks(query: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = bookSearchRepository.searchBooks(query, getSortMode(), 1, 15)
        if(response.isSuccessful) {
            response.body()?.let { body ->
                _searchResult.postValue(body)
            }
        }
    }

    // Room
    fun saveBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        bookSearchRepository.insertBooks(book)
    }

    fun deleteBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        bookSearchRepository.deleteBooks(book)
    }

    //val favoriteBooks : Flow<List<Book>> = bookSearchRepository.getFavoriteBooks()
    val favoriteBooks : StateFlow<List<Book>> = bookSearchRepository.getFavoriteBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())

    // SavedState
    var query = String()
        set(value) {
            field = value
            savedStateHandle.set(SAVE_STATE_KEY, value)
        }

    init {
        query = savedStateHandle.get<String>(SAVE_STATE_KEY) ?: ""
    }

    companion object {
        private const val SAVE_STATE_KEY = "query"
    }

    // DataStore
    fun saveSortMode(value: String) = viewModelScope.launch(Dispatchers.IO) {
        bookSearchRepository.saveSortMode(value)
    }

    // withContext : 값을 무조건 반환하고 종료
    suspend fun getSortMode() = withContext(Dispatchers.IO)  {
        bookSearchRepository.getSortMode().first()
    }

    // Paging, 코루틴이 데이터 스트림을 캐시하고 공유 가능하게 만들어줌
    val favoritePagingBooks : StateFlow<PagingData<Book>> =
        bookSearchRepository.getFavoritePagingBooks()
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())
}