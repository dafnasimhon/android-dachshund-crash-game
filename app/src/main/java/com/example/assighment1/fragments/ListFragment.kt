package com.example.assighment1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.assighment1.R
import com.example.assighment1.interfaces.Callback_HighScoreClicked
import com.example.assighment1.logic.RecordListManager
import java.text.SimpleDateFormat
import java.util.*

class ListFragment : Fragment() {

    private lateinit var list_TL_records: TableLayout
    var highScoreItemClicked: Callback_HighScoreClicked? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_list, container, false)
        findViews(v)
        initViews()
        return v
    }

    private fun findViews(v: View) {
        list_TL_records = v.findViewById(R.id.list_TL_records)
    }

    private fun initViews() {
        val records = RecordListManager.getInstance().recordList.records
        val sdf = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())


        records.forEachIndexed { index, record ->
            val row = TableRow(context)
            row.setPadding(0, 20, 0, 20)

            val txtRank = TextView(context).apply { text = "${index + 1}." }
            val txtScore = TextView(context).apply {
                text = record.score.toString()
                setTypeface(null, android.graphics.Typeface.BOLD)
            }
            val txtDate = TextView(context).apply { text = sdf.format(Date(record.date)) }

            row.addView(txtRank)
            row.addView(txtScore)
            row.addView(txtDate)

            row.setOnClickListener {
                highScoreItemClicked?.highScoreItemClicked(record.lat, record.lon)
            }

            list_TL_records.addView(row)
        }
    }
}