package com.jg.astroweather.presentation

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment: Fragment() {

    fun showErrorMessage(message: String){
        Snackbar.make(requireView().rootView, message, Snackbar.LENGTH_LONG).show()
    }
}