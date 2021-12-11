package sku.challenge.atmanatodoapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import sku.challenge.atmanatodoapp.R
import sku.challenge.atmanatodoapp.databinding.FragmentLocalBinding


@AndroidEntryPoint
class LocalFragment : Fragment() {

    private var _binding: FragmentLocalBinding? = null

    private val binding: FragmentLocalBinding
        get() = _binding!!

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
            findNavController().navigate(LocalFragmentDirections.actionEditItem(-1))
        }
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