package wardani.dika.gitsearch.util

import android.content.Intent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import wardani.dika.gitsearch.R
import kotlin.reflect.KClass


fun AppCompatActivity.startActivity(kClass: KClass<*>, block: Intent.() -> Unit = {}) {
    val intent = Intent(this, kClass.java)
    block(intent)
    startActivity(intent)
}

fun AppCompatActivity.showWarning(message: String?) {
    val bottomSheetDialog = BottomSheetDialog(this)
    bottomSheetDialog.setContentView(R.layout.error_dialog)
    bottomSheetDialog.findViewById<TextView>(R.id.messageError)?.text = message
    bottomSheetDialog.show()
}
