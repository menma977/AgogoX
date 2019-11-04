package id.co.agogo.dialog

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.core.os.postDelayed
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.co.agogo.R

class BottomPopUpNavigationMenu : BottomSheetDialogFragment() {
    private var editTextChangeListener: EditTextChangeListener? = null
    private lateinit var imm: InputMethodManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.botttom_pop_up_navigation_menu, container, false)
        imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val passwordTRX: EditText = view.findViewById(R.id.password_trx)
        val login: Button = view.findViewById(R.id.loginButton)
        Handler().postDelayed(500) {
            passwordTRX.requestFocus()
            imm.showSoftInput(passwordTRX, InputMethodManager.SHOW_IMPLICIT)
        }

        login.setOnClickListener {
            try {
                Handler().postDelayed(500) {
                    passwordTRX.clearFocus()
                    imm.hideSoftInputFromWindow(passwordTRX.windowToken, 0)
                }
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
