package c.odin.paratodos.activities

import android.os.Bundle
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick


class MainActivity : AppCompatActivity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUi().setContentView(this)
    }

    fun tryLogin(ui: AnkoContext<MainActivity>, name: CharSequence?, password: CharSequence?) {
        ui.doAsync {
            Thread.sleep(500)

            activityUiThreadWithContext {
                if (checkCredentials(name.toString(), password.toString())) {
                    toast("Logged in! :)")
//                    startActivity<CountriesActivity>()
                } else {
                    toast("Wrong password :( Enter user:password")
                }
            }
        }
    }

    private fun checkCredentials(name: String, password: String) = name == "user" && password == "password"
}

