package com.example.pert4_post_003

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ktp")
data class KTP(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val nik: String,
    val kabupaten: String,
    val kecamatan: String,
    val desa: String,
    val rt: String,
    val rw: String,
    val gender: String,
    val status: String
)