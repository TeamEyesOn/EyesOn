package com.d201.eyeson.view.angel

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.d201.domain.utils.ResultType
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentAngelMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "AngelMainFragment"
@AndroidEntryPoint
class AngelMainFragment : BaseFragment<FragmentAngelMainBinding>(R.layout.fragment_angel_main) {

    private val angelMainViewModel: AngelMainViewModel by viewModels()
    private lateinit var job: Job
    private lateinit var angelMainAdapter: AngelMainAdapter

    override fun init() {
        Log.d(TAG, "init: ####################################")
        initListener()
        initView()
        initViewModelCallback()
        angelMainViewModel.getAngelInfo()
    }

    private fun initListener() {
        binding.apply {
            btnSetting.setOnClickListener {
                findNavController().navigate(AngelMainFragmentDirections.actionAngelMainFragmentToAngelSettingFragment())
            }
            btnComplaintsList.setOnClickListener {
                findNavController().navigate(AngelMainFragmentDirections.actionAngelMainFragmentToComplaintsListFragment())
            }
        }
    }

    private fun initView() {
        angelMainAdapter = AngelMainAdapter()
        binding.apply {
            ryComplaints.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = angelMainAdapter
            }
        }
    }

    private fun initViewModelCallback(){
        job = lifecycleScope.launch { 
            angelMainViewModel.getCrewBoards(0).collectLatest {
                angelMainAdapter.submitData(it)
            }
        }
        lifecycleScope.launch{
            angelMainViewModel.apply {
                angelInfo.collectLatest {
                    Log.d(TAG, "initViewModel: $it")
                    binding.tvTest.text = it.toString()
                }
            }
        }
        

    }

}