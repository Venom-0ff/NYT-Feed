package com.skostyukov.nytfeed

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.skostyukov.nytfeed.model.HistoryItem
import com.skostyukov.nytfeed.model.Result
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class HistoryRecyclerAdapter(private val dataSet: List<HistoryItem>, private val context: Context) :
    RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewDateTime: TextView
        var textViewTitle: TextView
        var cardView: CardView

        init {
            textViewDateTime = view.findViewById(R.id.textViewDateHistory)
            textViewTitle = view.findViewById(R.id.textViewTitleHistory)
            cardView = view.findViewById(R.id.cardViewHistory)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view =
            LayoutInflater.from(viewGroup.context).inflate(
                R.layout.recycler_history_item,
                viewGroup, false
            )

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (dataSet.isNotEmpty()) {
            if (dataSet[position].date!!.isNotBlank()) {
                viewHolder.textViewDateTime.text = LocalDateTime.parse(
                    dataSet[position].date, DateTimeFormatter.ISO_LOCAL_DATE_TIME
                ).format(
                    DateTimeFormatter.ofLocalizedDateTime(
                        FormatStyle.SHORT,
                        FormatStyle.SHORT
                    )
                )
            }

            viewHolder.textViewTitle.text = dataSet[position].title

            if (dataSet[position].url!!.isNotBlank()) {
                viewHolder.cardView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataSet[position].url))
                    context.startActivity(intent)
                }
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return dataSet.size
    }
}