package com.example.pert4_post_003

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Dao
import com.example.pert4_post_003.KTP


@Dao
interface KTPDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ktp : KTP)

    @Query("DELETE FROM ktp")
    fun deleteAll()

    @Query("SELECT * FROM ktp ORDER BY id ASC")
    fun getAllSync(): List<KTP>

    @Query("SELECT * FROM ktp ORDER BY id ASC")
    fun getAllLive(): LiveData<List<KTP>>
}