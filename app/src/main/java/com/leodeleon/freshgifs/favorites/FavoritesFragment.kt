package com.leodeleon.freshgifs.favorites


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.leodeleon.freshgifs.R
import com.leodeleon.freshgifs.databinding.FragmentFavoritesBinding
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.koin.android.viewmodel.ext.android.viewModel


class FavoritesFragment : Fragment() {
    private val viewModel: FavoritesViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser){
            viewModel.loadData()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.setHasFixedSize(true)
        (recycler_view.layoutManager as? StaggeredGridLayoutManager)?.let {
            it.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        }
    }

}
