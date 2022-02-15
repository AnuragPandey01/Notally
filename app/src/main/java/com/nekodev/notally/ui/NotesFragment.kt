package com.nekodev.notally.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.nekodev.notally.R
import com.nekodev.notally.adapter.NotesAdapter
import com.nekodev.notally.database.Notes
import com.nekodev.notally.databinding.NotesFragmentBinding


class NotesFragment : Fragment() {

    private lateinit var viewModel: NotesViewModel
    private lateinit var binding : NotesFragmentBinding
    private lateinit var adapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.notes_fragment,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        setOnClickListener()
        viewModel.getAllNotes().observe(viewLifecycleOwner){ noteList ->
            Log.d("meow", "onViewCreated2: $noteList, ${viewModel.noteList}")
            initRecyclerView(noteList)
        }
    }

    private fun setOnClickListener() {
        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_notesFragment_to_detailNoteFragment)
        }
    }

    private fun initRecyclerView(noteList:List<Notes>){
        Log.d("meow", "initRecyclerView: yess")
        val layoutManager = StaggeredGridLayoutManager(2 , LinearLayoutManager.VERTICAL)
        adapter = NotesAdapter(noteList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager
    }

}