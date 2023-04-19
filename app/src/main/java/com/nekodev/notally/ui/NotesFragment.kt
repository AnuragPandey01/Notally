package com.nekodev.notally.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nekodev.notally.R
import com.nekodev.notally.adapter.NotesAdapter
import com.nekodev.notally.database.Notes
import com.nekodev.notally.databinding.NotesFragmentBinding
import com.nekodev.notally.util.SwipeGuesture

class NotesFragment : Fragment() {

    private lateinit var viewModel: NotesViewModel
    private lateinit var binding : NotesFragmentBinding
    private lateinit var adapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NotesFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        setOnClickListener()
        viewModel.getAllNotes().observe(viewLifecycleOwner){ noteList ->
            setNotes(noteList)
        }
    }

    private fun setOnClickListener() {
        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_notesFragment_to_detailNoteFragment)
        }
    }

    private fun setNotes(noteList: List<Notes>){
        if(noteList.isEmpty()){
            binding.emptyListAnim.visibility = View.VISIBLE
            binding.tip.visibility = View.VISIBLE
            return
        }
        binding.emptyListAnim.visibility =View.GONE
        binding.tip.visibility = View.GONE
        initRecyclerView(noteList)
    }

    private fun initRecyclerView(noteList:List<Notes>){
        val layoutManager = StaggeredGridLayoutManager(2 , LinearLayoutManager.VERTICAL)
        adapter = NotesAdapter(noteList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager
        ItemTouchHelper(swipeGesture).attachToRecyclerView(binding.recyclerView)
    }

    private val swipeGesture = object : SwipeGuesture() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val note = adapter.onSwipe(viewHolder.adapterPosition)
            viewModel.deleteNote(note.id)
            showSnackBar(note)
        }

        private fun showSnackBar(note: Notes) {
            Snackbar.make(binding.recyclerView, note.title,Snackbar.LENGTH_LONG)
                .setAnchorView(binding.addNote)
                .setAction("UNDO") {
                    viewModel.insertNote(note)
                }
                .setBackgroundTint(resources.getColor(R.color.icon_background,null))
                .setActionTextColor(Color.WHITE)
                .show()
        }
    }

}