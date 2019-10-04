package c.odin.paratodos.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import c.odin.paratodos.activity.ui.MainUI
import c.odin.paratodos.adapter.TodoAdapter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.setContentView


class MainActivity : AppCompatActivity(), AnkoLogger {

    val todoList = ArrayList<String>()
    val BUNDLE_TODO_LIST = "TodoList"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            val arrayList = savedInstanceState.get(BUNDLE_TODO_LIST)
            todoList.addAll(arrayList as List<String>)
        }

        MainUI(TodoAdapter(todoList)).setContentView(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArrayList(BUNDLE_TODO_LIST, todoList)
        super.onSaveInstanceState(outState)
    }

//    fun tryLogin(ui: AnkoContext<MainActivity>, name: CharSequence?, password: CharSequence?) {
//        ui.doAsync {
//            Thread.sleep(500)
//
//            activityUiThreadWithContext {
//                if (checkCredentials(name.toString(), password.toString())) {
//                    toast("Logged in! :)")
////                    startActivity<CountriesActivity>()
//                } else {
//                    toast("Wrong password :( Enter user:password")
//                }
//            }
//        }
//    }
//
//    private fun checkCredentials(name: String, password: String) =
//        name == "user" && password == "password"
}

