package c.odin.paratodos.activity.ui

import android.content.Context
import android.view.Gravity
import android.view.Menu
import android.view.ViewManager
import android.widget.*
import androidx.core.content.ContextCompat
import c.odin.paratodos.R
import c.odin.paratodos.activity.MainActivity
import c.odin.paratodos.adapter.TodoAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

                    addHamburgerButton(this, ctx)
                    populateMenu(menu, ctx)

                    menu
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

            addFloatingPlusButton(this, ctx, todoList)
                .lparams {
                    //setting button to bottom right of the screen
                    margin = dip(10)
                    alignParentBottom()
                    alignParentEnd()
                    alignParentRight()
                    gravity = Gravity.BOTTOM or Gravity.END
                }

        }.applyRecursively(customStyle)
    }

    private fun populateMenu(menu: Menu, ctx: Context) {
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
        rl: RelativeLayout,
        ctx: Context,
        todoList: ListView?
    ): FloatingActionButton {
        return rl.floatingActionButton {
            imageResource = android.R.drawable.ic_input_add
            onClick {
                val adapter = todoList?.adapter as TodoAdapter
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
//                                        showHideHintListView(todoList!!)
                                }
                            }
                        }
                    }
                }.show()
            }
        }
    }
}