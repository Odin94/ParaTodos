package c.odin.paratodos.adapter

import android.app.Activity
import android.graphics.Typeface
import android.graphics.Typeface.DEFAULT_BOLD
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout.HORIZONTAL
import c.odin.paratodos.activity.EXTRA_TODO
import c.odin.paratodos.activity.TodoDetailActivity
import c.odin.paratodos.model.Todo
import c.odin.paratodos.persistence.database
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class TodoListAdapter(
    private var todoList: MutableList<Todo> = ArrayList(),
    private val activity: Activity,
    private val detailRequestCode: Int,
    var completedTodosAdapter: CompletedTodosAdapter? = null
) : BaseAdapter() {
    override fun getView(i: Int, v: View?, parent: ViewGroup?): View {
        return with(parent!!.context) {
            //taskNum will serve as the S.No. of the todoList starting from 1
            val taskNum: Int = i + 1
            val todo = todoList[i]

            //Layout for a todoList view item
            linearLayout {
                lparams(width = matchParent, height = wrapContent)
                padding = dip(10)
                orientation = HORIZONTAL
                isClickable = true
                setOnClickListener {
                    toast(todo.title)
                    activity.startActivityForResult<TodoDetailActivity>(
                        detailRequestCode,
                        EXTRA_TODO to todo
                    )
                }

                checkBox {
                    focusable = View.NOT_FOCUSABLE

                    var checkOffTimer = Timer("checkOffTimer#$taskNum", false)
                    onCheckedChange { _, isChecked ->
                        if (isChecked) {
                            todo.completed = true
                            database.update(todo)
                            checkOffTimer.schedule(1000) {
                                runOnUiThread {
                                    completedTodosAdapter?.add(todo)
                                    deleteByIndex(i)
                                }
                            }
                        } else {
                            todo.completed = false
                            database.update(todo)
                            checkOffTimer.cancel()
                            checkOffTimer = Timer("checkOffTimer#$taskNum", false)
                        }
                    }
                }

                textView {
                    //                    id = R.id.taskNum
                    text = taskNum.toString()
                    textSize = 16f
                    typeface = Typeface.MONOSPACE
                    padding = dip(5)
                }

                textView {
                    //                    id = R.id.taskName
                    text = todo.title
                    textSize = 16f
                    typeface = DEFAULT_BOLD
                    padding = dip(5)
                }
            }
        }
    }

    override fun getItem(position: Int): Todo {
        return todoList[position]
    }

    override fun getCount(): Int {
        return todoList.size
    }

    override fun getItemId(position: Int): Long {
        //can be used to return the item's ID column of table
        return 0L
    }

    fun add(todo: Todo) {
        todoList.add(todoList.size, todo)
        notifyDataSetChanged()
    }

    // deleteByIndex by id here (or make separate function) TODO
    fun deleteByIndex(i: Int) {
        todoList.removeAt(i)
        notifyDataSetChanged()
    }

    fun deleteByIndex(i: Long) {
        deleteByIndex(i.toInt())
    }

    fun deleteById(i: Long) {
        filterTodos { it.id != i }
    }

    fun filterTodos(filterCondition: (Todo) -> Boolean) {
        todoList = todoList.filter(filterCondition).toMutableList()
        notifyDataSetChanged()
    }

    fun update(todo: Todo) {
        todoList.replaceAll { if (it.id == todo.id) todo else it }
        notifyDataSetChanged()
    }

    fun getTodoListCopy() = todoList.toMutableList()
}