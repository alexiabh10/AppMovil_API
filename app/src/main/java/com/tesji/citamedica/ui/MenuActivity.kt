package com.tesji.citamedica.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.tesji.citamedica.R
import com.tesji.citamedica.io.ApiService
import com.tesji.citamedica.util.PreferenceHelper
import com.tesji.citamedica.util.PreferenceHelper.set
import com.tesji.citamedica.util.PreferenceHelper.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuActivity : AppCompatActivity() {

    private val apiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val btnLogout = findViewById<Button>(R.id.btn_logout)
        val btnReservarCita = findViewById<Button>(R.id.btn_reservar_Cita)
        val btnMisCitas = findViewById<Button>(R.id.btn_mis_citas)

        btnLogout.setOnClickListener {
            performLogout()

        }

        btnReservarCita.setOnClickListener {
            goToCreateAppointment()
        }

        btnMisCitas.setOnClickListener {
            goToAppointments()
        }

    }

    private fun goToAppointments(){
        val i = Intent(this, AppointmentsActivity::class.java)
        startActivity(i)
    }

    private fun goToCreateAppointment(){
        val i = Intent(this, CreateAppointmentActivity::class.java)
        startActivity(i)

    }

    private fun goToLogin(){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun performLogout(){
        val jwt = preferences["jwt", ""]
        val call = apiService.postLogout("Bearer $jwt")
        call.enqueue(object: Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
               clearSesionPreference()
                goToLogin()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun clearSesionPreference(){
        preferences["jwt"] = ""
    }
}