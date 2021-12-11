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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import sku.challenge.atmanatodoapp.R
import sku.challenge.atmanatodoapp.databinding.FragmentEditItemBinding
import sku.challenge.atmanatodoapp.vo.Item

@AndroidEntryPoint
class EditItemFragment : Fragment() {

    private var _binding: FragmentEditItemBinding? = null

    private val binding: FragmentEditItemBinding
        get() = _binding!!

    private val editItemViewModel by viewModels<EditItemViewModel>()

    private val args by navArgs<EditItemFragmentArgs>()

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

        if (isEditCase()) {
            editItemViewModel.loadItem(args.id)
        }

        binding.save.setOnClickListener {
            editItemViewModel.saveItem(
                email = binding.emailEditText.text.toString(),
                firstName = binding.firstNameEditText.text.toString(),
                lastName = binding.lastNameEditText.text.toString()
            )
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                editItemViewModel.event.collect { event ->
                    when (event) {
                        is EditItemViewModel.Event.ItemEvent -> loadItems(event.item)
                        is EditItemViewModel.Event.SaveEvent -> findNavController().navigateUp()
                        else -> {} // no op
                    }
                }
            }
        }
    }

    private fun loadItems(item: Item) {
        // could use dataBinding but too lazy right now
        // also I am not sure how it will work with input
        binding.firstNameEditText.setText(item.firstName)
        binding.lastNameEditText.setText(item.lastName)
        binding.emailEditText.setText(item.email)
    }

    private fun isEditCase() = args.id != -1

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}