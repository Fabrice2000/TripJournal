package com.example.tripjournal.ui.trips

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripjournal.R
import com.example.tripjournal.data.database.TripJournalDatabase
import com.example.tripjournal.data.repository.TripRepository
import com.example.tripjournal.ui.places.applySystemBarPadding
import kotlinx.coroutines.launch

class TripListActivity : AppCompatActivity() {

    private val viewModel: TripListViewModel by viewModels {
        TripListViewModel.Factory(TripRepository(TripJournalDatabase.get(this).tripDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_list)
        findViewById<ViewGroup>(android.R.id.content).getChildAt(0).applySystemBarPadding()

        val emptyText = findViewById<TextView>(R.id.tripEmptyText)
        val adapter = TripAdapter { trip ->
            startActivity(TripDetailActivity.newIntent(this, trip.id))
        }
        findViewById<RecyclerView>(R.id.tripList).apply {
            layoutManager = LinearLayoutManager(this@TripListActivity)
            this.adapter = adapter
        }

        // Étape 3 : le bouton + ouvrira le formulaire de voyage.

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.trips.collect { trips ->
                    adapter.submitList(trips)
                    emptyText.visibility = if (trips.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }
}