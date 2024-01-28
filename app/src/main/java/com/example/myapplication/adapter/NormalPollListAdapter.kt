package com.example.myapplication.adapter

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MyAdapterListener
import com.example.myapplication.R

class NormalPollListAdapter(
    private val listener: MyAdapterListener,
    var itemList: HashMap<Int, String>,
    val callBack: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BlogViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        when (holder) {
            is BlogViewHolder -> {
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
                        Log.e("EditText_TAG", "bind: " + "setOnEditorActionListener")
                        listener.onItemClicked(position)
                        return@setOnEditorActionListener true
                    }
                    false
                }


                holder.textItem.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
//                        Log.d("textItem", "textItem: " + s.toString())
//                    itemList.add(position, s.toString())
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        Log.d("textItem", "position : $position || textItem: " + s)
                        if (itemList.size > 0) {
//                            itemList.remove(position)
//                            itemList.put(position,s.toString())
//                            if (!s.isNullOrBlank()) {
                                val isValueDuplicate = itemList.any { it.value == s.toString() && it.key != position }
                                if (isValueDuplicate) {
                                    // Show error message or handle the duplicate value as needed
                                    // For example, you can set an error message on the EditText
                                    holder.textItem.error = "already exists"
                                } else {
                                    // Update the itemList with the entered value
                                    itemList[position] = s.toString()
//                                }
                            }

                        }
//                        optionList[position] = s.toString()
//                        notifyDataSetChanged()
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


}