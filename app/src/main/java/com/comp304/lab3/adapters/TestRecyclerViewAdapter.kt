package com.comp304.lab3.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.comp304.lab3.R
import com.comp304.lab3.data.model.Test
import com.comp304.lab3.util.Constants

class TestRecyclerViewAdapter(private val context: Context,
                              private val sharedPreferences: SharedPreferences,
) : RecyclerView.Adapter<TestRecyclerViewAdapter.MyViewHolder>() {

    private var tests: List<Test> = listOf()
    val nurseId = sharedPreferences.getInt(Constants.SHARED_PREF_KEY, 0)

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val testIdTextView: TextView = itemView.findViewById(R.id.textViewTestId)
        private val nurseIdTextView: TextView = itemView.findViewById(R.id.textViewNurseId)
        private val bloodPressureTextView: TextView = itemView.findViewById(R.id.textViewBloodPressure)
        private val temperatureTextView: TextView = itemView.findViewById(R.id.textViewTemperature)
        private val fpgTextView: TextView = itemView.findViewById(R.id.textViewFPG)
        private val rbcCountTextView: TextView = itemView.findViewById(R.id.textViewRbcCount)
        private val heartRateTextView: TextView = itemView.findViewById(R.id.textViewHeartRate)
        private val cardView: CardView = itemView.findViewById(R.id.cardTest)

        // Bind the retrieved Test fields to their corresponding UI components in the RecyclerView's card
        // And set an OnClick listener on each card to pass that Test's information to the ViewTestInfo Activity
        fun bind(test: Test) {
            testIdTextView.text = (context.getString(
                R.string.recyclerView_test_label,
                test.testId.toString()
            ))
            nurseIdTextView.text = (context.getString(
                R.string.recyclerView_nurse_label,
                test.nurseId.toString()
            ))
            bloodPressureTextView.text = (context.getString(
                R.string.recyclerView_blood_pressure_label,
                test.BPH.toString(), test.BPL.toString()
            ))
            temperatureTextView.text = (context.getString(
                R.string.recyclerView_temperature_label,
                test.temperature.toString()
            ))
            heartRateTextView.text = (context.getString(
                R.string.recyclerView_heart_rate_label,
                test.heartRate.toString()
            ))
            rbcCountTextView.text = (context.getString(
                R.string.recyclerView_blood_count_label,
                test.rbcCount.toString()
            ))
            fpgTextView.text = (context.getString(
                R.string.recyclerView_glucose_label,
                test.FPG.toString()
            ))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_view_tests, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = tests.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val test = tests[position]
        holder.bind(test)
    }

    // Update the adapter's data source with the latest Tests and refresh the RecyclerView to reflect latest data
    fun updateList(newTests: List<Test>) {
        tests = newTests
        notifyDataSetChanged()
    }

}