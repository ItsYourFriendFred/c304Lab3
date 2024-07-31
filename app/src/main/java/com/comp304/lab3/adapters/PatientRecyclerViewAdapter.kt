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
import com.comp304.lab3.data.model.Patient
import com.comp304.lab3.util.Constants

internal class PatientRecyclerViewAdapter(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
    private val onItemClick: (Patient) -> Unit
) : RecyclerView.Adapter<PatientRecyclerViewAdapter.MyViewHolder>() {

    private var patients: List<Patient> = listOf()
    val nurseId = sharedPreferences.getInt(Constants.SHARED_PREF_KEY, 0)

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val firstNameTextView: TextView = itemView.findViewById(R.id.textViewFirstName)
        private val lastNameTextView: TextView = itemView.findViewById(R.id.textViewLastName)
        private val roomNumberTextView: TextView = itemView.findViewById(R.id.textViewRoomNumber)
        private val cardView: CardView = itemView.findViewById(R.id.cardPatient)

        // Bind the retrieved Patient fields to their corresponding UI components in the RecyclerView's card
        // And set an OnClick listener on each card to pass that Patient's information to the Patient Activity
        fun bind(patient: Patient) {
            firstNameTextView.text = patient.firstname
            lastNameTextView.text = patient.lastname
            roomNumberTextView.text = (context.getString(
                R.string.recyclerView_room_label,
                patient.room.toString()
            ))

            // Set onClick listener on the CardView to handle item clicks
            // When card is clicked, callback invoked with the clicked patient
            // Lambda function is defined in the activity where it's passed to this RecyclerViewAdapter
            cardView.setOnClickListener {
                onItemClick(patient)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_view_patients, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = patients.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val patient = patients[position]
        holder.bind(patient)
    }

    // Update the adapter's data source with the latest Patients and refresh the RecyclerView to reflect latest data
    fun updateList(newPatients: List<Patient>) {
        patients = newPatients
        notifyDataSetChanged()
    }

}