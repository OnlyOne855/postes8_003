package com.example.post8_003

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.post8_003.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tugasRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvTugas.layoutManager = LinearLayoutManager(this)

        tugasRef = FirebaseDatabase.getInstance().getReference("tugas")

        fetchData()

        binding.fabAddTugas.setOnClickListener {
            AddTugasDialog(this, tugasRef).show()
        }
    }

    private fun fetchData() {
        tugasRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Tugas>()

                snapshot.children.forEach {
                    it.getValue(Tugas::class.java)?.let(list::add)
                }

                if (list.isEmpty()) {
                    binding.tvTugas.visibility = View.GONE
                    binding.emptyView.root.visibility = View.VISIBLE
                } else {
                    binding.tvTugas.visibility = View.VISIBLE
                    binding.emptyView.root.visibility = View.GONE
                    binding.tvTugas.adapter = TugasAdapter(list, tugasRef)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}