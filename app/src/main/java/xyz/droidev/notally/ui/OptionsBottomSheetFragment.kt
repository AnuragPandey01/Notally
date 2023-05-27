package xyz.droidev.notally.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import xyz.droidev.notally.R
import xyz.droidev.notally.databinding.BottomsheetOptionsBinding

class OptionsBottomSheetFragment(private val params: OptionsBottomSheetFragmentParams): BottomSheetDialogFragment() {

    private var _binding: BottomsheetOptionsBinding?  = null
    private val binding: BottomsheetOptionsBinding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetOptionsBinding.inflate(layoutInflater)

        setOnClickListener()
        return binding.root
    }

    private fun setOnClickListener() {
        binding.btnInfo.setOnClickListener {
            params.onInfo()
            dismiss()
        }
        binding.btnSend.setOnClickListener {
            params.onSend()
            dismiss()
        }
        binding.btnMakeCopy.setOnClickListener {
            params.onMakeCopy()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract class OptionsBottomSheetFragmentParams{

        abstract fun onMakeCopy()

        abstract fun onSend()

        abstract fun onInfo()
    }
}
