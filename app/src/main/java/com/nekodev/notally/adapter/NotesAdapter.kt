package com.nekodev.notally.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.nekodev.notally.R
import com.nekodev.notally.util.colorPicker
import com.nekodev.notally.database.Notes


class NotesAdapter(private val noteList: List<Notes>):RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private var i = 0
    private val colorList = listOf("#E5B9AF", "#C3C4E5", "#C3EFC0", "#F8FFC6")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notes_item_view,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNotes = noteList[position]
        holder.title.text = currentNotes.title
        holder.date.text = currentNotes.date.toString()
        holder.noteHolder.setCardBackgroundColor(Color.parseColor(colorPicker.getBackgroundColor()))
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.note_title)
        val date: TextView = itemView.findViewById(R.id.date)
        val noteHolder: CardView = itemView.findViewById(R.id.note_holder)

    }

}