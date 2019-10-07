package c.odin.paratodos.activity.ui

import android.content.Context
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.ViewManager
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import c.odin.paratodos.R
import c.odin.paratodos.activity.MainActivity
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


class MainUI(val todoAdapter: TodoListAdapter) : AnkoComponent<MainActivity> {
    private val customStyle = { v: Any ->
        when (v) {
            is Button -> v.textSize = 26f
            is EditText -> v.textSize = 24f
        }
    }


    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        coordinatorLayout {
            lparams(width = matchParent, height = matchParent)
            appBarLayout {
                lparams(width = matchParent, height = wrapContent)


                toolbar {
                    lparams(width = matchParent, height = wrapContent)

                    addHamburgerButton(this, ctx)
                    populateMenu(menu, ctx)

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
                    adapter = todoAdapter
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

            showHideHintListView(todoList, hintListView)
        }.applyRecursively(customStyle)

    }

    private fun populateMenu(menu: Menu, ctx: Context) {
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
                val adapter = todoList?.adapter as TodoListAdapter
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
                            positiveButton("Add") {
                                if (task.text.toString().isEmpty()) {
                                    ctx.toast("Oops!! Your task says nothing!")
                                } else {
                                    adapter.add(task.text.toString())
                                    ctx.database.storeTodo(
                                        Todo(
                                            -1,
                                            "",
                                            task.text.toString(),
                                            "",
                                            "",
                                            "",
                                            ""
                                        )
                                    )
                                    showHideHintListView(todoList!!, hintListView)
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