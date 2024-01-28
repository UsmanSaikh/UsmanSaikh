package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.database.Entry
import com.example.myapplication.model.PollOption

class PollGroupAdapter(private val pollGroups: List<Entry>,var b: Boolean) : RecyclerView.Adapter<PollGroupAdapter.ViewHolder>() {

    lateinit var radioButton :RadioButton
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_poll_group, parent, false)

        radioButton = RadioButton(parent.context)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pollGroup = pollGroups[position]
        holder.bind(pollGroup)
    }

    override fun getItemCount(): Int {
        return pollGroups.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(pollGroup: Entry) {
            itemView.findViewById<TextView>(R.id.groupName).text = pollGroup.question

            var ChiledList: ArrayList<PollOption> = ArrayList()
            if (!b)
            {
                for (i in 0 until pollGroup.option.size) {
                    if (pollGroup.answer == i) {
                        ChiledList.add(PollOption(pollGroup.option[i],true))
                    } else {
                        ChiledList.add(PollOption(pollGroup.option[i],false))
                    }
                }
            }
            else{

                for (i in 0 until pollGroup.option.size) {
                    ChiledList.add(PollOption(pollGroup.option[i],false))
                }
            }




            val pollAdapter = CurrentPollListAdapter(ChiledList,pollGroup,b)
            itemView.findViewById<RecyclerView>(R.id.innerRecyclerView).apply {
                layoutManager = LinearLayoutManager(itemView.context)
                adapter = pollAdapter
            }
        }
    }

}
