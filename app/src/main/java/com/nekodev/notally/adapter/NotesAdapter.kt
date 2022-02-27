package com.nekodev.notally.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nekodev.notally.R
import com.nekodev.notally.database.Notes
import com.nekodev.notally.ui.EditNoteFragment
import com.nekodev.notally.ui.EditNoteFragmentDirections
import com.nekodev.notally.ui.NotesFragmentDirections
import com.nekodev.notally.ui.NotesViewModel
import com.nekodev.notally.util.ColorPicker
import com.nekodev.notally.util.SwipeGuesture

class NotesAdapter(private val noteList: List<Notes>):RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notes_item_view,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNotes = noteList[position]
        holder.title.text = currentNotes.title
        holder.date.text = currentNotes.date
        holder.noteHolder.setCardBackgroundColor(Color.parseColor(ColorPicker.getBackgroundColor()))
        holder.root.setOnClickListener {
            val action = NotesFragmentDirections.actionNotesFragmentToDetailNoteFragment(noteList[position],false)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.note_title)
        val date: TextView = itemView.findViewById(R.id.date)
        val noteHolder: CardView = itemView.findViewById(R.id.note_holder)
        val root: View = itemView.rootView
    }

    fun onSwipe(position: Int): Notes{
        return noteList[position]
    }
}