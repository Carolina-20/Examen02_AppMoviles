package com.example.examenapp.crud

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.examenapp.R
import com.example.examenapp.datos.DatosMarcas
import com.example.examenapp.modelos.Marca
import com.example.examenapp.modelos.ModeloAutos
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class CrearModeloActv : AppCompatActivity() {
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crear_modelo_actv)

        val goBackButton = findViewById<ImageButton>(
            R.id.go_back_to_modelosAutos_list
        )

        goBackButton.setOnClickListener {
            finish()
        }

        val txtMarca = findViewById<EditText>(R.id.pt_modeloAutos_marca)

        val marcaNombre = intent.getStringExtra("nombreMarca")?: ""
        txtMarca.setText(marcaNombre)



        val saveNuevoModeloAutosButton = findViewById<Button>(
            R.id.btn_new_modeloAuto
        )

        val marcaId = intent.getStringExtra("marcaId").toString()

        saveNuevoModeloAutosButton.setOnClickListener {
            createModeloAutos(marcaId)
        }
    }

    private fun createModeloAutos(marcaId: String) {
        val inputNombreMod = findViewById<EditText>(R.id.pt_modeloAutos_nombreMod)
        val inputPrecio = findViewById<EditText>(R.id.pt_modeloAutos_precio)
        val inputFuerzaMotor = findViewById<EditText>(R.id.pt_modeloAutos_fuerzaMotor)
        val inputUnidadesDisponibles = findViewById<EditText>(R.id.pt_modeloAutos_unidadesDisponibles)
        val inputFechaLanzamiento = findViewById<EditText>(R.id.pt_modeloAutos_fechaLanzamiento)


        val nombreMod = inputNombreMod.text.toString()
        val precio = inputPrecio.text.toString().toDouble()
        val fuerzaMotor = inputFuerzaMotor.text.toString().toDouble()
        val unidadesDisponibles = inputUnidadesDisponibles.text.toString().toInt()
        val fechaLanzamiento = inputFechaLanzamiento.text.toString()



        val newModeloAutos = hashMapOf(
            "nombre" to nombreMod,
            "precio" to precio,
            "fuerzaMotor" to fuerzaMotor,
            "unidadesDisponibles" to unidadesDisponibles,
            "esSedan" to false,
            "fechaLanzamiento" to fechaLanzamiento,
            "marcaId" to marcaId
        )

        db.collection("modelos")
            .add(newModeloAutos)
            .addOnSuccessListener {
                Toast.makeText(this, "Nuevo modelo agregado correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }
    }
}