package com.pys.booksearchapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.pys.booksearchapp.R
import com.pys.booksearchapp.data.repository.BookSearchRepositoryImpl
import com.pys.booksearchapp.databinding.ActivityMainBinding
import com.pys.booksearchapp.ui.viewModel.BookSearchViewModel
import com.pys.booksearchapp.ui.viewModel.BookSearchViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var bookSearchViewModel: BookSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupBottomNavigationView()

        //앱이 처음 실행되었을 경우에만 search 노출
        if(savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.fragment_search
        }
        val bookSearchRepository = BookSearchRepositoryImpl()
        val factory = BookSearchViewModelProviderFactory(bookSearchRepository)
        bookSearchViewModel = ViewModelProvider(this, factory)[BookSearchViewModel::class.java]
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item->
            when(item.itemId) {
                R.id.fragment_search -> {
                    Log.d("search", "   pys")
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout,SearchFragment())
                        .commit()
                    true
                }
                R.id.fragment_favorite -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout,FavoriteFragment())
                        .commit()
                    true
                }
                R.id.fragment_settings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout,SettingsFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}