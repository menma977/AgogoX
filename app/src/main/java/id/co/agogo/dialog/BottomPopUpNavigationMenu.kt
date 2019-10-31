package id.co.agogo.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.co.agogo.R

class BottomPopUpNavigationMenu : BottomSheetDialogFragment() {
    private var editTextChangeListener: EditTextChangeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.botttom_pop_up_navigation_menu, container, false)

        val passwordTRX = view.findViewById<EditText>(R.id.password_trx)
        passwordTRX.setOnClickListener {
            try {
                editTextChangeListener?.onEditTextChange(passwordTRX.text.toString())
                dismiss()
            } catch (e: java.lang.ClassCastException) {
                throw ClassCastException(context.toString() + "must implement EditTextChangeListener")
            }
        }

        return view
    }

    interface EditTextChangeListener {
        fun onEditTextChange(text: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            editTextChangeListener = context as EditTextChangeListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement EditTextChangeListener")
        }

    }
}
