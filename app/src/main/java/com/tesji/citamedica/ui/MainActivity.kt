package com.tesji.citamedica.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.tesji.citamedica.R
import com.tesji.citamedica.io.ApiService
import com.tesji.citamedica.io.response.LoginResponse
import com.tesji.citamedica.util.PreferenceHelper
import com.tesji.citamedica.util.PreferenceHelper.get
import com.tesji.citamedica.util.PreferenceHelper.set
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = PreferenceHelper.defaultPrefs(this)
        if (preferences["jwt", ""].contains("."))
            goToMenu()

        val tvGoRegister = findViewById<Button>(R.id.btnGoToRegister)
        val btnGoMenu = findViewById<Button>(R.id.btn_Go_To_Menu)

        tvGoRegister.setOnClickListener{
            goToRegister()
        }

        btnGoMenu.setOnClickListener {
            performLogin()
        }
    }

    private fun goToRegister(){
        val i = Intent(this, RegistrerActivity::class.java)
        startActivity(i)
    }

    private fun goToMenu(){
        val i = Intent(this, MenuActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun createSesionPreferences(jwt: String){
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["jwt"] = jwt

    }

    private fun performLogin(){
        val etEmail = findViewById<EditText>(R.id.et_email).text.toString()
        val etPassword = findViewById<EditText>(R.id.et_password).text.toString()

        if (etEmail.trim().isEmpty() || etPassword.trim().isEmpty()){
            Toast.makeText(applicationContext, "Debes ingresar tu Correo y Contraseña", Toast.LENGTH_SHORT).show()
            return
        }


        val call = apiService.postLogin(etEmail, etPassword)
        call.enqueue(object: retrofit2.Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val loginResponse = response.body()
                    if (loginResponse == null){
                        Toast.makeText(applicationContext,"Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (loginResponse.success){
                        createSesionPreferences(loginResponse.jwt)
                        goToMenu()
                    }else{
                        Toast.makeText(applicationContext,"Las credenciales son incorrectas", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(applicationContext,"Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(applicationContext,"Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
            }

        })
    }

}