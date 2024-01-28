package com.example.myapplication.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.PollGroupAdapter
import com.example.myapplication.database.PollDatabase
import com.example.myapplication.database.Repository
import com.example.myapplication.databinding.FragmentHistoryBinding

class HistoryPollFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private lateinit var pollGroupAdapter: PollGroupAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val historyViewModel =
            ViewModelProvider(this).get(HistoryViewModel::class.java)

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHistory
        historyViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        GetData()
        return root
    }

    private fun GetData() {

        val database by lazy { PollDatabase.getDatabase(requireActivity()) }
        val repository by lazy { Repository(database.expanseDao()) }


        repository.pollListHistory.observe(viewLifecycleOwner) { list ->
            list.let {
                pollGroupAdapter = PollGroupAdapter(it, false)
                binding.rvHistoryList.adapter = pollGroupAdapter
                if (it.size != 0) {
                    binding.textHistory.visibility = View.GONE
                } else {
                    binding.textHistory.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}