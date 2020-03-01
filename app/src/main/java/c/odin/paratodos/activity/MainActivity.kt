package c.odin.paratodos.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import c.odin.paratodos.activity.ui.MainUI
import c.odin.paratodos.adapter.CompletedTodosAdapter
import c.odin.paratodos.adapter.TodoListAdapter
import c.odin.paratodos.model.Todo
import c.odin.paratodos.persistence.database
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.toast


private const val BUNDLE_TODO_LIST = "TodoList"
private val TAG = MainActivity::class.qualifiedName


class MainActivity : AppCompatActivity(), AnkoLogger {
    private lateinit var todoAdapter: TodoListAdapter
    private lateinit var completedTodoAdapter: CompletedTodosAdapter

    private val DETAIL_CODE = 42
    private lateinit var ui: MainUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val todos = database.getTodos()
        val openTodos = todos.filter { !it.completed }.toMutableList()
        val completedTodos = todos.filter { it.completed }.toMutableList()

        todoAdapter = TodoListAdapter(openTodos, this, DETAIL_CODE)
        completedTodoAdapter = CompletedTodosAdapter(completedTodos, this, DETAIL_CODE)

        ui = MainUI(todoAdapter, completedTodoAdapter, this)
        ui.setContentView(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            DETAIL_CODE -> {
                if (data != null) {
                    val affectedTodo = data.getParcelableExtra<Todo>(EXTRA_TODO)
                    val changeType = data.getSerializableExtra(EXTRA_CHANGE_TYPE) as CHANGE_TYPE

                    when (changeType) {
                        CHANGE_TYPE.UPDATE -> updateTodoListState(affectedTodo)
                        CHANGE_TYPE.DELETE -> {
                            toast("Deleted '${affectedTodo.title}'")
                            todoAdapter.deleteById(affectedTodo.id)
                            showRestoreButton(affectedTodo)
                        }
                        else -> Log.v(TAG, "No change")
                    }
                }
            }
        }
    }

    private fun updateTodoListState(updatedTodo: Todo) {
        todoAdapter.update(updatedTodo)
        todoAdapter.filterTodos { !it.completed }
    }

    private fun showRestoreButton(deletedTodo: Todo) {
        ui.activateRestoreButton(deletedTodo)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}

