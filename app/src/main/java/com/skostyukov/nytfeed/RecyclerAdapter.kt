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
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.skostyukov.nytfeed.model.Result
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class RecyclerAdapter(private val dataSet: List<Result>, private val context: Context) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private val db = Firebase.firestore
//    private val user = Firebase.auth.currentUser

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewDateTime: TextView
        var textViewTitle: TextView
        var textViewBy: TextView
        var textViewImgCopyright: TextView
        var imageView: ImageView
        var cardView: CardView

        init {
            textViewDateTime = view.findViewById(R.id.textViewDate)
            textViewTitle = view.findViewById(R.id.textViewTitle)
            textViewBy = view.findViewById(R.id.textViewBy)
            textViewImgCopyright = view.findViewById(R.id.textViewImgCopyright)
            imageView = view.findViewById(R.id.imageView)
            cardView = view.findViewById(R.id.cardView)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.recycler_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textViewDateTime.text = LocalDateTime.parse(
            dataSet[position].first_published_date, DateTimeFormatter.ISO_OFFSET_DATE_TIME
        ).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT))

        viewHolder.textViewBy.text = dataSet[position].byline

        viewHolder.textViewTitle.text =
            dataSet[position].title.ifBlank { dataSet[position].abstract }

        viewHolder.textViewImgCopyright.text =
            "Image: © " + dataSet[position].multimedia[2].copyright
        Picasso.get().load(dataSet[position].multimedia[2].url).into(viewHolder.imageView)

        viewHolder.cardView.setOnClickListener {
            val entry = hashMapOf(
                "url" to dataSet[position].url,
                "title" to viewHolder.textViewTitle.text,
                "date" to LocalDateTime.now().toString()
            )
            db.collection("users").document(MainActivity.user.uid).collection("history").add(entry)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataSet[position].url))
            context.startActivity(intent)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return dataSet.size
    }
}