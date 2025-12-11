package com.example.pert4_post_003

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pert4_post_003.*
import com.example.pert4_post_003.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DatabaseKTP
    private lateinit var appExecutor: AppExecutor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appExecutor = AppExecutor()
        db = DatabaseKTP.getDatabase(this)

        val statusList = arrayOf("Belum Menikah", "Sudah Menikah", "Cerai", "Single")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatus.adapter = adapter

        tampilkanData()

        binding.btnSimpan.setOnClickListener { simpanData() }
        binding.btnReset.setOnClickListener { resetData() }
    }

    private fun tampilkanData() {
        appExecutor.diskIO.execute {
            val data = db.ktpdao().getAllSync()
            val text = if (data.isEmpty()) {
                "Belum ada data warga yang tersimpan."
            } else {
                buildString {
                    data.forEachIndexed { index, item ->
                        append("${index + 1}. ${item.nama} (${item.gender}) - ${item.status}\n")
                        append("NIK: ${item.nik}\n")
                        append("Alamat: RT ${item.rt}/RW ${item.rw}, ${item.desa}, ${item.kecamatan}, ${item.kabupaten}\n\n")
                    }
                }
            }
            appExecutor.mainThread.execute {
                binding.tvListData.text = text.trim()
            }
        }
    }

    private fun simpanData() {
        val nama = binding.etNama.text.toString().trim()
        val nik = binding.etNIK.text.toString().trim()
        val kabupaten = binding.etKabupaten.text.toString().trim()
        val kecamatan = binding.etKecamatan.text.toString().trim()
        val desa = binding.etDesa.text.toString().trim()
        val rt = binding.etRT.text.toString().trim()
        val rw = binding.etRW.text.toString().trim()
        val selectedGenderId = binding.radioGender.checkedRadioButtonId
        val gender = if (selectedGenderId != -1) findViewById<RadioButton>(selectedGenderId).text.toString() else ""
        val status = binding.spinnerStatus.selectedItem.toString()

        if (nama.isEmpty() || nik.isEmpty() || kabupaten.isEmpty() || kecamatan.isEmpty() ||
            desa.isEmpty() || rt.isEmpty() || rw.isEmpty() || gender.isEmpty()
        ) {
            Toast.makeText(this, " Semua data harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        val ktp = KTP(
            nama = nama,
            nik = nik,
            kabupaten = kabupaten,
            kecamatan = kecamatan,
            desa = desa,
            rt = rt,
            rw = rw,
            gender = gender,
            status = status
        )

        appExecutor.diskIO.execute {
            db.ktpdao().insert(ktp)
            appExecutor.mainThread.execute {
                Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                clearForm()
                tampilkanData()
            }
        }
    }

    private fun resetData() {
        appExecutor.diskIO.execute {
            db.ktpdao().deleteAll()
            appExecutor.mainThread.execute {
                clearForm()
                tampilkanData()
                Toast.makeText(this, "Semua data berhasil dihapus!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearForm() {
        binding.etNama.text.clear()
        binding.etNIK.text.clear()
        binding.etKabupaten.text.clear()
        binding.etKecamatan.text.clear()
        binding.etDesa.text.clear()
        binding.etRT.text.clear()
        binding.etRW.text.clear()
        binding.radioGender.clearCheck()
        binding.spinnerStatus.setSelection(0)
    }
}