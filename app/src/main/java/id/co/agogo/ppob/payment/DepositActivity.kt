package id.co.agogo.ppob.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.co.agogo.R

class DepositActivity : AppCompatActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        finishAndRemoveTask()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit)
    }
}
