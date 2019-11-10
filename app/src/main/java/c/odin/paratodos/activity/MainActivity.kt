package c.odin.paratodos.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import c.odin.paratodos.activity.ui.MainUI
import c.odin.paratodos.adapter.TodoListAdapter
import c.odin.paratodos.model.Todo
import c.odin.paratodos.persistence.database
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.toast


private const val BUNDLE_TODO_LIST = "TodoList"
private val TAG = MainActivity::class.qualifiedName


class MainActivity : AppCompatActivity(), AnkoLogger {
    private lateinit var todoAdapter: TodoListAdapter

    private val DETAIL_CODE = 42
    private lateinit var ui: MainUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val todoList: MutableList<Todo> = ArrayList<Todo>()
        savedInstanceState?.let {
            val arrayList = savedInstanceState.get(BUNDLE_TODO_LIST)
            todoList.addAll((arrayList as List<Todo>).filter { !it.completed })
        }

        doAsync {
            val titles = database.getTodos()
            titles.filter { !it.completed }.forEach { todoAdapter.add(it) }
            todoAdapter.notifyDataSetChanged()
        }

        todoAdapter = TodoListAdapter(todoList, this, DETAIL_CODE)

        ui = MainUI(todoAdapter, this)
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
        outState.putParcelableArrayList(BUNDLE_TODO_LIST, ArrayList(todoAdapter.getTodoListCopy()))
        super.onSaveInstanceState(outState)
    }
}

