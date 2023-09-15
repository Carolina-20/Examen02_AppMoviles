package com.example.examenapp.crud

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.examenapp.R
import com.example.examenapp.datos.DatosMarcas
import com.example.examenapp.modelos.ModeloAutos
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O)
class ModeloActv : AppCompatActivity() {
    val db = Firebase.firestore
    private val modelos = ArrayList<ModeloAutos>()
    var selectedMarcaId = ""
    var selectedItemId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modeloautos_actv)

        selectedMarcaId = intent.getStringExtra("id") ?: ""
        println("marcaId: $selectedMarcaId")

        // Buttons and Listeners
        val goBackButton = findViewById<ImageButton>(
            R.id.btn_go_back_to_marca
        )

        goBackButton.setOnClickListener {
            finish()
        }

        val createSeriesButton = findViewById<Button>(
            R.id.btn_create_modeloAutos
        )

        createSeriesButton.setOnClickListener {
            goToActivity(CrearModeloActv::class.java, Bundle().apply {
                val marcaId = intent.getStringExtra("id") ?: ""
                val nombreMarca =  intent.getStringExtra("nombre") ?: ""
                println(nombreMarca)
                if (marcaId.isEmpty() || nombreMarca.isEmpty()) {
                    putString("marcaId", marcaId)
                    putString("nombreMarca", nombreMarca)
                }
            })
        }
    }
    override fun onStart() {
        super.onStart()

        //loadModeloAutos(selectedMarcaId)
    }

    override fun onResume() {
        super.onResume()
        loadModeloAutos(selectedMarcaId)
    }
    private fun goToActivity(activity: Class<*>, params: Bundle? = null) {
        val intent = Intent(this, activity)
        if (params != null) {
            intent.putExtras(params)
        }
        startActivity(intent)
    }


    private fun loadModeloAutos(marcaId: String) {
        if (marcaId != "") {
            val nombreMarca = intent.getStringExtra("nombre") ?: ""
            db.collection("modelos")
                .whereEqualTo("marcaId", marcaId)
                .get()
                .addOnSuccessListener {
                    for (modelo in it) {
                        val id = modelo.id
                        val nombre = modelo.data["nombre"] as String
                        val precio = modelo.data["precio"] as String
                        val fuerzaMotor = modelo.data["fuerzaMotor"] as String
                        val unidadesDisponibles = modelo.data["unidadesDisponibles"] as String
                        val esSedan = modelo.data["esSedan"] as String
                        val fechaLanzamiento = modelo.data["fechaLanzamiento"] as String
                        val marcaId = modelo.data["marcaId"] as String

                        val modeloAuto = ModeloAutos(
                            id,
                            nombre, precio.toDouble(),
                            fuerzaMotor. toDouble(),
                            unidadesDisponibles.toInt(),
                            esSedan.toBoolean(),
                            fechaLanzamiento,
                            marcaId
                        )

                        modelos.add(modeloAuto)
                    }

                    val mrNombre = findViewById<TextView>(
                        R.id.mr_marca
                    )

                    mrNombre.text = nombreMarca

                    val modeloAutosList = findViewById<ListView>(
                        R.id.lv_modeloAutos
                    )

                    val adapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
                        modelos
                    )

                    modeloAutosList.adapter = adapter

                    adapter.notifyDataSetChanged()

                    registerForContextMenu(modeloAutosList)

                }
        }
    }

    private fun showConfirmDialog(modeloAutos: ModeloAutos) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Se elimnara el modelo: ${modeloAutos.getNombreMod()}")
        builder.setMessage("Este se eliminara definitivamente")
        builder.setPositiveButton("Acepto") { dialog, which ->
            modelos.removeAt(selectedItemId)
            db.collection("marcas")
                .document(modeloAutos.getId())
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_SHORT).show()
                    loadModeloAutos(selectedMarcaId)
                }

        }
        builder.setNegativeButton("No", null)
        builder.show()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val inflater = menuInflater
        inflater.inflate(
            R.menu.menu_modeloautos,
            menu
        )

        // position
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val position = info.position

        selectedItemId = position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.mi_edit_modeloAutos -> {
                val modeloAutos = modelos[selectedItemId]
                val nombreMarca = intent.getStringExtra("nombre") ?: ""
                goToActivity(
                    ModificarModeloActv::class.java,
                    Bundle().apply {
                        putString("marcaId", selectedMarcaId)
                        putString("marcaNombre", nombreMarca)
                        putString("modeloAutoId", modeloAutos.getId())
                        putString("modeloAutoNombreMod", modeloAutos.getNombreMod())
                        putDouble("modeloPrecio", modeloAutos.getPrecio())
                        putDouble("modeloFuerzaMotor", modeloAutos.getFuerzaMotor())
                        putBoolean("modeloEsSedan", modeloAutos.getEsSedan())
                        putInt("modeloUnidadesDisponibles", modeloAutos.getUnidadesDisponibles())
                        putString("modeloFechaLanzamiento", modeloAutos.getFechaLanzamiento().toString())
                    }
                )
                true
            }
            R.id.mi_delete_modeloAutos -> {
                val modelo = modelos[selectedItemId]
                showConfirmDialog(modelo)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }


}