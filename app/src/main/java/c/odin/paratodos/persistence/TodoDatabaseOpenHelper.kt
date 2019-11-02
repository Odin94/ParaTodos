package c.odin.paratodos.persistence

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import c.odin.paratodos.activity.extensions.int
import c.odin.paratodos.model.Todo
import org.jetbrains.anko.db.*
import java.time.LocalDateTime
import java.time.ZoneOffset

const val TODO_TABLE_NAME = "Todo"

private val TAG = TodoDatabaseOpenHelper::class.qualifiedName

class TodoDatabaseOpenHelper private constructor(ctx: Context) :
    ManagedSQLiteOpenHelper(ctx, "Todos", null, 1) {
    init {
        instance = this
        onCreate(this.writableDatabase)
    }

    companion object {
        private var instance: TodoDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context) = instance ?: TodoDatabaseOpenHelper(ctx.applicationContext)
    }

    fun getTodos(): List<Todo> {
        return use {
            select(TODO_TABLE_NAME)
                .orderBy("id")
                .parseList(classParser<Todo>())
        }
    }

    fun getTodo(id: String): Todo {
        return use {
            select(TODO_TABLE_NAME)
                .whereSimple("id = ?", id)
                .parseSingle(classParser<Todo>())
        }
    }

    fun getTodo(id: Int) = getTodo(id.toString())

    fun store(todo: Todo): Long {
        return use {
            insert(
                TODO_TABLE_NAME,
                "date_created" to LocalDateTime.now(ZoneOffset.UTC).toString(),
                "title" to todo.title,
                "description" to todo.description,
                "priority" to "",
                "date_reminder" to "",
                "date_due" to "",
                "completed" to todo.completed.int
            )
        }
    }

    fun update(todo: Todo) {
        use {
            val updatedRows = update(
                TODO_TABLE_NAME,
                "date_created" to todo.date_created,
                "title" to todo.title,
                "description" to todo.description,
                "priority" to todo.priority,
                "date_reminder" to todo.date_reminder,
                "date_due" to todo.date_due,
                "completed" to todo.completed.int
            )
                .whereArgs("id = {todoId}", "todoId" to todo.id)
                .exec()

            if (updatedRows == 0) Log.e(TAG, "Failed to update TODO: $todo")
        }
    }

    fun delete(todo: Todo) {
        use {
            val deletedRows = delete(
                TODO_TABLE_NAME,
                "id = {todoId}",
                "todoId" to todo.id
            )

            if (deletedRows == 0) Log.e(TAG, "Failed to deleteByIndex TODO: $todo")
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable(
            TODO_TABLE_NAME, true,
            "id" to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            "date_created" to TEXT,
            "title" to TEXT,
            "description" to TEXT,
            "priority" to TEXT,
            "date_reminder" to TEXT,
            "date_due" to TEXT,
            "completed" to INTEGER + DEFAULT("0")
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(TODO_TABLE_NAME, true)
    }
}

val Context.database: TodoDatabaseOpenHelper
    get() = TodoDatabaseOpenHelper.getInstance(this)