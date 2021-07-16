package com.example.newyorklist.ui.fragments.base

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * @author Dmitriy Larin
 */
abstract class BaseFragmentWithBinding<Bind : ViewBinding> : Fragment() {

    var _binding: Bind? = null
    protected val binding get() = _binding!!


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}