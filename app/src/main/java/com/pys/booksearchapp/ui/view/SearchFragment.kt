package com.pys.booksearchapp.ui.view

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pys.booksearchapp.databinding.FragmentSearchBinding
import com.pys.booksearchapp.ui.adapter.BookSearchLoadStateAdapter
import com.pys.booksearchapp.ui.adapter.BookSearchPagingAdapter
import com.pys.booksearchapp.ui.viewModel.SearchViewModel
import com.pys.booksearchapp.util.Constants.SEARCH_BOOKS_TIME_DELAY
import com.pys.booksearchapp.util.collectLatestStateFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding get() = _binding!!

    //private lateinit var bookSearchViewModel: BookSearchViewModel
    //private val bookSearchViewModel by activityViewModels<BookSearchViewModel>()
    private val searchViewModel by viewModels<SearchViewModel>()

    //private lateinit var bookSearchAdapter: BookSearchAdapter
    private lateinit var bookSearchAdapter: BookSearchPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //bookSearchViewModel = (activity as MainActivity).bookSearchViewModel

        setUpRecyclerView()
        searchBooks()
        setUpLoadState()

/*
        bookSearchViewModel.searchResult.observe(viewLifecycleOwner) { response ->
            val books = response.documents
            bookSearchAdapter.submitList(books)
        }
*/
        collectLatestStateFlow(searchViewModel.searchPagingResult) {
            bookSearchAdapter.submitData(it)
        }
    }

    private fun setUpRecyclerView() {
        //bookSearchAdapter = BookSearchAdapter()
        bookSearchAdapter = BookSearchPagingAdapter()
        binding.rvSearchResult.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            //adapter = bookSearchAdapter

            adapter = bookSearchAdapter.withLoadStateFooter(
                footer = BookSearchLoadStateAdapter(bookSearchAdapter::retry)
            )
        }
        bookSearchAdapter.setOnItemClickListener {
            val action = SearchFragmentDirections.actionFragmentSearchToFragmentBook(it)
            findNavController().navigate(action)
        }
    }

    private fun searchBooks() {
        var startTime = System.currentTimeMillis()
        var endTime: Long

        binding.etSearch.text =
            Editable.Factory.getInstance().newEditable(searchViewModel.query)

        // 텍스트가 입력되면 값을 viewModel에 전달(사람 입력시간을 고려하여 delay를 줌)
        binding.etSearch.addTextChangedListener { text: Editable? ->
            endTime = System.currentTimeMillis()
            if (endTime - startTime >= SEARCH_BOOKS_TIME_DELAY) {
                text.let {
                    val query = it.toString().trim()
                    if (query.isNotEmpty()) {
                        //bookSearchViewModel.searchBooks(query)
                        searchViewModel.searchBooksPaging(query)
                        searchViewModel.query = query
                    }
                }
            }
            startTime = endTime
        }
    }

    private fun setUpLoadState() {
        bookSearchAdapter.addLoadStateListener { combinedLoadStates ->
            val loadState = combinedLoadStates.source
            val isListEmpty = bookSearchAdapter.itemCount < 1
                    && loadState.refresh is LoadState.NotLoading
                    && loadState.append.endOfPaginationReached

            binding.tvEmptylist.isVisible = isListEmpty
            binding.rvSearchResult.isVisible = !isListEmpty

            binding.progressBar.isVisible = loadState.refresh is LoadState.Loading

            // 재시도, 로딩전, 로딩 후 에러 발생 시 InVisible
            /*binding.btnRetry.isVisible = loadState.refresh is LoadState.Error
                    || loadState.append is LoadState.Error || loadState.prepend is LoadState.Error
            val errorState: LoadState.Error? = loadState.append as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_SHORT).show()
            }*/
        }

        //binding.btnRetry.setOnClickListener {
        //    bookSearchAdapter.retry()
        //}
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}