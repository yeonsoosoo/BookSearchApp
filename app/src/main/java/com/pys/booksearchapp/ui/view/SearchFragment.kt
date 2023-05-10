package com.pys.booksearchapp.ui.view

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pys.booksearchapp.databinding.FragmentSearchBinding
import com.pys.booksearchapp.ui.adapter.BookSearchAdapter
import com.pys.booksearchapp.ui.viewModel.BookSearchViewModel
import com.pys.booksearchapp.util.Constants.SEARCH_BOOKS_TIME_DELAY

class SearchFragment : Fragment() {
    private var _binding : FragmentSearchBinding? = null
    private val binding : FragmentSearchBinding get() = _binding!!

    private lateinit var bookSearchViewModel: BookSearchViewModel
    private lateinit var bookSearchAdapter: BookSearchAdapter

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
        bookSearchViewModel = (activity as MainActivity).bookSearchViewModel

        setUpRecyclerView()
        searchBooks()

        bookSearchViewModel.searchResult.observe(viewLifecycleOwner) { response ->
            val books = response.documents
            bookSearchAdapter.submitList(books)
        }
    }

    private fun setUpRecyclerView() {
        bookSearchAdapter = BookSearchAdapter()
        binding.rvSearchResult.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = bookSearchAdapter
        }
        bookSearchAdapter.setOnItemClickListener {
            val action = SearchFragmentDirections.actionFragmentSearchToFragmentBook(it)
            findNavController().navigate(action)
        }
    }

    private fun searchBooks() {
        var startTime = System.currentTimeMillis()
        var endTime : Long

        binding.etSearch.text = Editable.Factory.getInstance().newEditable(bookSearchViewModel.query)

        // 텍스트가 입력되면 값을 viewModel에 전달(사람 입력시간을 고려하여 delay를 줌)
        binding.etSearch.addTextChangedListener { text : Editable? ->
            endTime = System.currentTimeMillis()
            if(endTime - startTime >= SEARCH_BOOKS_TIME_DELAY) {
                text.let {
                    val query = it.toString().trim()
                    if(query.isNotEmpty()) {
                        bookSearchViewModel.searchBooks(query)
                        bookSearchViewModel.query = query
                    }
                }
            }
            startTime = endTime
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}