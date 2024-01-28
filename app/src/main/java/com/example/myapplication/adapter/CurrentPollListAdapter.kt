package com.example.myapplication.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MyApplication
import com.example.myapplication.R
import com.example.myapplication.database.Entry
import com.example.myapplication.database.PollDatabase
import com.example.myapplication.database.Repository
import com.example.myapplication.model.PollOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrentPollListAdapter(
    private val options: List<PollOption>,
    val pollGroup: Entry,
    val b: Boolean
) :
    RecyclerView.Adapter<CurrentPollListAdapter.ViewHolder>() {

    var selectedOption: Int = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.current_poll_list_item_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val option = options[position]

        holder.radioButton.text = option.optionText
        holder.radioButton.isChecked = selectedOption == position

        if (b) {
            holder.radioButton.setOnClickListener {
                selectedOption = position

                val itemList = ArrayList<String>()
                for (i in 0 until options.size) {
                    options.get(i).selected = false
                    itemList.add(options.get(i).optionText)
                }
                options.get(position).selected = true
                notifyItemChanged(selectedOption)

                val applicationContext: Context = MyApplication.getAppContext()

                val database by lazy { PollDatabase.getDatabase(applicationContext) }
                val repository by lazy { Repository(database.expanseDao()) }

                val entry = Entry(pollGroup.id, pollGroup.question, itemList, true, selectedOption)
                CoroutineScope(Dispatchers.IO).launch {
                    repository.upDate(entry)
                }
            }

            if (option.selected) {
                holder.radioButton.isChecked = true
                animateBackgroundColor(holder.linearLayout)
                holder.tvPersent.text = "100%"
            } else {
                holder.radioButton.isChecked = false
                animateBackgroundColorNormal(holder.linearLayout)
                holder.tvPersent.text = "0%"
            }

        } else {
            if (option.selected) {
                holder.radioButton.isChecked = true
                animateBackgroundColor(holder.linearLayout)
                holder.tvPersent.text = "100%"
            } else {
                holder.radioButton.isChecked = false
                animateBackgroundColorNormal(holder.linearLayout)
                holder.linearLayout.visibility = View.GONE
                holder.radioButton.isClickable = false
                holder.tvPersent.text = "0%"
                holder.radioButton.buttonDrawable = null
            }
        }

    }

    private fun animateBackgroundColor(view: View) {
        val colorFrom = android.graphics.Color.parseColor("#E8DEF8")
        val colorTo = android.graphics.Color.parseColor("#FF6200EE")

        val backgroundColorAnimator =
            ObjectAnimator.ofArgb(view, "backgroundColor", colorFrom, colorTo)
        backgroundColorAnimator.duration = 100
        backgroundColorAnimator.interpolator = AccelerateInterpolator()

        val animatorSet = AnimatorSet()
        animatorSet.play(backgroundColorAnimator)
        animatorSet.start()
    }

    private fun animateBackgroundColorNormal(view: View) {
        val colorFrom = android.graphics.Color.parseColor("#E8DEF8")
        val colorTo = android.graphics.Color.parseColor("#FF6200EE")

        val backgroundColorAnimator =
            ObjectAnimator.ofArgb(view, "backgroundColor", colorTo, colorFrom)
        backgroundColorAnimator.duration = 500
        backgroundColorAnimator.interpolator = AccelerateInterpolator()

        val animatorSet = AnimatorSet()
        animatorSet.play(backgroundColorAnimator)
        animatorSet.start()
    }

    override fun getItemCount(): Int {
        return options.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var radioButton: RadioButton
        lateinit var linearLayout: LinearLayout
        lateinit var tvPersent: TextView
        fun bind(option: PollOption, position: Int) {
            itemView.setOnClickListener {
                selectedOption = adapterPosition
                notifyDataSetChanged()
            }
        }

        init {
            radioButton = itemView.findViewById(R.id.radioButton)
            linearLayout = itemView.findViewById<LinearLayout>(R.id.linear_layout)
            tvPersent = itemView.findViewById<TextView>(R.id.tvPersent)


        }
    }
}