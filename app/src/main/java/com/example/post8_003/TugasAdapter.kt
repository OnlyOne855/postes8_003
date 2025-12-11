package com.example.post8_003

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.example.post8_003.databinding.ItemTugasBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database

class TugasAdapter(
    private val list: List<Tugas>,
    private val ref: DatabaseReference
) : RecyclerView.Adapter<TugasAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemTugasBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tugas: Tugas) {
            binding.cbSelesai.isChecked = tugas.selesai
            binding.tvTugas.text = tugas.tugas
            binding.tvDeskripsi.text = tugas.deskripsi
            binding.tvTanggal.text = tugas.tanggal

            binding.cbSelesai.setOnCheckedChangeListener { _, isChecked ->
                ref.child(tugas.id!!).child("selesai").setValue(isChecked)
            }

            binding.btnHapus.setOnClickListener {
                ref.child(tugas.id!!).removeValue()
            }

            binding.root.alpha = if (tugas.selesai) 0.5f else 1f

            binding.btnHapus.setOnClickListener {
                ref.child(tugas.id!!).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(
                            binding.root.context,
                            "Tugas berhasil dihapus",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTugasBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}