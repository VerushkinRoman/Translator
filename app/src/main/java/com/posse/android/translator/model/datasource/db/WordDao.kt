package com.posse.android.translator.model.datasource.db

import androidx.room.*

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(word: RoomDataModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg word: RoomDataModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(words: List<RoomDataModel>)

    @Update
    fun update(word: RoomDataModel)

    @Update
    fun update(vararg word: RoomDataModel)

    @Update
    fun update(words: List<RoomDataModel>)

    @Delete
    fun delete(word: RoomDataModel)

    @Delete
    fun delete(vararg word: RoomDataModel)

    @Delete
    fun delete(words: List<RoomDataModel>)

    @Query("SELECT * FROM RoomDataModel")
    fun getAll(): List<RoomDataModel>

    @Query("SELECT * FROM RoomDataModel WHERE word = :word")
    fun getByWord(word: String): List<RoomDataModel>
}