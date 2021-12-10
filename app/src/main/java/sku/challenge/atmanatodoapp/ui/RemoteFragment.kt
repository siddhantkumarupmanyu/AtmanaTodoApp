package sku.challenge.atmanatodoapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import sku.challenge.atmanatodoapp.R

class RemoteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_remote, container, false)
    }

    companion object {

        const val FRAGMENT_TAB_NAME = "Remote"

        @JvmStatic
        fun newInstance() = RemoteFragment()
    }
}