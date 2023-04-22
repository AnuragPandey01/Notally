package com.nekodev.notally.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nekodev.notally.R
import com.nekodev.notally.database.Notes
import com.nekodev.notally.databinding.FragmentEditNoteBinding
import com.nekodev.notally.util.hideKeyboard
import com.nekodev.notally.util.showKeyboard
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class EditNoteFragment : Fragment() {

    private lateinit var binding: FragmentEditNoteBinding
    private var noteId  = -1
    private var isNewNote = false

    private val viewModel : NotesViewModel by lazy {
        ViewModelProvider(this).get(NotesViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentEditNoteBinding.inflate(layoutInflater)

        //get arguments arguments(clickedNote:Notes?, editMode = true)
        noteId = EditNoteFragmentArgs.fromBundle(requireArguments()).clickedNote

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()

        if(noteId == -1){
            isNewNote = true
            viewModel.setIsEditMode(true)
        }else{
            setNoteContent()
            viewModel.setIsEditMode(false)
        }

        viewModel.isEditMode.observe(viewLifecycleOwner){ isEditMode ->
            if(isEditMode) {
                requireContext().showKeyboard(binding.etTitle)
                binding.etTitle.isEnabled = true
                binding.etTitle.isClickable = true
                binding.etContent.isEnabled = true
                binding.etContent.isClickable = true
                binding.btnEdit.visibility = View.GONE
                binding.btnDone.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.GONE
            }
            if(!isEditMode){
                binding.etTitle.isEnabled = false
                binding.etTitle.isClickable = false
                binding.etContent.isEnabled = false
                binding.etContent.isClickable = false
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnDone.visibility = View.GONE
                binding.btnDelete.visibility = View.VISIBLE
            }
        }
    }

    private fun setNoteContent() {
        lifecycleScope.launch {
            val note = withContext(IO){
                viewModel.getNoteByID(noteId)
            }
            binding.etTitle.setText(note.title)
            binding.etContent.setText(note.body)
        }
    }

    private fun setOnClickListener() {
        binding.btnDone.setOnClickListener { onDone() }
        binding.btnBack.setOnClickListener { onBack() }
        binding.btnDelete.setOnClickListener { deleteNote() }
        binding.btnEdit.setOnClickListener { onEditClicked() }
        binding.view.setOnClickListener {
            binding.etContent.requestFocus()
        }
    }

    private fun deleteNote() {
        requireContext().hideKeyboard(binding.etTitle)
        setUpBottomSheet()
    }

    private fun onDone(){
        if(binding.etTitle.text?.isEmpty()!!){
            binding.etTitle.error = "Title can't be empty"
            return
        }
        if(isNewNote){
            createNote()
            return
        }
        updateNote()
    }

    private fun updateNote() {
        val title = binding.etTitle.text.toString()
        val description = binding.etContent.text.toString()
        viewModel.updateNote( Notes(noteId, title, description, getDate()) )
        onBack()
    }

    private fun createNote() {
        val title = binding.etTitle.text.toString()
        val description =
            binding.etContent.text.toString().ifBlank { "" }
        viewModel.insertNote(Notes(0,title,description,getDate()))
        onBack()
    }

    private fun onBack(){
        findNavController().navigate(R.id.action_detailNoteFragment_to_notesFragment)
    }

    private fun onEditClicked() {
        viewModel.setIsEditMode(true)
    }

    private fun getDate(): String{
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("dd LLL yyyy")
        return simpleDateFormat.format(calendar.time).toString()
    }

    private fun setUpBottomSheet() {
        val bottomSheet = BottomSheetDialog(requireContext())
        bottomSheet.setContentView(R.layout.delete_bottomsheet)
        val btnBottomSheetYes = bottomSheet.findViewById<Button>(R.id.yes)
        val btnBottomSheetNo = bottomSheet.findViewById<Button>(R.id.no)
        bottomSheet.show()
        btnBottomSheetYes?.setOnClickListener {
            viewModel.deleteNote(noteId)
            bottomSheet.dismiss()
            findNavController().navigate(R.id.action_detailNoteFragment_to_notesFragment)
        }
        btnBottomSheetNo?.setOnClickListener {
            bottomSheet.dismiss()
        }
    }
}