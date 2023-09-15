package com.example.examenapp.modelos

import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
class Marca(
    private var id: String,
    private var nombre: String,
    private var pais: String,
    private var ciudad: String,
    private var concesionarios: String
)
{
    constructor(): this("", "", "", "","")

    public fun getId(): String {
        return id
    }

    fun getNombre(): String {
        return nombre
    }

    fun setNombre(nombre: String) {
        this.nombre = nombre
    }

    fun getPais(): String {
        return pais
    }

    fun setPais(pais: String) {
        this.pais = pais
    }

    fun getCiudad(): String {
        return ciudad
    }

    fun setCiudad(ciudad: String) {
        this.ciudad = ciudad
    }

    fun getConcesionarios(): String {
        return concesionarios
    }

    fun setConcesionarios(concesionarios: String) {
        this.concesionarios = concesionarios
    }

    override fun toString(): String {
        return "$nombre"

    }

    fun getListaDatos(): List<String>{
        return listOf(
            "Nombre de la marca: $nombre",
            "Pais de origen: $pais",
            "Ciudad en la que se encuentra la matriz: $ciudad",
            "Numero de concesionarios: $concesionarios",
        )
    }
}