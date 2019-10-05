package c.odin.paratodos.adapter

import android.graphics.Typeface
import android.graphics.Typeface.DEFAULT_BOLD
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout.HORIZONTAL
import org.jetbrains.anko.*


class TodoAdapter(private val todoList: ArrayList<String> = ArrayList<String>()) : BaseAdapter() {
    override fun getView(i: Int, v: View?, parent: ViewGroup?): View {
        return with(parent!!.context) {
            //taskNum will serve as the S.No. of the todoList starting from 1
            val taskNum: Int = i + 1

            //Layout for a todoList view item
            linearLayout {
                //                id = R.id.listItemContainer
                lparams(width = matchParent, height = wrapContent)
                padding = dip(10)
                orientation = HORIZONTAL

                textView {
                    //                    id = R.id.taskNum
                    text = taskNum.toString()
                    textSize = 16f
                    typeface = Typeface.MONOSPACE
                    padding = dip(5)
                }

                textView {
                    //                    id = R.id.taskName
                    text = todoList[i]
                    textSize = 16f
                    typeface = DEFAULT_BOLD
                    padding = dip(5)
                }
            }
        }
    }

    override fun getItem(position: Int): String {
        return todoList[position]
    }

    override fun getCount(): Int {
        return todoList.size
    }

    override fun getItemId(position: Int): Long {
        //can be used to return the item's ID column of table
        return 0L
    }

    //function to add an item to the todoList
    fun add(text: String) {
        todoList.add(todoList.size, text)
        notifyDataSetChanged()
    }

    //function to delete an item from todoList
    fun delete(i: Int) {
        todoList.removeAt(i)
        notifyDataSetChanged()
    }

}