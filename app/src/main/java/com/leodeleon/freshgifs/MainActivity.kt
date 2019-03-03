package com.leodeleon.freshgifs

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.leodeleon.freshgifs.explore.ExploreFragment
import com.leodeleon.freshgifs.favorites.FavoritesFragment
import com.leodeleon.freshgifs.utils.listen
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val fragments: List<Fragment> = listOf(ExploreFragment(),
        FavoritesFragment()
    )

    private val adapter = object : FragmentPagerAdapter(supportFragmentManager) {
        override fun getItem(position: Int) = fragments[position]

        override fun getCount() = fragments.size

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view_pager.adapter = adapter
        setupTabs()
    }

    private fun setupTabs(){
        tab_layout.apply {
            setupWithViewPager(view_pager)
            getTabAt(0)?.apply {
                setCustomView(R.layout.view_tab)
                icon = getDrawable(R.drawable.tab_explore)
            }
            getTabAt(1)?.apply {
                setCustomView(R.layout.view_tab)
                icon = getDrawable(R.drawable.tab_faves)
            }
            listen {
                setSelectedTabIndicatorColor(
                    when (it.position) {
                        0 -> ContextCompat.getColor(context, R.color.yellow)
                        1 -> ContextCompat.getColor(context, R.color.pink)
                        else -> Color.WHITE
                    }
                )
            }
        }
    }
}
