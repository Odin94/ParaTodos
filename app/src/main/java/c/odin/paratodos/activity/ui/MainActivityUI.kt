package c.odin.paratodos.activity.ui

import android.view.Gravity
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.core.content.ContextCompat
import c.odin.paratodos.R
import c.odin.paratodos.activity.MainActivity
import c.odin.paratodos.adapter.TodoAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.sdk27.coroutines.onClick


class MainUI(val todoAdapter: TodoAdapter) : AnkoComponent<MainActivity> {
    private val customStyle = { v: Any ->
        when (v) {
            is Button -> v.textSize = 26f
            is EditText -> v.textSize = 24f
        }
    }


    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        relativeLayout {
            var todoList: ListView? = null

            appBarLayout {
                lparams(width = matchParent, height = wrapContent)

                toolbar {
                    lparams(width = matchParent, height = wrapContent)

                    menu.apply {
                        add("Action1").apply {
                            tooltipText = "Start Action 1"

                            // Unfortunately you cant't use `icon = R.drawable.ic_action_foo` here,
                            // because it would expect a Drawable instead of a Resource ID
//                            setIcon(R.drawable.ic_action_foo)

                            setOnMenuItemClickListener {
                                //                                startActivity<Activity1>()
                                true
                            }
                        }

                        add("Action2")
                            // If you don't like the extra apply,
                            // you can also use chain most of the setters
                            .setTooltipText("Start Action 2")
//                            .setIcon(R.drawable.ic_action_bar)
                            .setOnMenuItemClickListener {
                                //                                startActivity<Activity2>()
                                true
                            }
                            // Not all types of menu Icons do actually show the icon,
                            // so make it an Action for demo purposes
                            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS) // <-- this is actually one of the setters, that cant be chained
                    }
                }
            }
            verticalLayout {
                todoList = listView {
                    //assign adapter
                    adapter = todoAdapter
                }
            }.lparams {
                margin = dip(5)
            }
            floatingActionButton {
                imageResource = android.R.drawable.ic_input_add
                onClick {
                    val adapter = todoList?.adapter as TodoAdapter
                    alert {
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
                                        toast("Oops!! Your task says nothing!")
                                    } else {
                                        adapter.add(task.text.toString())
//                                        showHideHintListView(todoList!!)
                                    }
                                }
                            }
                        }
                    }.show()
                }
            }.lparams {
                //setting button to bottom right of the screen
                margin = dip(10)
                alignParentBottom()
                alignParentEnd()
                alignParentRight()
                gravity = Gravity.BOTTOM or Gravity.END
            }

        }.applyRecursively(customStyle)
    }
}