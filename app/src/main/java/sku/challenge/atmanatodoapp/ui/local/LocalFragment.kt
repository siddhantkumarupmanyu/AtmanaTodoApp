package sku.challenge.atmanatodoapp.ui.local

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
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import sku.challenge.atmanatodoapp.R
import sku.challenge.atmanatodoapp.databinding.FragmentLocalBinding
import sku.challenge.atmanatodoapp.ui.ContainerFragmentDirections
import sku.challenge.atmanatodoapp.ui.ItemButtonListener
import sku.challenge.atmanatodoapp.ui.ListViewAdapter


@AndroidEntryPoint
class LocalFragment : Fragment(), ItemButtonListener {

    private var _binding: FragmentLocalBinding? = null

    private val binding: FragmentLocalBinding
        get() = _binding!!

    private val localViewModel by viewModels<LocalViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_local,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.addItem.setOnClickListener {
            findNavController().navigate(ContainerFragmentDirections.actionContainerToEditItem(-1))
        }

        binding.commonListView.listView.adapter = ListViewAdapter(this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                localViewModel.items.collect { result ->
                    val adapter = binding.commonListView.listView.adapter as ListViewAdapter
                    adapter.submitList(result)
                }
            }
        }
    }

    override fun edit(position: Int) {
        val adapter = binding.commonListView.listView.adapter as ListViewAdapter
        val id = adapter.currentList[position].id
        findNavController().navigate(ContainerFragmentDirections.actionContainerToEditItem(id))
    }

    override fun delete(position: Int) {
        val adapter = binding.commonListView.listView.adapter as ListViewAdapter
        val item = adapter.currentList[position]

        localViewModel.deleteItem(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val FRAGMENT_TAB_NAME = "Local"

        @JvmStatic
        fun newInstance() = LocalFragment()
    }

}