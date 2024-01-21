package com.tesji.citamedica.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.tesji.citamedica.R
import com.tesji.citamedica.databinding.ActivityRegistrerBinding
import com.tesji.citamedica.io.ApiService
import com.tesji.citamedica.io.response.LoginResponse
import com.tesji.citamedica.util.PreferenceHelper
import com.tesji.citamedica.util.PreferenceHelper.set
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrerBinding

    val apiService by lazy {
        ApiService.create()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnGoToLogin.setOnClickListener {
            goToLogin()
        }
        binding.btnConfirmRegister.setOnClickListener {
            performRegister()
        }
    }

    private fun goToLogin(){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    private fun performRegister(){

        val name = binding.etRegisterName.text.toString().trim()
        val email = binding.etRegisterEmail.text.toString().trim()
        val password = binding.etRegisterPassword.text.toString()
        val passwordConfirmation = binding.etRegisterPasswordConfirmation.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty()){
            Toast.makeText(this, "Debe de completar todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != passwordConfirmation){
            Toast.makeText(this, "Las contraseñas ingresadas no son iguales", Toast.LENGTH_SHORT).show()
            return
        }

        val call = apiService.postRegister(name, email, password, passwordConfirmation)
        call.enqueue(object : Callback<LoginResponse>{
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
                        Toast.makeText(applicationContext,"Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(applicationContext,"El correo ya existe en la platarforma", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@RegistrerActivity, "ERROR: No se pudo registrar", Toast.LENGTH_SHORT).show()
            }

        })


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

}