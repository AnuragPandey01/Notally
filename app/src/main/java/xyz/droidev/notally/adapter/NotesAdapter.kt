package xyz.droidev.notally.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import xyz.droidev.notally.R
import xyz.droidev.notally.data.local.database.Notes
import xyz.droidev.notally.ui.NotesFragmentDirections
import xyz.droidev.notally.util.ColorPicker

class NotesAdapter(private var noteList: List<Notes>):RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notes_item_view,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNotes = noteList[position]
        holder.title.text = currentNotes.title
        holder.content.text = currentNotes.body
        holder.date.text = currentNotes.date
        holder.noteHolder.setCardBackgroundColor(Color.parseColor(ColorPicker.getBackgroundColor()))
        holder.noteHolder.setOnClickListener {
            val action = NotesFragmentDirections.actionNotesFragmentToDetailNoteFragment(currentNotes.id)
            it.findNavController().navigate( action )
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.note_title)
        val content: TextView = itemView.findViewById(R.id.note_content)
        val date: TextView = itemView.findViewById(R.id.date)
        val noteHolder: CardView = itemView.findViewById(R.id.note_holder)
    }

    fun onSwipe(position: Int): Notes {
        return noteList[position]
    }
    fun filterList(filteredList: List<Notes>) {
        noteList = filteredList
        notifyDataSetChanged()
    }
}