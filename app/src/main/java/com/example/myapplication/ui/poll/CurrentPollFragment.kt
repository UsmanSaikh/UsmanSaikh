package com.example.myapplication.ui.poll

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MyApplication
import com.example.myapplication.adapter.CurrentPollListAdapter
import com.example.myapplication.adapter.PollGroupAdapter
import com.example.myapplication.database.DatabaseViewModel
import com.example.myapplication.database.PollDatabase
import com.example.myapplication.database.Repository
import com.example.myapplication.databinding.FragmentCurrentPollBinding

class CurrentPollFragment : Fragment() {

    private var _binding: FragmentCurrentPollBinding? = null

    private val binding get() = _binding!!
    private lateinit var pollGroupAdapter: PollGroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val currentPollModel =
            ViewModelProvider(this).get(CurrentPollModel::class.java)

        _binding = FragmentCurrentPollBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome

        currentPollModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }


        getData()

        return root
    }

    private fun getData() {


        val database by lazy { PollDatabase.getDatabase(requireActivity()) }
        val repository by lazy { Repository(database.expanseDao()) }

        repository.pollList.observe(viewLifecycleOwner) { list ->

            list.let {
                pollGroupAdapter = PollGroupAdapter(it, true)
                binding.rvPollList.adapter = pollGroupAdapter

                Log.e("Data", "getData: " + it.size )

                if (it.size != 0) {
                    binding.textHome.visibility = View.GONE
                } else {
                    binding.textHome.visibility = View.VISIBLE
                }
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}