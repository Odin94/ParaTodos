package c.odin.paratodos.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import c.odin.paratodos.activity.ui.TodoDetailUI
import c.odin.paratodos.model.Todo
import c.odin.paratodos.persistence.database
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.setContentView


const val EXTRA_TODO = "ExtraTodo"
const val EXTRA_CHANGE_TYPE = "ExtraChangeType"

private const val BUNDLE_TODO = "BundleTodo"
private const val BUNDLE_CHANGE_TYPE = "BundleChangeType"

enum class CHANGE_TYPE {
    NO_CHANGE,
    UPDATE,
    DELETE
}

class TodoDetailActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var todo: Todo
    private var changeType: CHANGE_TYPE = CHANGE_TYPE.NO_CHANGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        todo = intent.getParcelableExtra(EXTRA_TODO)

        savedInstanceState?.let {
            todo = savedInstanceState.get(BUNDLE_TODO) as Todo
            changeType = savedInstanceState.get(BUNDLE_CHANGE_TYPE) as CHANGE_TYPE
        }

        TodoDetailUI(todo, this).setContentView(this)
    }

    fun updateTodo(todo: Todo) {
        changeType = CHANGE_TYPE.UPDATE
        database.update(todo)
    }

    fun deleteTodo(todo: Todo) {
        changeType = CHANGE_TYPE.DELETE
        database.delete(todo)

        finishActivityWithResult()
    }

    override fun onBackPressed() {
        finishActivityWithResult()
    }

    private fun finishActivityWithResult() {
        val returnIntent = Intent()
        returnIntent.putExtra(EXTRA_TODO, todo)
        returnIntent.putExtra(EXTRA_CHANGE_TYPE, changeType)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(BUNDLE_TODO, todo)
        outState.putSerializable(BUNDLE_CHANGE_TYPE, changeType)
        super.onSaveInstanceState(outState)
    }
}
