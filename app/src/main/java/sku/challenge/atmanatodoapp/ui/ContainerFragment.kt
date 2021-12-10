package sku.challenge.atmanatodoapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import sku.challenge.atmanatodoapp.R
import sku.challenge.atmanatodoapp.databinding.FragmentContainerBinding


const val FRAGMENTS_COUNT = 2

@AndroidEntryPoint
class ContainerFragment : Fragment() {

    private var _binding: FragmentContainerBinding? = null

    private val binding: FragmentContainerBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_container,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.pager.adapter = ContainerViewPagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when (position) {
                0 -> RemoteFragment.FRAGMENT_TAB_NAME
                1 -> LocalFragment.FRAGMENT_TAB_NAME
                else -> "Unknown"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class ContainerViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount() = FRAGMENTS_COUNT

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    RemoteFragment.newInstance()
                }
                1 -> {
                    LocalFragment.newInstance()
                }
                else -> {
                    throw UnsupportedOperationException("This code should never be executed")
                }
            }
        }

    }
}