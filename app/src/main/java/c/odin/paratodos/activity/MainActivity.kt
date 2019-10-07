package c.odin.paratodos.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import c.odin.paratodos.activity.ui.MainUI
import c.odin.paratodos.adapter.TodoListAdapter
import c.odin.paratodos.persistence.database
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.setContentView


private const val BUNDLE_TODO_LIST = "TodoList"

class MainActivity : AppCompatActivity(), AnkoLogger {

    private val todoList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            val arrayList = savedInstanceState.get(BUNDLE_TODO_LIST)
            todoList.addAll(arrayList as List<String>)
        }

        val todoAdapter = TodoListAdapter(todoList)

        val ctx = this
        doAsync {
            val titles = database.getTodos().map { it.title }.toMutableList()
            titles.forEach { todoAdapter.add(it) }
            todoAdapter.notifyDataSetChanged()
        }

        MainUI(TodoListAdapter(todoList)).setContentView(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArrayList(BUNDLE_TODO_LIST, todoList)
        super.onSaveInstanceState(outState)
    }
}

