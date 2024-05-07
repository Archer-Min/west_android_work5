package com.example.westwork5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.westwork5.adapter.MyAdapter
import com.example.westwork5.databinding.ActivityMyBinding
import com.example.westwork5.view.SlideDeleteRecyclerView
import java.util.*

class MyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val list = LinkedList<String>().apply {
            add("111111")
            add("222222")
            add("666666")
            add("999999")
            add("够了")
        }

        val adapter = MyAdapter(this, list)
        binding.rv.apply {
            layoutManager = LinearLayoutManager(this@MyActivity)
            this.adapter = adapter
            setOnItemActionListener(object : SlideDeleteRecyclerView.OnItemActionListener {
                override fun OnItemClick(position: Int) {
                    Toast.makeText(this@MyActivity, "Click$position", Toast.LENGTH_SHORT).show()
                }

                override fun OnItemTop(position: Int) {
                    val temp = list[position]
                    list.removeAt(position)
                    list.addFirst(temp)
                    adapter.notifyDataSetChanged()
                }

                override fun OnItemDelete(position: Int) {
                    list.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
            })
        }

    }
}