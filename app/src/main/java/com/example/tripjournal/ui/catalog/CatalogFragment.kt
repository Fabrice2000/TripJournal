
package com.example.tripjournal.ui.catalog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripjournal.R

class CatalogFragment : Fragment(R.layout.fragment_catalog) {

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView =
            view.findViewById<androidx.recyclerview.widget.RecyclerView>(
                R.id.tripsRecyclerView
            )

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
    }
}
