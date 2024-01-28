package com.example.myapplication

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.CurrentPollListAdapter
import com.example.myapplication.adapter.DragablePollListAdapter
import com.example.myapplication.adapter.NormalPollListAdapter
import com.example.myapplication.database.DatabaseViewModel
import com.example.myapplication.database.Entry
import com.example.myapplication.databinding.ActivityCreatePollBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreatePollActivity : AppCompatActivity(), MyAdapterListener {

    private lateinit var binding: ActivityCreatePollBinding


    private val pollViewModel: DatabaseViewModel by viewModels {
        DatabaseViewModel.DatabaseViewModelFactory((application as MyApplication).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreatePollBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initClicks()

        val itemList = HashMap<Int, String>(5)
        val adapter = DragablePollListAdapter(this,itemList){
            if (it < 5) {
                binding.tvAddOption.background =
                    ContextCompat.getDrawable(this, R.drawable.add_option_bg)

                binding.tvOption.text = "You can add " + (5 - itemList.size) + " more options"
            }
        }.also {
            binding.listview.adapter = it
            it.enableDragAndDrop(binding.listview)
        }

        binding.tvOption.text = "You can add " + 5 + " more options"
        binding.tvAddOption.setOnClickListener {
            if (itemList.size < 5) {
                if (itemList.isEmpty()) {
                    itemList.put(0, "")
                } else {
                    val position = itemList.size
                    itemList.put(position, "")
                }

                binding.tvOption.text = "You can add " + (5 - itemList.size) + " more options"

                if (itemList.size >= 5) {
                    binding.tvAddOption.background =
                        ContextCompat.getDrawable(this, R.drawable.add_nonselected_option_bg)
                }

                binding.listview.adapter?.notifyItemInserted(itemList.size)
            }
        }

        binding.tvCreate.setOnClickListener {
            if (!binding.etQuestion.text.equals("")) {
                if (itemList.size >= 2) {
                    var functionExecuted = false
                    for (i in 0 until itemList.size) {
                        if (!itemList.get(i).equals("")) {
                            if (!functionExecuted) {
                                val entry = Entry(
                                    0,
                                    binding.etQuestion.text.toString(),
                                    ArrayList(adapter.itemList.values),
                                    false,
                                    0
                                )
                                CoroutineScope(Dispatchers.IO).launch {
                                    if (!pollViewModel.isExist(entry.question)) {
                                        pollViewModel.insert(entry)
                                        runOnUiThread(Runnable {
                                            Toast.makeText(
                                                this@CreatePollActivity,
                                                "Poll Created",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        })
                                        finish()
                                    } else {
                                        runOnUiThread(Runnable {
                                            Toast.makeText(
                                                this@CreatePollActivity,
                                                "this Question is Already Exist ",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        })

                                    }
                                }
                                functionExecuted = true
                            }
                        }
                        else{
                            runOnUiThread(Runnable {
                                Toast.makeText(
                                    this,
                                    "Please Enter Valid Question at least two option ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                        }
                    }
                } else {
                    runOnUiThread(Runnable {
                        Toast.makeText(
                            this,
                            "Please Enter Valid Question at least two option ",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
                }
            } else {
                runOnUiThread(Runnable {
                    Toast.makeText(
                        this,
                        "Please Enter Valid Question at least two option ",
                        Toast.LENGTH_SHORT
                    ).show()
                })

            }
        }
    }

    private fun initClicks() {
        binding.ivBack.setOnClickListener { view ->
            onBackPressed()
        }
    }

    override fun onItemClicked(position: Int) {
        binding.tvAddOption.performClick()
    }

    override fun onEditTextValueChanged(position: Int, value: String) {
    }

}