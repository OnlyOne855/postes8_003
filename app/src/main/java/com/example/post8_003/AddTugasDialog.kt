package com.example.post8_003

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.post8_003.databinding.UploadDialogBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database

class AddTugasDialog(
    private val context: Context,
    private val tugasRef: DatabaseReference
) {

    fun show() {
        val binding = UploadDialogBinding.inflate(LayoutInflater.from(context))

        binding.editTanggal.setOnClickListener {
            val calendar = Calendar.getInstance()

            val datePicker = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val selectedCal = Calendar.getInstance()
                    selectedCal.set(year, month, dayOfMonth)

                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    binding.editTanggal.setText(sdf.format(selectedCal.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePicker.datePicker.minDate = calendar.timeInMillis
            datePicker.show()
        }

        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle("Tambah Jadwal Tugas")
            .setView(binding.root)
            .setPositiveButton("Tambah", null) //
            .setNegativeButton("Batal") { d, _ -> d.dismiss() }
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

            val tugas = binding.editTextTugas.text.toString().trim()
            val deskripsi = binding.editTextDeskripsi.text.toString().trim()
            val tanggal = binding.editTanggal.text.toString().trim()

            if (tugas.isEmpty() || tanggal.isEmpty()) {
                Toast.makeText(
                    context,
                    "Isi semua data!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                saveDataToFirebase(tugas, deskripsi, tanggal)
                dialog.dismiss()
            }
        }
    }

    private fun saveDataToFirebase(
        tugas: String,
        deskripsi: String,
        tanggal: String
    ) {
        val id = tugasRef.push().key ?: return

        val data = Tugas(
            id = id,
            tugas = tugas,
            deskripsi = deskripsi,
            tanggal = tanggal,
            selesai = false
        )

        tugasRef.child(id).setValue(data)
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "Tugas ditambahkan",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    "Gagal menambahkan tugas",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

}