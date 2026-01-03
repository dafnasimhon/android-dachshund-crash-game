package com.example.assighment1.logic

data class Record(
    val score: Int = 0,
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val date: Long = System.currentTimeMillis()
)

data class RecordList(
    val records: MutableList<Record> = mutableListOf()
)