package com.nekodev.notally.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nekodev.notally.R
import com.nekodev.notally.database.Notes
import com.nekodev.notally.databinding.FragmentEditNoteBinding
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.ContextCompat.getSystemService

class EditNoteFragment : Fragment() {

    private lateinit var binding: FragmentEditNoteBinding
    private lateinit var viewModel: NotesViewModel
    private lateinit var args: EditNoteFragmentArgs
    private var isEditMode:Boolean? = null
    private var isNewNote= true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = DataBindingUtil.inflate(layoutInflater,
            R.layout.fragment_edit_note,container,false)
        //get arguments
        args = EditNoteFragmentArgs.fromBundle(arguments!!)
        isEditMode = args.editMode
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        //check if not in edit mode than change layout for view mode
        if (isEditMode == false ){
            changeToViewMode()
            setTextField()
            isNewNote = false
        }
        if (isNewNote){
            showKeyboard()
        }
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnDone.setOnClickListener { onDone() }
        binding.btnBack.setOnClickListener { onBack() }
        binding.btnDelete.setOnClickListener { deleteNote() }
        binding.btnEdit.setOnClickListener { onEditClicked() }
    }

    private fun deleteNote() {
        if(isEditMode!!){
            Toast.makeText(requireContext(),"Can't delete in edit mode",Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.deleteNote(args.clickedNote!!)
        findNavController().navigate(R.id.action_detailNoteFragment_to_notesFragment)
    }

    private fun onDone(){
        if(binding.noteEditTitle.text?.isEmpty()!!){
            binding.noteEditTitle.error = "Title can't be empty"
            return
        }
        if(isNewNote){
            createNote()
            return
        }
        updateNote()
    }

    private fun updateNote() {
        Log.d("meow", "updateNote: yes ")
        val title = binding.noteEditTitle.text.toString()
        val description = binding.noteEditDescription.text.toString()
        viewModel.updateNote( Notes(args.noteId, title, description, getDate()) )
        onBack()
    }

    private fun createNote() {
        val title = binding.noteEditTitle.text.toString()
        val description = binding.noteEditDescription.text.toString()
        viewModel.insertNote(Notes(0,title,description,getDate()))
        onBack()
    }

    private fun onBack(){
        hideKeyboard()
        findNavController().navigate(R.id.action_detailNoteFragment_to_notesFragment)
    }

    private fun onEditClicked() {
        if(isEditMode == true){
            Toast.makeText(requireContext(),"Already in edit mode",Toast.LENGTH_SHORT).show()
            return
        }
        isEditMode = true
        setEditTextField()
        changeToEditMode()
        showKeyboard()
    }

    private fun changeToViewMode() {
        binding.apply {
            noteEditTitle.visibility = View.GONE
            noteTitle.visibility = View.VISIBLE
            noteEditDescription.visibility = View.GONE
            noteDescription.visibility = View.VISIBLE
            btnDone.visibility =View.GONE
        }
        binding.btnEdit.setImageResource(R.drawable.ic_edit_outline)
    }

    private fun changeToEditMode() {
        binding.apply {
            noteEditTitle.visibility = View.VISIBLE
            noteTitle.visibility = View.GONE
            noteEditDescription.visibility = View.VISIBLE
            noteDescription.visibility = View.GONE
            btnDone.visibility = View.VISIBLE
        }
        binding.btnEdit.setImageResource(R.drawable.ic_edit_filled)
    }

    private fun setEditTextField() {
        val noteToEdit = args.clickedNote
        binding.noteEditTitle.setText(noteToEdit?.title)
        binding.noteEditTitle.requestFocus()
        binding.noteEditDescription.setText(noteToEdit?.body)
        showKeyboard()
    }

    private fun setTextField() {
        val noteToEdit = args.clickedNote
        binding.noteTitle.text = noteToEdit?.title
        binding.noteDescription.text = noteToEdit?.body
    }

    private fun getDate(): String{
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("dd LLL yyyy")
        return simpleDateFormat.format(calendar.time).toString()
    }

    private fun showKeyboard() {
        binding.noteEditTitle.requestFocus()
        val inputManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(binding.noteEditTitle, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard(){
        binding.noteEditTitle.requestFocus()
        val inputManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(binding.noteEditTitle.windowToken,0)
    }
}