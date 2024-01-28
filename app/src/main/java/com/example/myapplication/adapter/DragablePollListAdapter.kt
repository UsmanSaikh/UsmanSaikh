package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MyAdapterListener
import com.example.myapplication.MyApplication
import com.example.myapplication.R

class DragablePollListAdapter(
    private val listener: MyAdapterListener,
    var itemList: HashMap<Int, String>,
    val callBack: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DragablePollListAdapter.BlogViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        when (holder) {
            is DragablePollListAdapter.BlogViewHolder -> {
                holder.bind(itemList, position)

                holder.cancelButton.setOnClickListener {
                    if (itemList.size > position) {
                        itemList.remove(position)
                        notifyItemRemoved(position)
                        callBack.invoke(position)
                    }
                }

                if (position == 5) {
                    holder.textItem.imeOptions = EditorInfo.IME_ACTION_DONE;
                } else {
                    holder.textItem.imeOptions = EditorInfo.IME_ACTION_NEXT;
                }

                holder.textItem.setOnEditorActionListener { _, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_NEXT || (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                        listener.onItemClicked(position)
                        return@setOnEditorActionListener true
                    }
                    false
                }


                holder.textItem.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {

                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (itemList.size > 0) {

                            val isValueDuplicate =
                                itemList.any { it.value == s.toString() && it.key != position }
                            if (isValueDuplicate) {
                                holder.textItem.error = "already exists"
                            } else {
                                itemList[position] = s.toString()
                            }

                        }

                    }
                })
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    private class BlogViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textItem: EditText = itemView.findViewById(R.id.editView)
        val cancelButton: ImageView = itemView.findViewById(R.id.cancelButton)

        fun bind(itemList: HashMap<Int, String>, position: Int) {
            textItem.requestFocus()

            textItem.setText(itemList[position])

            textItem.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }


        }
    }


    fun enableDragAndDrop(recyclerView: RecyclerView) {
        val itemTouchHelper = ItemTouchHelper(DragItemTouchHelperCallback(this))
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    class DragItemTouchHelperCallback(private val adapter: DragablePollListAdapter) :
        ItemTouchHelper.Callback() {

        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        override fun isItemViewSwipeEnabled(): Boolean {
            return false
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            return makeMovementFlags(dragFlags, 0)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
            vibratePhone()
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        }

        private fun vibratePhone() {
            val vibrator =
                MyApplication.instance?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        200,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(200)
            }
        }
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {


        if (fromPosition != toPosition) {
            val fromEntry = itemList.entries.elementAt(fromPosition)
            val toEntry = itemList.entries.elementAt(toPosition)

            itemList.remove(fromEntry.key)
            itemList.remove(toEntry.key)

            itemList[toEntry.key] = fromEntry.value
            itemList[fromEntry.key] = toEntry.value

            notifyItemMoved(fromPosition, toPosition)
        }
    }


}


