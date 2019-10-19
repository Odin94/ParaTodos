package c.odin.paratodos.activity.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.ViewManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import c.odin.paratodos.activity.TodoDetailActivity
import c.odin.paratodos.model.Todo
import com.google.android.material.appbar.AppBarLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.sdk27.coroutines.onClick


class TodoDetailUI(val todo: Todo) : AnkoComponent<TodoDetailActivity> {
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
                    textView {
                        text = todo.title
                        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        textSize = 24f
                    }.lparams {
                        gravity = Gravity.START
                    }

                    view {
                        background = ctx.getDrawable(android.R.color.darker_gray)
                    }.lparams(width = matchParent, height = dip(1)) {
                        topMargin = dip(15)
                        bottomMargin = dip(15)
                    }

                    textView {
                        hint = "Description"
                        text = todo.description
                        textSize = 18f
                        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                    }.lparams(width = matchParent, height = matchParent) {
                        gravity = Gravity.CENTER
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