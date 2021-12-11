package sku.challenge.atmanatodoapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import sku.challenge.atmanatodoapp.R
import sku.challenge.atmanatodoapp.databinding.FragmentEditItemBinding

@AndroidEntryPoint
class EditItemFragment : Fragment() {

    private var _binding: FragmentEditItemBinding? = null

    private val binding: FragmentEditItemBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_item,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}