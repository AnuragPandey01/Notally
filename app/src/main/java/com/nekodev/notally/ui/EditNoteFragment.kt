package com.nekodev.notally.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nekodev.notally.R
import com.nekodev.notally.database.Notes
import com.nekodev.notally.databinding.FragmentEditNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class EditNoteFragment : Fragment() {

    private lateinit var binding: FragmentEditNoteBinding
    private lateinit var args: EditNoteFragmentArgs
    private var isNewNote = true

    private val viewModel : NotesViewModel by lazy {
        ViewModelProvider(this).get(NotesViewModel::class.java)
    }
    private val inputManager : InputMethodManager by lazy {
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_edit_note,container,false)

        binding.notesViewModel = viewModel
        binding.lifecycleOwner = this
        //get arguments arguments(clickedNote:Notes?, editMode = true)
        args = EditNoteFragmentArgs.fromBundle(requireArguments())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()

        //check if not in edit mode than change layout for view mode
        if (args.clickedNote != null ){
            binding.clickedNote = args.clickedNote
            viewModel.notifyModeChanged()
            isNewNote = false
        }
        viewModel.isEditMode.observe(viewLifecycleOwner){ isEditMode ->
            if(isEditMode) showKeyboard()
        }
    }

    private fun setOnClickListener() {
        binding.btnDone.setOnClickListener { onDone() }
        binding.btnBack.setOnClickListener { onBack() }
        binding.btnDelete.setOnClickListener { deleteNote() }
        binding.btnEdit.setOnClickListener { onEditClicked() }
    }

    private fun deleteNote() {
        hideKeyboard()
        setUpBottomSheet()
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
        viewModel.updateNote( Notes(args.clickedNote!!.id, title, description, getDate()) )
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
        viewModel.notifyModeChanged()
    }

    private fun getDate(): String{
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("dd LLL yyyy")
        return simpleDateFormat.format(calendar.time).toString()
    }

    private fun showKeyboard() {
        binding.noteEditTitle.requestFocus()
        inputManager.showSoftInput(binding.noteEditTitle, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard(){
        binding.noteEditTitle.requestFocus()
        inputManager.hideSoftInputFromWindow(binding.noteEditTitle.windowToken,0)
    }

    private fun setUpBottomSheet() {
        val bottomSheet = BottomSheetDialog(requireContext())
        bottomSheet.setContentView(R.layout.delete_bottomsheet)
        val btnBottomSheetYes = bottomSheet.findViewById<Button>(R.id.yes)
        val btnBottomSheetNo = bottomSheet.findViewById<Button>(R.id.no)
        bottomSheet.show()
        btnBottomSheetYes?.setOnClickListener {
            viewModel.deleteNote(args.clickedNote!!)
            bottomSheet.dismiss()
            findNavController().navigate(R.id.action_detailNoteFragment_to_notesFragment)
        }
        btnBottomSheetNo?.setOnClickListener {
            bottomSheet.dismiss()
        }
    }
}