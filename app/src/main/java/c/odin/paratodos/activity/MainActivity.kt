package c.odin.paratodos.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import c.odin.paratodos.activity.ui.MainUI
import c.odin.paratodos.adapter.TodoListAdapter
import c.odin.paratodos.model.Todo
import c.odin.paratodos.persistence.database
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.setContentView


private const val BUNDLE_TODO_LIST = "TodoList"

class MainActivity : AppCompatActivity(), AnkoLogger {

    private var todoList: MutableList<Todo> = ArrayList<Todo>()
    private lateinit var todoAdapter: TodoListAdapter

    private val DETAIL_CODE = 42

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            val arrayList = savedInstanceState.get(BUNDLE_TODO_LIST)
            todoList.addAll(arrayList as List<Todo>)
        }

        doAsync {
            val titles = database.getTodos()
            titles.filter { !it.completed }.forEach { todoAdapter.add(it) }
            todoAdapter.notifyDataSetChanged()
        }

        todoAdapter = TodoListAdapter(todoList, this, DETAIL_CODE)

        MainUI(todoAdapter, this).setContentView(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            DETAIL_CODE -> {
                if (data != null) {
                    val updatedTodo = data.getParcelableExtra<Todo>(EXTRA_TODO)
                    todoAdapter.update(updatedTodo)

                    database.updateTodo(updatedTodo)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(BUNDLE_TODO_LIST, ArrayList(todoList))
        super.onSaveInstanceState(outState)
    }
}

