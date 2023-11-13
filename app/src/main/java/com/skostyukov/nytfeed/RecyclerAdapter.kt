package com.skostyukov.nytfeed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skostyukov.nytfeed.model.Result

class RecyclerAdapter(private val dataSet: List<Result>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewDateTime: TextView
        var textViewTitle: TextView
        var textViewBy: TextView
        init {
            textViewDateTime = view.findViewById(R.id.textViewDate)
            textViewTitle = view.findViewById(R.id.textViewTitle)
            textViewBy = view.findViewById(R.id.textViewBy)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_item, viewGroup, false)

//        val lp = view.layoutParams
//        lp.height = 256
//        view.layoutParams = lp

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textViewDateTime.text = dataSet[position].first_published_date
        viewHolder.textViewTitle.text = dataSet[position].title
        viewHolder.textViewBy.text = dataSet[position].byline
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount():Int {
        return dataSet.size
    }
}