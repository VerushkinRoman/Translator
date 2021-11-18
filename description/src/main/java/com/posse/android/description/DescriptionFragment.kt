package com.posse.android.description

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import com.posse.android.description.databinding.DescriptionDialogLayoutBinding
import com.posse.android.models.DataModel
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent

class DescriptionFragment : DialogFragment(R.layout.description_dialog_layout), KoinComponent {

    private val binding by viewBinding(DescriptionDialogLayoutBinding::bind)

    private val imageLoader: ImageLoader by inject()

    private var disposable: Disposable? = null

    private val data: DataModel? by lazy { arguments?.getParcelable(KEY_DATA) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.attributes?.apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
        }

        with(binding) {
            header changeTextTo data?.text
            description changeTextTo data?.meanings?.get(0)?.translation?.translation

            closeBtn.setOnClickListener {
                dismiss()
            }
        }

        disposable = imageLoader.enqueue(
            ImageRequest.Builder(this.requireContext())
                .data("https://${data?.meanings?.get(0)?.imageUrl}")
                .crossfade(true)
                .target(binding.image)
                .build()
        )

    }

    override fun onDestroy() {
        disposable?.dispose()
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

    private infix fun TextView.changeTextTo(newText: String?) {
        this.text = newText
    }
}