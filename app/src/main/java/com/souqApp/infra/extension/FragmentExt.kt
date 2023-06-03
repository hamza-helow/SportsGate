package com.souqApp.infra.extension

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle

fun Fragment.setupMenu(
    menuId: Int?,
    onPrepareMenu: (menu: Menu) -> Unit = {},
    onSelectedItem: (menuItem: MenuItem) -> Unit = {},
) {

    (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            if (menuId != null) {
                menuInflater.inflate(menuId, menu)
            } else menu.clear()
        }

        override fun onPrepareMenu(menu: Menu) {
            super.onPrepareMenu(menu)
            onPrepareMenu(menu)
        }


        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            onSelectedItem(menuItem)
            return false
        }

    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
}