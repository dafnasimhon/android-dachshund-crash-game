package com.example.assighment1.logic

import android.content.Context
import com.example.assighment1.utilities.GameConstants
import com.example.assighment1.utilities.SharedPreferencesManager
import com.google.gson.Gson

class RecordListManager private constructor(context: Context) {

    var recordList: RecordList = RecordList()

    init {
        loadFromSP()
    }

    companion object {
        @Volatile
        private var instance: RecordListManager? = null

        fun init(context: Context): RecordListManager {
            return instance ?: synchronized(this) {
                instance ?: RecordListManager(context).also { instance = it }
            }
        }

        fun getInstance(): RecordListManager {
            return instance ?: throw IllegalStateException(
                "RecordListManager must be initialized by calling init(context) before use."
            )
        }
    }

    fun addRecord(score: Int, lat: Double = 0.0, lon: Double = 0.0) {
        val newRecord = Record(score, lat, lon)
        recordList.records.add(newRecord)

        recordList.records.sortByDescending { it.score }

        if (recordList.records.size > 10) {
            recordList.records.removeAt(10)
        }

        saveToSP()
    }

    private fun saveToSP() {
        val json = Gson().toJson(recordList)
        SharedPreferencesManager.getInstance().putString(GameConstants.SP_KEYS.RECORDS_KEY, json)
    }

    private fun loadFromSP() {
        val json = SharedPreferencesManager.getInstance().getString(GameConstants.SP_KEYS.RECORDS_KEY, "")
        if (json.isNotEmpty()) {
            recordList = Gson().fromJson(json, RecordList::class.java)
        }
    }
}