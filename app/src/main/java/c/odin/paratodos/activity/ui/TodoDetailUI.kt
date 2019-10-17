package c.odin.paratodos.activity.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.Menu
import android.view.View
import android.view.ViewManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import c.odin.paratodos.R
import c.odin.paratodos.activity.TodoDetailActivity
import c.odin.paratodos.model.Todo
import org.jetbrains.anko.*
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.sdk27.coroutines.onClick


class TodoDetailUI(val todo: Todo) : AnkoComponent<TodoDetailActivity> {
    private val customStyle = { v: Any ->
        when (v) {
            is Button -> v.textSize = 26f
            is EditText -> v.textSize = 24f
        }
    }


    @SuppressLint("SetTextI18n")
    override fun createView(ui: AnkoContext<TodoDetailActivity>) = with(ui) {
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

            verticalLayout {
                textView {
                    text = "Descritpion: " + todo.description
                    textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                }
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
            val hamburgerIcon = ctx.getDrawable(R.drawable.ic_menu_24px)!!
            hamburgerIcon.setTint(ctx.getColor(android.R.color.white))
            setImageDrawable(hamburgerIcon)

            setBackgroundColor(android.R.drawable.screen_background_light_transparent)
            onClick { ctx.toast("Hello") }
        }
    }
}