package xyz.droidev.notally.ui

import android.content.Intent
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
import xyz.droidev.notally.R
import xyz.droidev.notally.data.local.database.Notes
import xyz.droidev.notally.databinding.FragmentEditNoteBinding
import xyz.droidev.notally.util.hideKeyboard
import xyz.droidev.notally.util.showKeyboard
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class EditNoteFragment : Fragment() {

    private lateinit var binding: FragmentEditNoteBinding
    private lateinit var mNote: Notes
    private var noteId = -1
    private var isNewNote = false

    private val viewModel: NotesViewModel by lazy {
        ViewModelProvider(this).get(NotesViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditNoteBinding.inflate(layoutInflater)

        //get arguments arguments(clickedNote:Notes?, editMode = true)
        noteId = EditNoteFragmentArgs.fromBundle(requireArguments()).clickedNote

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()

        if (noteId == -1) {
            isNewNote = true
            viewModel.setIsEditMode(true)
        } else {
            setNoteContent()
            viewModel.setIsEditMode(false)
        }

        viewModel.isEditMode.observe(viewLifecycleOwner) { isEditMode ->
            if (isEditMode) {
                requireContext().showKeyboard(binding.etTitle)
                binding.etTitle.isEnabled = true
                binding.etTitle.isClickable = true
                binding.etContent.isEnabled = true
                binding.etContent.isClickable = true
                binding.btnEdit.visibility = View.GONE
                binding.btnDone.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.GONE
                binding.bottomCard.visibility = View.GONE
            }
            if (!isEditMode) {
                binding.etTitle.isEnabled = false
                binding.etTitle.isClickable = false
                binding.etContent.isEnabled = false
                binding.etContent.isClickable = false
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnDone.visibility = View.GONE
                binding.btnDelete.visibility = View.VISIBLE
                binding.bottomCard.visibility = View.VISIBLE
            }
        }
    }

    private fun setNoteContent() {
        lifecycleScope.launch {
            val note = withContext(IO) {
                viewModel.getNoteByID(noteId)
            }
            binding.etTitle.setText(note.title)
            binding.etContent.setText(note.body)
            binding.tvLastEdited.text = "Edited ${note.date}"
            mNote = note
        }
    }

    private fun setOnClickListener() {
        binding.btnDone.setOnClickListener { onDone() }
        binding.btnBack.setOnClickListener { onBack() }
        binding.btnDelete.setOnClickListener { deleteNote() }
        binding.btnEdit.setOnClickListener { onEditClicked() }

        binding.btnOptions.setOnClickListener {
            OptionsBottomSheetFragment(
                object : OptionsBottomSheetFragment.OptionsBottomSheetFragmentParams() {
                    override fun onMakeCopy() {
                        viewModel.insertNote(
                            Notes(
                                0,
                                mNote.title,
                                mNote.body,
                                getDate(),
                                getDate()
                            )
                        )
                        findNavController().navigate(R.id.action_detailNoteFragment_to_notesFragment)
                    }

                    override fun onSend() {
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                getString(R.string.send_note_template, mNote.title, mNote.body)
                            )
                        }
                        val chooser = Intent.createChooser(intent, "Complete action using...")
                        startActivity(chooser)
                    }

                    override fun onInfo() {
                        InfoBottomSheetFragment(
                            createdAt = mNote.created_at,
                            updatedAt = mNote.date,
                            charCount = mNote.body.length + mNote.title.length
                        ).show(parentFragmentManager, INFO_BOTTOM_SHEET_TAG)
                    }

                }
            ).show(parentFragmentManager, OPTIONS_BOTTOM_SHEET_TAG)
        }

    }

    private fun deleteNote() {
        requireContext().hideKeyboard(binding.etTitle)
        setUpDeleteBottomSheet()
    }

    private fun onDone() {
        if (binding.etTitle.text?.isEmpty()!!) {
            binding.etTitle.error = "Title can't be empty"
            return
        }
        if (isNewNote) {
            createNote()
            return
        }
        updateNote()
    }

    private fun updateNote() {
        val title = binding.etTitle.text.toString()
        val description = binding.etContent.text.toString()
        viewModel.updateNote(Notes(noteId, title, description, getDate(), created_at = mNote.date))
        onBack()
    }

    private fun createNote() {
        val title = binding.etTitle.text.toString()
        val description =
            binding.etContent.text.toString().ifBlank { " " }
        viewModel.insertNote(Notes(0, title, description, getDate(),getDate()))
        onBack()
    }

    private fun onBack() {
        findNavController().navigate(R.id.action_detailNoteFragment_to_notesFragment)
    }

    private fun onEditClicked() {
        viewModel.setIsEditMode(true)
    }

    private fun getDate(): String {
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("dd LLL yyyy")
        return simpleDateFormat.format(calendar.time).toString()
    }

    private fun setUpDeleteBottomSheet() {
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

    companion object {
        const val OPTIONS_BOTTOM_SHEET_TAG = "OPTIONS_BOTTOM_SHEET"
        const val INFO_BOTTOM_SHEET_TAG = "INFO_BOTTOM_SHEET"
        const val TAG = "EDIT_NOTE_FRAGMENT"
    }
}