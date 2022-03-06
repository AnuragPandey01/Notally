package com.nekodev.notally.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.nekodev.notally.R
import com.nekodev.notally.database.Notes
import com.nekodev.notally.ui.NotesFragmentDirections
import com.nekodev.notally.util.ColorPicker

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
        holder.noteHolder.setOnClickListener {
            holder.title.transitionName = "title_anim"
            val extra = FragmentNavigatorExtras(holder.title to "title_anims")
            val action = NotesFragmentDirections.actionNotesFragmentToDetailNoteFragment(noteList[position])
            it.findNavController().navigate( action, extra )
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.note_title)
        val date: TextView = itemView.findViewById(R.id.date)
        val noteHolder: CardView = itemView.findViewById(R.id.note_holder)
    }

    fun onSwipe(position: Int): Notes{
        return noteList[position]
    }
}