package c.odin.paratodos.activity.ui

import android.content.Context
import android.icu.util.Calendar
import android.os.Handler
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.ViewManager
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import c.odin.paratodos.R
import c.odin.paratodos.activity.MainActivity
import c.odin.paratodos.activity.extensions.getDateString
import c.odin.paratodos.activity.extensions.openDatePicker
import c.odin.paratodos.activity.extensions.setDate
import c.odin.paratodos.adapter.TodoListAdapter
import c.odin.paratodos.model.Todo
import c.odin.paratodos.persistence.database
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.jetbrains.anko.*
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.sdk27.coroutines.onClick


class MainUI(val todoListAdapter: TodoListAdapter, val activity: MainActivity) :
    AnkoComponent<MainActivity> {
    private val customStyle = { v: Any ->
        when (v) {
            is Button -> v.textSize = 26f
            is EditText -> v.textSize = 24f
        }
    }
    private lateinit var mainLayout: CoordinatorLayout
    private var todoToRestore: Todo? = null
    private lateinit var restoreButton: FloatingActionButton

    override fun createView(ui: AnkoContext<MainActivity>): View {
        mainLayout = with(ui) {
            coordinatorLayout {
                lparams(width = matchParent, height = matchParent)
                appBarLayout {
                    lparams(width = matchParent, height = wrapContent)


                    toolbar {
                        lparams(width = matchParent, height = wrapContent)

                        addHamburgerButton(this, ctx)
                        populateMenu(menu)

                        menu
                    }
                }

                var todoList: ListView? = null

                val hintListView = textView("What's your Todo List for today?") {
                    textSize = 20f
                }.lparams {
                    gravity = Gravity.CENTER
                }


                verticalLayout {
                    todoList = listView {
                        //assign adapter
                        adapter = todoListAdapter
                    }
                }.lparams {
                    margin = dip(5)
                    behavior = AppBarLayout.ScrollingViewBehavior()
                }

                addFloatingPlusButton(this, ctx, todoList, hintListView)
                    .lparams {
                        horizontalMargin = dip(25)
                        verticalMargin = dip(25)
                        gravity = Gravity.BOTTOM or Gravity.END
                    }

                restoreButton = addRestoreButton(this, ctx)
                    .lparams {
                        horizontalMargin = dip(25)
                        verticalMargin = dip(25)
                        gravity = Gravity.BOTTOM or Gravity.START
                    }

                showHideHintListView(todoList, hintListView)
            }.applyRecursively(customStyle)
        }

        return mainLayout
    }

    fun activateRestoreButton(todo: Todo) {
        todoToRestore = todo
        restoreButton.show()

        Handler().postDelayed({
            resetRestoreButton()
        }, 5000)
    }

    private fun resetRestoreButton() {
        todoToRestore = null
        restoreButton.hide()
    }

    private fun addRestoreButton(layout: CoordinatorLayout, ctx: Context): FloatingActionButton {
        return layout.floatingActionButton {
            val undoIcon = context.getDrawable(R.drawable.ic_undo_24px)!!
            undoIcon.setTint(context.getColor(android.R.color.white))
            setImageDrawable(undoIcon)
            onClick {
                val todoToRestoreCopy = todoToRestore
                if (todoToRestoreCopy != null) {
                    context.database.store(todoToRestoreCopy)
                    todoListAdapter.add(todoToRestoreCopy)
                    context.toast("Restored '${todoToRestoreCopy.title}'")

                    resetRestoreButton()
                }
            }

            hide()
        }
    }

    private fun populateMenu(menu: Menu) {
        menu.apply {
            add("Action1").apply {
                tooltipText = "Start Action 1"


                setOnMenuItemClickListener {
                    //                                startActivity<Activity1>()
                    true
                }
            }

            add("Action 2").apply {
                tooltipText = "Start Action 2"

                setOnMenuItemClickListener {
                    // startActivity<Activity2>()
                    true
                }
            }
        }
    }

    private fun addHamburgerButton(vm: ViewManager, ctx: Context): ImageButton {
        return vm.imageButton {
            val hamburgerIcon = ctx.getDrawable(R.drawable.ic_menu_24px)!!
            hamburgerIcon.setTint(ctx.getColor(android.R.color.white))
            setImageDrawable(hamburgerIcon)

            setBackgroundColor(android.R.drawable.screen_background_light_transparent)
            onClick { ctx.toast("Hello") }
        }
    }

    private fun addFloatingPlusButton(
        layout: CoordinatorLayout,
        ctx: Context,
        todoList: ListView?,
        hintListView: TextView
    ): FloatingActionButton {
        return layout.floatingActionButton {
            imageResource = android.R.drawable.ic_input_add
            onClick {
                ctx.alert {
                    customView {
                        verticalLayout {
                            //Dialog Title
                            toolbar {
                                //                                    id = R.id.dialog_toolbar
                                lparams(width = matchParent, height = wrapContent)
                                backgroundColor =
                                    ContextCompat.getColor(ctx, R.color.colorAccent)
                                title = "What's your next milestone?"
                                setTitleTextColor(
                                    ContextCompat.getColor(
                                        ctx,
                                        android.R.color.white
                                    )
                                )
                            }
                            val task = editText {
                                hint = "To do task "
                                padding = dip(20)
                            }
                            var dateDue = ""

                            linearLayout {
                                imageButton {
                                    val calendarIcon =
                                        ctx.getDrawable(R.drawable.ic_calendar_today_24px)!!
                                    calendarIcon.setTint(ctx.getColor(android.R.color.darker_gray))
                                    setImageDrawable(calendarIcon)

                                    setBackgroundColor(android.R.drawable.screen_background_light_transparent)
                                    onClick {
                                        val c = Calendar.getInstance()
                                        c.openDatePicker(activity) { view, year, monthOfYear, dayOfMonth ->
                                            c.setDate(year, monthOfYear, dayOfMonth)
                                            dateDue = c.getDateString()

                                            calendarIcon.setTint(ctx.getColor(android.R.color.holo_red_dark))
                                        }
                                    }
                                }
                            }.lparams(width = matchParent, height = wrapContent) {
                                padding = dip(20)
                            }

                            positiveButton("Add") {
                                if (task.text.toString().isEmpty()) {
                                    ctx.toast("Oops!! Your task says nothing!")
                                } else {
                                    val newTodo = Todo(
                                        title = task.text.toString(),
                                        date_due = dateDue
                                    )
                                    newTodo.id = ctx.database.store(newTodo)
                                    todoListAdapter.add(newTodo)
                                    showHideHintListView(todoList, hintListView)
                                }
                            }
                        }
                    }
                }.show()
            }
        }
    }

    private fun showHideHintListView(listView: ListView?, hintListView: TextView) {
        if (listView == null || getTotalListItems(listView) > 0) {
            hintListView.visibility = View.GONE
        } else {
            hintListView.visibility = View.VISIBLE
        }
    }

    private fun getTotalListItems(list: ListView?) = list?.adapter?.count ?: 0
}