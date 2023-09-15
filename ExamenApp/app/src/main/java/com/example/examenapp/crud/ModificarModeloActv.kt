package com.example.examenapp.crud

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.examenapp.R
import com.example.examenapp.datos.DatosMarcas
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class ModificarModeloActv : AppCompatActivity() {

    private val db = Firebase.firestore
    private val spinnerValues = arrayListOf<String>("Si", "No")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modificar_modeloautos_actv)

        loadDataInEditText(intent)

        val goBackButton = findViewById<ImageButton>(
            R.id.ib_go_back_update_modeloAutos_to_modeloAutos_list
        )

        goBackButton.setOnClickListener {
            finish()
        }

        val saveUpdatedData = findViewById<Button>(
            R.id.btn_update_modeloAutos
        )

        saveUpdatedData.setOnClickListener {
            updatemodeloAutos()
            finish()
        }
    }

    private fun updatemodeloAutos() {
        val inputNombreMod = findViewById<EditText>(R.id.pt_modeloAutos_update_nombreMod)
        val inputPrecio = findViewById<EditText>(R.id.pt_modeloAutos_update_precio)
        val inputFuerzaMotor = findViewById<EditText>(R.id.pt_modeloAutos_update_fuerzaMotor)
        val inputUnidadesDisponibles = findViewById<EditText>(R.id.pt_modeloAutos_update_unidadesDisponibles)
        val inputFechaLanzamiento = findViewById<EditText>(R.id.pt_modeloAutos_update_fechaLanzamiento)
        val spinnerIsSedan = findViewById<Spinner>(R.id.spinner_is_sedan_modeloAutos)
        val inputMarca = findViewById<EditText>(R.id.pt_marca_nombre)

        val modeloAutosNombreMod = inputNombreMod.text.toString()
        val modeloAutosPrecio = inputPrecio.text.toString().toDouble()
        val modeloAutosFuerzaMotor= inputFuerzaMotor.text.toString().toDouble()
        val modeloAutosUnidadesDisponibles = inputUnidadesDisponibles.text.toString().toInt()
        val modeloAutosFechaLanzamiento = inputFechaLanzamiento.text.toString()
        val modeloAutosIsSedan = spinnerIsSedan.selectedItem.toString() == "Si"
        val marcaNombre = inputMarca.text.toString()

        val marcaId = intent.getStringExtra("marcaId")?: ""
        val modeloAutosId = intent.getStringExtra("modeloAutosId")

        val modeloActualizado = hashMapOf(
            "nombre" to modeloAutosNombreMod,
            "precio" to modeloAutosPrecio,
            "fuerzaMotor" to modeloAutosFuerzaMotor,
            "unidadesDisponibles" to modeloAutosUnidadesDisponibles,
            "esSedan" to modeloAutosIsSedan,
            "fechaLanzamiento" to modeloAutosFechaLanzamiento,
            "marcaId" to marcaId
        )

        if (!marcaId.isEmpty()) {
            db.collection("modelos")
                .document(marcaId)
                .set(modeloActualizado)
                .addOnSuccessListener {
                    Toast.makeText(this, "Modelo actualizado correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
    }

    private fun loadDataInEditText(intent: Intent) {
        val marcaId = intent.getStringExtra("marcaId")
        val marcaNombre = intent.getStringExtra("marcaNombre")
        val modeloAutosId = intent.getStringExtra("modeloAutosId")
        val modeloAutosNombreMod = intent.getStringExtra("modeloAutosNombreMod")
        val modeloAutosPrecio = intent.getDoubleExtra("modeloAutosPrecio", 0.0)
        val modeloAutosFuerzaMotor = intent.getDoubleExtra("modeloAutosFuerzaMotor", 0.0)
        val modeloAutosEsSedan = intent.getBooleanExtra("modeloAutosEsSedan", false)
        val modeloAutosUnidadesDisponibles= intent.getIntExtra("modeloAutosUnidadesDisponibles", 0)
        val modeloAutosFechaLanzamiento = intent.getStringExtra("modeloAutosFechaLanzamiento")

        val inputNombreMod = findViewById<EditText>(R.id.pt_modeloAutos_update_nombreMod)
        val inputPrecio = findViewById<EditText>(R.id.pt_modeloAutos_update_precio)
        val inputFuerzaMotor = findViewById<EditText>(R.id.pt_modeloAutos_update_fuerzaMotor)
        val inputUnidadesDisponibles = findViewById<EditText>(R.id.pt_modeloAutos_update_unidadesDisponibles)
        val inputFechaLanzamiento = findViewById<EditText>(R.id.pt_modeloAutos_update_fechaLanzamiento)
        loadSpinner(modeloAutosEsSedan)
        val spinnerIsSedan = findViewById<Spinner>(
            R.id.spinner_is_sedan_modeloAutos
        )
        val inputMarca= findViewById<EditText>(R.id.pt_marca_nombre)

        inputNombreMod.setText(modeloAutosNombreMod)
        inputPrecio.setText(modeloAutosPrecio.toString())
        inputFuerzaMotor.setText(modeloAutosFuerzaMotor.toString())
        inputUnidadesDisponibles.setText(modeloAutosUnidadesDisponibles.toString())
        inputFechaLanzamiento.setText(modeloAutosFechaLanzamiento)
        inputMarca.setText(marcaNombre)
    }

    private fun loadSpinner(value: Boolean) {
        val spinnerIsSedan = findViewById<Spinner>(
            R.id.spinner_is_sedan_modeloAutos
        )

        val arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            spinnerValues
        )

        arrayAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerIsSedan.adapter = arrayAdapter
        val selection = if (value) 0 else 1
        spinnerIsSedan.setSelection(selection)
    }




}