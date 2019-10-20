package c.odin.paratodos.activity.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.ViewManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.widget.doOnTextChanged
import c.odin.paratodos.activity.TodoDetailActivity
import c.odin.paratodos.activity.extensions.getDateString
import c.odin.paratodos.activity.extensions.setDate
import c.odin.paratodos.model.Todo
import c.odin.paratodos.persistence.database
import com.google.android.material.appbar.AppBarLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onTouch


class TodoDetailUI(val todo: Todo, val activity: TodoDetailActivity) :
    AnkoComponent<TodoDetailActivity> {
    private val customStyle = { v: Any ->
        when (v) {
            is Button -> v.textSize = 26f
        }
    }


    @SuppressLint("SetTextI18n")
    override fun createView(ui: AnkoContext<TodoDetailActivity>) = with(ui) {
        coordinatorLayout {

            appBarLayout {
                toolbar {
                    addHamburgerButton(this, ctx)
                    populateMenu(menu)

                    menu
                }.lparams(width = matchParent, height = wrapContent)
            }.lparams(width = matchParent, height = wrapContent)

            verticalLayout {
                verticalLayout {
                    linearLayout {
                        // DUE DATE
                        textView {
                            text = todo.date_due
                            hint = "Due date"

                            onTouch { _, _ ->
                                val c = Calendar.getInstance()
                                val year = c.get(Calendar.YEAR)
                                val month = c.get(Calendar.MONTH)
                                val day = c.get(Calendar.DAY_OF_MONTH)

                                val dpd = DatePickerDialog(
                                    activity,
                                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                                        c.setDate(year, monthOfYear, dayOfMonth)
                                        todo.date_due = c.getDateString()

                                        ctx.database.updateTodo(todo)
                                    },
                                    year,
                                    month,
                                    day
                                )

                                dpd.show()
                            }
                        }
                    }.lparams(width = matchParent, height = wrapContent)

                    view {
                        // DIVIDER
                        background = ctx.getDrawable(android.R.color.darker_gray)
                    }.lparams(width = matchParent, height = dip(1)) {
                        topMargin = dip(15)
                        bottomMargin = dip(15)
                    }

                    editText {
                        // TITLE
                        setText(todo.title)
                        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        textSize = 24f
                        minWidth = dip(400)
                        background = ctx.getDrawable(android.R.color.transparent)

                        doOnTextChanged { text, _, _, _ ->
                            todo.title = text.toString()
                            ctx.database.updateTodo(todo)
                        }
                    }.lparams {
                        gravity = Gravity.START
                    }

                    view {
                        // DIVIDER
                        background = ctx.getDrawable(android.R.color.darker_gray)
                    }.lparams(width = matchParent, height = dip(1)) {
                        topMargin = dip(15)
                        bottomMargin = dip(15)
                    }

                    editText {
                        // DESCRIPTION
                        hint = "Description"
                        setText(todo.description)
                        textSize = 18f
                        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        background = ctx.getDrawable(android.R.color.transparent)

                        doOnTextChanged { text, _, _, _ ->
                            todo.description = text.toString()
                            ctx.database.updateTodo(todo)
                        }
                    }.lparams(width = matchParent, height = wrapContent) {
                        gravity = Gravity.START
                    }

                    showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE

                }.lparams(width = matchParent, height = matchParent) {
                    margin = dip(5)
                    padding = dip(20)
                }
            }.lparams(width = matchParent, height = matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }.applyRecursively(customStyle)
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
            val hamburgerIcon = ctx.getDrawable(c.odin.paratodos.R.drawable.ic_menu_24px)!!
            hamburgerIcon.setTint(ctx.getColor(android.R.color.white))
            setImageDrawable(hamburgerIcon)

            setBackgroundColor(android.R.drawable.screen_background_light_transparent)
            onClick { ctx.toast("Hello2") }
        }
    }
}