package com.example.examenapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.examenapp.crud.CrearMarcaActv
import com.example.examenapp.crud.ModeloActv
import com.example.examenapp.crud.ModificarMarcaActv
import com.example.examenapp.datos.DatosMarcas
import com.example.examenapp.modelos.Marca
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity: AppCompatActivity() {
//    val marcas = DatosMarcas.marcas
    val marcas = ArrayList<Marca>()
    var selectedItemId = 0
    var db = Firebase.firestore;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_actv)

//        loadMarcas()

        val btnCreate = findViewById<Button>(
            R.id.btn_create_marca
        )

        btnCreate.setOnClickListener {
            goToActivity(CrearMarcaActv::class.java)
        }
    }

    override fun onStart() {
        super.onStart()
//        loadMarcas()
    }

    override fun onResume() {
        super.onResume()
        loadMarcas()
    }

    private fun loadMarcas() {
        marcas.clear()
        val listView = findViewById<ListView>(
            R.id.lv_marcas
        )

        db.collection("marcas")
            .get()
            .addOnSuccessListener {
                for (marca in it) {
                    val id = marca.id
                    val nombre = marca.data["nombre"] as String
                    val pais = marca.data["pais"] as String
                    val ciudad = marca.data["ciudad"] as String
                    val concesionarios = marca.data["concesionarios"] as String

                    marcas.add(Marca(id, nombre, pais, ciudad, concesionarios))
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, marcas)
                listView.adapter = adapter
                adapter.notifyDataSetChanged()
                registerForContextMenu(listView)
            }

    }

    private fun goToActivity(activity: Class<*>, params: Bundle? = null) {
        val intent = Intent(this, activity)
        if (params != null) {
            intent.putExtras(params)
        }
        startActivity(intent)
    }

    private fun showConfirmDeleteDialog(marca: Marca, adapter: ArrayAdapter<Marca>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Se eliminara la marca ${marca.getNombre()}?")
        builder.setMessage("Este se eliminará definitivamente")
        builder.setPositiveButton("Aceptar") { _, _ ->
            marcas.removeAt(selectedItemId)

            db.collection("marcas")
                .document(marca.getId())
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_SHORT).show()
                    adapter.notifyDataSetChanged()
                }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val inflater = menuInflater
        inflater.inflate(R.menu.menu_marca, menu)

        //position
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val position = info.position

        selectedItemId = position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.mis_modelos -> {
                "Ver: ${selectedItemId}"
                val params = Bundle()
                val marca = marcas[selectedItemId]
                params.putString("id", marca.getId())
                params.putString("nombre", marca.getNombre())
                params.putString("pais", marca.getPais())
                params.putString("ciudad", marca.getCiudad())
                params.putString("concesionarios", marca.getConcesionarios())

                goToActivity(ModeloActv::class.java, params)
                return true
            }
            R.id.mi_update -> {
                "Modificar: ${selectedItemId}"
                val params = Bundle()
                val marca = marcas[selectedItemId]
                params.putString("id", marca.getId())
                params.putString("nombre", marca.getNombre())
                params.putString("pais", marca.getPais())
                params.putString("ciudad", marca.getCiudad())
                params.putString("concesionarios", marca.getConcesionarios())

                goToActivity(ModificarMarcaActv::class.java, params)
                return true
            }
            R.id.mi_delete -> {
                val marca = marcas[selectedItemId]
                val listView = findViewById<ListView>(
                    R.id.lv_marcas
                )
                showConfirmDeleteDialog(marca, listView.adapter as ArrayAdapter<Marca>)
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}