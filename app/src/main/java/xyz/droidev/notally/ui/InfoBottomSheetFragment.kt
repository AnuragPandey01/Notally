package xyz.droidev.notally.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import xyz.droidev.notally.R
import xyz.droidev.notally.databinding.BottomsheetInfoBinding

class InfoBottomSheetFragment(val charCount:Int,val createdAt: String, val updatedAt: String): BottomSheetDialogFragment() {

    private var _binding: BottomsheetInfoBinding?  = null
    private val binding: BottomsheetInfoBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetInfoBinding.inflate(layoutInflater)

        binding.tvCreatedAt.text = getString(R.string.created_at,createdAt)
        binding.tvUpdatedAt.text = getString(R.string.updated_at,updatedAt)
        binding.tvLetterCount.text = getString(R.string.letter_count,charCount)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}