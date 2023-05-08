package com.pys.booksearchapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pys.booksearchapp.R
import com.pys.booksearchapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupBottomNavigationView()

        //앱이 처음 실행되었을 경우에만 search 노출
        if(savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.fragment_search
        }
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnClickListener {
            when(it.id) {
                R.id.fragment_search -> {
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