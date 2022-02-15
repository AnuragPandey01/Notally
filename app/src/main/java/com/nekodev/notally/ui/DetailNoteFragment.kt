package com.nekodev.notally.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nekodev.notally.R
import com.nekodev.notally.database.Notes
import com.nekodev.notally.databinding.FragmentDetailNoteBinding

class DetailNoteFragment : Fragment() {

    private lateinit var binding: FragmentDetailNoteBinding
    private lateinit var viewModel: NotesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = DataBindingUtil.inflate(layoutInflater,
            R.layout.fragment_detail_note,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        setOnClickListener()

    }

    private fun setOnClickListener() {
        binding.btnDone.setOnClickListener {
            createNote()
            findNavController().navigate(R.id.action_detailNoteFragment_to_notesFragment)
        }
        binding.btnBack.setOnClickListener { findNavController().navigate(R.id.action_detailNoteFragment_to_notesFragment) }
    }

    private fun createNote() {
        val title = binding.noteTitle.text.toString()
        val description = binding.noteDescription.text.toString()
        viewModel.insertNote(Notes(0,title,description,"May 20 2021"))
        Log.d("meow", "createNote: ${viewModel.getAllNotes().value} ")

    }


}