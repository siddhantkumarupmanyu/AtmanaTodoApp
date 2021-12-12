package sku.challenge.atmanatodoapp.ui.remote

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import sku.challenge.atmanatodoapp.R
import sku.challenge.atmanatodoapp.databinding.FragmentRemoteBinding
import sku.challenge.atmanatodoapp.ui.ItemButtonListener
import sku.challenge.atmanatodoapp.ui.ListViewAdapter
import sku.challenge.atmanatodoapp.vo.Item

@AndroidEntryPoint
class RemoteFragment : Fragment() {

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

        binding.commonListView.listView.adapter =
            ListViewAdapter(ItemButtonListener.NULL_ITEM_BUTTON_LISTENER)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                remoteViewModel.items.collect { result ->
                    when (result) {
                        is RemoteViewModel.FetchedPageResult.Loading -> showLoadingMoreProgressBar()
                        is RemoteViewModel.FetchedPageResult.NoMoreDataAvailable -> showNoDataSnackBar()
                        is RemoteViewModel.FetchedPageResult.Success -> setNewData(result.data)
                    }
                }
            }
        }

        remoteViewModel.fetchNextPage()

        addEndScrollListenerOnRecyclerView()
    }

    private fun addEndScrollListenerOnRecyclerView() {
        binding.commonListView.listView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // https://stackoverflow.com/a/36128493
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val pastVisibleItemCount = layoutManager.findFirstVisibleItemPosition()

                if (pastVisibleItemCount + visibleItemCount >= totalItemCount) {
                    remoteViewModel.fetchNextPage()
                }
            }
        })
    }

    private fun setNewData(data: List<Item>) {
        val adapter = binding.commonListView.listView.adapter as ListViewAdapter
        adapter.submitList(data)
        // hideProgressBar()
    }

    private fun hideProgressBar() {
        TODO("Not yet implemented")
    }

    private fun showNoDataSnackBar() {
        TODO("Not yet implemented")
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