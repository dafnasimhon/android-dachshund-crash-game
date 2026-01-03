package com.example.assighment1.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
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

        val darkBlue = ContextCompat.getColor(requireContext(), R.color.sky_blue_dark_text)
        val rowTextColor = Color.parseColor("#444444") // אפור כהה לנתונים

        records.forEachIndexed { index, record ->
            val row = TableRow(context).apply {
                setPadding(0, 15, 0, 15)
                background = ContextCompat.getDrawable(requireContext(), android.R.drawable.list_selector_background)
            }

            // 1. מספר סידורי (#)
            val txtRank = TextView(context).apply {
                text = "${index + 1}."
                setTextColor(darkBlue)
                textSize = 16f
                setPadding(10, 0, 0, 0)
            }

            val txtScore = TextView(context).apply {
                text = record.score.toString()
                setTextColor(rowTextColor)
                textSize = 18f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                gravity = Gravity.CENTER
            }


            val txtDate = TextView(context).apply {
                text = sdf.format(Date(record.date))
                setTextColor(rowTextColor)
                textSize = 14f
                gravity = Gravity.END
                setPadding(0, 0, 10, 0)
            }


            row.addView(txtRank)
            row.addView(txtScore)
            row.addView(txtDate)


            row.setOnClickListener {
                highScoreItemClicked?.highScoreItemClicked(record.lat, record.lon)
            }


            list_TL_records.addView(row)


            val divider = View(context).apply {
                layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1)
                setBackgroundColor(Color.parseColor("#DDDDDD"))
            }
            list_TL_records.addView(divider)
        }
    }
}