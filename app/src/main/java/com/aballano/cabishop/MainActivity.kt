package com.aballano.cabishop

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aballano.cabishop.checkout.presentation.screen.CheckoutFragment
import com.aballano.cabishop.productlist.presentation.screens.ProductListFragment

class MainActivity : AppCompatActivity() {

    private var checkoutMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            // For the shake of the size of the project is currently not a priority, but for bigger
            // projects it should be considered to have a navigation system. It'd abstract away any knowledge about
            // specific screens from other ones.
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProductListFragment.newInstance())
                .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        checkoutMenuItem = menu.findItem(R.id.menu_checkout).apply {
            isVisible = currentFragment() !is CheckoutFragment
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_checkout -> {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, 0)
                .add(R.id.container, CheckoutFragment.newInstance())
                .commitNow()
            checkoutMenuItem?.isVisible = false

            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        when (val currentFragment = currentFragment()) {
            is CheckoutFragment -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(0, R.anim.slide_out_right)
                    .remove(currentFragment)
                    .commitNow()
                checkoutMenuItem?.isVisible = true
            }

            else -> super.onBackPressed()
        }
    }

    private fun AppCompatActivity.currentFragment(): Fragment? =
        supportFragmentManager.findFragmentById(R.id.container)
}

