package c.odin.paratodos.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import c.odin.paratodos.activity.ui.TodoDetailUI
import c.odin.paratodos.model.Todo
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.setContentView

public const val EXTRA_TODO = "ExtraTodo"
private const val BUNDLE_TODO = "BundleTodo"

class TodoDetailActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var todo: Todo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        todo = intent.getParcelableExtra(EXTRA_TODO)

        savedInstanceState?.let {
            todo = savedInstanceState.get(BUNDLE_TODO) as Todo
        }

        TodoDetailUI(todo).setContentView(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(BUNDLE_TODO, todo)
        super.onSaveInstanceState(outState)
    }
}
