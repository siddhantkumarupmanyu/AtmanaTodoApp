package sku.challenge.atmanatodoapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import sku.challenge.atmanatodoapp.R
import sku.challenge.atmanatodoapp.databinding.FragmentRemoteBinding
import sku.challenge.atmanatodoapp.vo.Item

@AndroidEntryPoint
class RemoteFragment : Fragment() {


    // TODO: fix this Fragment

    private var _binding: FragmentRemoteBinding? = null

    private val binding: FragmentRemoteBinding
        get() = _binding!!

    private val remoteViewModel by viewModels<RemoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_remote,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        // binding.commonListView.listView.adapter = ListViewAdapter {
        //     // no op for remote data
        // }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                remoteViewModel.items.collect { result ->
                    when (result) {
                        is RemoteViewModel.FetchedPageResult.Success -> loadNewData(result.data)
                        is RemoteViewModel.FetchedPageResult.Loading -> showLoadingMoreProgressBar()
                    }
                }
            }
        }

        // remoteViewModel.fetchNextPage()
    }

    private fun loadNewData(data: List<Item>) {
        // val adapter = binding.commonListView.listView.adapter as ListViewAdapter
        // adapter.submitList(data)
    }

    private fun showLoadingMoreProgressBar() {
        // TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val FRAGMENT_TAB_NAME = "Remote"

        @JvmStatic
        fun newInstance() = RemoteFragment()
    }
}