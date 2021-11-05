package com.posse.android.translator.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.fragment.app.DialogFragment
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import com.posse.android.translator.databinding.DescriptionDialogLayoutBinding
import com.posse.android.translator.model.data.DataModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DescriptionFragment : DialogFragment(), KoinComponent {

    private var _binding: DescriptionDialogLayoutBinding? = null
    private val binding get() = _binding!!

    private val imageLoader: ImageLoader by inject()

    private var disposable: Disposable? = null

    private val data: DataModel? by lazy { arguments?.getParcelable(KEY_DATA) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DescriptionDialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.attributes?.apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
        }

        binding.header.text = data?.text
        binding.description.text = data?.meanings?.get(0)?.translation?.translation

        disposable = imageLoader.enqueue(
            ImageRequest.Builder(this.requireContext())
                .data("https://${data?.meanings?.get(0)?.imageUrl}")
                .crossfade(true)
                .target(binding.image)
                .build()
        )

        binding.closeBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroy() {
        disposable?.dispose()
        _binding = null
        super.onDestroy()
    }

    companion object {

        fun newInstance(data: DataModel) = DescriptionFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_DATA, data)
            }
        }

        private const val KEY_DATA = "data"
    }
}