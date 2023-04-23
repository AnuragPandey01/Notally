package com.nekodev.notally.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.nekodev.notally.util.hideKeyboard
import com.nekodev.notally.util.showKeyboard

class NotesFragment : Fragment() {

    private lateinit var viewModel: NotesViewModel
    private lateinit var binding : NotesFragmentBinding
    private lateinit var adapter: NotesAdapter
    private var mList: List<Notes> = emptyList()

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
            mList = noteList
        }
    }

    private fun setOnClickListener() {
        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_notesFragment_to_detailNoteFragment)
        }
        binding.btnSearch.setOnClickListener {
            binding.ilSearch.visibility = View.VISIBLE
            binding.btnSearch.visibility = View.GONE
            binding.notesHeading.visibility = View.GONE

            requireContext().showKeyboard(binding.etSearch)
        }

        binding.ilSearch.setStartIconOnClickListener{
            binding.ilSearch.visibility = View.GONE
            binding.btnSearch.visibility = View.VISIBLE
            binding.notesHeading.visibility = View.VISIBLE

            adapter.filterList(mList)
            requireContext().hideKeyboard(binding.etSearch)
        }

        binding.etSearch.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(mList.isNotEmpty()){
                    filter(s.toString())
                }
            }

        })
    }

    private fun filter(string: String) {
        val filteredList: ArrayList<Notes> = ArrayList()
        for(item in mList){
            if(item.title.lowercase().contains(string.lowercase()) || item.body.lowercase().contains(string.lowercase()) ){
                filteredList.add(item)
            }
        }
        adapter.filterList(filteredList)
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
            Snackbar.make(binding.root, "Note Deleted!",Snackbar.LENGTH_LONG)
                .setAnchorView(binding.addNote)
                .setAction("UNDO") {
                    viewModel.insertNote(note)
                }
                .setTextColor(resources.getColor(R.color.white,null))
                .setBackgroundTint(resources.getColor(R.color.icon_background,null))
                .setActionTextColor(Color.WHITE)
                .show()
        }
    }

}