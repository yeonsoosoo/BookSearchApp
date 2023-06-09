package com.pys.booksearchapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.pys.booksearchapp.R
import com.pys.booksearchapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration : AppBarConfiguration

/*    private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)
    private val workManager = WorkManager.getInstance(application)*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //setupBottomNavigationView()
        setUpJetPackNavigation()

        //앱이 처음 실행되었을 경우에만 search 노출
        /*if(savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.fragment_search
        }*/

/*        val dataBase = BookSearchDatabase.getInstance(this)
        val bookSearchRepository = BookSearchRepositoryImpl(dataBase, dataStore)
        val factory = BookSearchViewModelProviderFactory(bookSearchRepository, workManager ,this)
        bookSearchViewModel = ViewModelProvider(this, factory)[BookSearchViewModel::class.java]*/
    }

    private fun setUpJetPackNavigation() {
        val host = supportFragmentManager
            .findFragmentById(R.id.booksearch_nav_host_fragment) as NavHostFragment ?: return
        navController = host.navController
        binding.bottomNavigationView.setupWithNavController(navController) // 네비게이션이 프래그먼트 전환을 해줌

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragment_search, R.id.fragment_favorite, R.id.fragment_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

   /* private fun setupBottomNavigationView() {
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
    }*/
}