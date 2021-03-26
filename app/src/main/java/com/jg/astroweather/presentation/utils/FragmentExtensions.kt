package com.jg.astroweather.presentation.utils

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import com.jg.astroweather.R


/**
 * Navigates only if this is safely possible; when this Fragment is still the current destination.
 */
fun Fragment.navigateSafe(
        @IdRes resId: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = null,
        navigatorExtras: Navigator.Extras? = null
) {
    if (isAbleToNavigate()) findNavController().navigate(
            resId, args,
            navOptions, navigatorExtras
    )
}

/**
 * Navigates only if this is safely possible; when this Fragment is still the current destination.
 */
fun Fragment.navigateSafe(
        deepLink: Uri,
        navOptions: NavOptions? = null,
        navigatorExtras: Navigator.Extras? = null
) {
    if (isAbleToNavigate()) findNavController().navigate(deepLink, navOptions, navigatorExtras)
}

/**
 * Navigates only if this is safely possible; when this Fragment is still the current destination.
 */
fun Fragment.navigateSafe(directions: NavDirections, navOptions: NavOptions? = null) {
    if (isAbleToNavigate()) findNavController().navigate(directions, navOptions)
}

/**
 * Navigates only if this is safely possible; when this Fragment is still the current destination.
 */
fun Fragment.navigateSafe(
        directions: NavDirections,
        navigatorExtras: Navigator.Extras
) {
    if (isAbleToNavigate()) findNavController().navigate(directions, navigatorExtras)
}

/**
 * Returns true if the navigation controller is still pointing at 'this' fragment, or false if it already navigated away.
 */
fun Fragment.isAbleToNavigate(): Boolean {

    val navController = findNavController()
    val destinationIdInNavController = navController.currentDestination?.id

    // add tag_navigation_destination_id to your ids.xml so that it's unique:
    val destinationIdOfThisFragment = view?.getTag(R.id.fragment_container)
            ?: destinationIdInNavController

    // check that the navigation graph is still in 'this' fragment, if not then the app already navigated:
    return if (destinationIdInNavController == destinationIdOfThisFragment) {
        view?.setTag(R.id.fragment_container, destinationIdOfThisFragment)
        true
    } else {
        Log.d("FragmentExtensions", "Cannot navigate: current destination is not the current fragment.")
        false
    }
}