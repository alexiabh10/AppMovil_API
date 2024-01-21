package com.tesji.citamedica.ui

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tesji.citamedica.R
import com.tesji.citamedica.model.Appointment
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class AppointmentAdapter
    :RecyclerView.Adapter<AppointmentAdapter.ViewHolder>(){

    var appointments = ArrayList<Appointment>()
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val tvAppointmentId = itemView.findViewById<TextView>(R.id.tv_id)
        val tvDoctorName = itemView.findViewById<TextView>(R.id.tv_medico)
        val tvScheduledDate = itemView.findViewById<TextView>(R.id.tv_fecha)
        val tvScheduledTime = itemView.findViewById<TextView>(R.id.tv_hora)

        val tvSpecialty = itemView.findViewById<TextView>(R.id.tv_especialidad)
        val tvDescription = itemView.findViewById<TextView>(R.id.tv_descripcion)
        val tvStatus = itemView.findViewById<TextView>(R.id.tv_status)
        val tvType = itemView.findViewById<TextView>(R.id.tv_tipo)
        val tvCreateAt = itemView.findViewById<TextView>(R.id.tv_creado_en)

        val ibExpand = itemView.findViewById<ImageButton>(R.id.ib_expand)
        val linearLayoutDetails = itemView.findViewById<LinearLayout>(R.id.linear_layout_detalles)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_appointment,parent, false)
        )
    }

    override fun getItemCount() = appointments.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment = appointments[position]

        holder.tvAppointmentId.text = "Cita Médica No.  ${appointment.id}"
        holder.tvDoctorName.text = appointment.doctor.name
        holder.tvScheduledDate.text = "Atención el día: ${appointment.scheduledDate}"
        holder.tvScheduledTime.text = "A las: ${appointment.scheduledTime}"

        holder.tvSpecialty.text = appointment.specialty.name
        holder.tvDescription.text = appointment.description
        holder.tvStatus.text = appointment.status
        holder.tvType.text = appointment.type

        val fecha = appointment.createdAt
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val dateString = OffsetDateTime.parse(fecha)
        val formatFecha: String = dateString.format(dateFormatter)


        holder.tvCreateAt.text = "La cita se registro el día: ${formatFecha} con los siguientes síntomas:"

        holder.ibExpand.setOnClickListener{

            TransitionManager.beginDelayedTransition(holder.linearLayoutDetails as ViewGroup, AutoTransition())
            if (holder.linearLayoutDetails.visibility == View.VISIBLE){
                holder.linearLayoutDetails.visibility = View.GONE
                holder.ibExpand.setImageResource(R.drawable.expand_more)
            }else {
                holder.linearLayoutDetails.visibility = View.VISIBLE
                holder.ibExpand.setImageResource(R.drawable.expand_less)
            }
        }
    }
}