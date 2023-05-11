package com.pys.booksearchapp.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.pys.booksearchapp.R
import com.pys.booksearchapp.databinding.FragmentSettingsBinding
import com.pys.booksearchapp.ui.viewModel.SettingViewModel
import com.pys.booksearchapp.util.Sort
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding : FragmentSettingsBinding? = null
    private val binding : FragmentSettingsBinding get() = _binding!!

    //private lateinit var bookSearchViewModel: BookSearchViewModel
    //private val bookSearchViewModel by activityViewModels<BookSearchViewModel>()
    private val settingViewModel by viewModels<SettingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //bookSearchViewModel = (activity as MainActivity).bookSearchViewModel

        saveSettings()
        loadSettings()
        showWorkStatus()
    }

    private fun saveSettings() {
        binding.rgSort.setOnCheckedChangeListener() {_, checkedId ->
            val value = when(checkedId) {
                R.id.rb_accuracy -> Sort.ACCURACY.value
                R.id.rb_latest -> Sort.LATEST.value
                else -> return@setOnCheckedChangeListener
            }
            settingViewModel.saveSortMode(value) // 저장
        }

        binding.swCacheDelete.setOnCheckedChangeListener {_, isChecked ->
            settingViewModel.saveCacheDeleteMode(isChecked)
            if(isChecked) {
                settingViewModel.setWork()
            } else {
                settingViewModel.deleteWork()
            }
        }
    }

    private fun loadSettings() {
        lifecycleScope.launch {
            val buttonId = when(settingViewModel.getSortMode()) {
                Sort.ACCURACY.value -> R.id.rb_accuracy
                Sort.LATEST.value -> R.id.rb_latest
                else -> return@launch
            }
            binding.rgSort.check(buttonId)
        }

        // WorkManager
        lifecycleScope.launch {
            val mode = settingViewModel.getCacheDeleteMode()
            binding.swCacheDelete.isChecked = mode
        }
    }

    // LiveData로 반환 받은 작업 상태 표시
    private fun showWorkStatus() {
        settingViewModel.getWorkStatus().observe(viewLifecycleOwner) { workInfo ->
            Log.d("WorkManager", workInfo.toString())
            if(workInfo.isEmpty()) {
                binding.tvWorkStatus.text = "No works"
            } else {
                binding.tvWorkStatus.text = workInfo[0].state.toString()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}