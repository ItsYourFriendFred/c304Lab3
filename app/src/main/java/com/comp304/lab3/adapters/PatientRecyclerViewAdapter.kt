package com.comp304.lab3.adapters

import android.content.Context
import android.content.Intent
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
import com.comp304.lab3.views.PatientActivity

internal class PatientRecyclerViewAdapter(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
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

            cardView.setOnClickListener {
                val intent = Intent(context, PatientActivity::class.java).apply {
                    putExtra("EXTRA_PATIENT_ID", patient.patientId)
                    putExtra("EXTRA_FIRST_NAME", patient.firstname)
                    putExtra("EXTRA_LAST_NAME", patient.lastname)
                    putExtra("EXTRA_ROOM_NUMBER", patient.room)
                    putExtra("EXTRA_NURSE_ID", patient.nurseId)
                }
                context.startActivity(intent)
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

    fun submitList(newPatients: List<Patient>) {
        patients = newPatients
        notifyDataSetChanged()
    }

}