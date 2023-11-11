package com.example.proofbz.Helper

import android.app.AlertDialog
import android.app.Dialog

import android.content.Context
import android.content.DialogInterface

import android.view.KeyEvent
import android.view.View
import android.widget.TextView

import android.widget.Toast
import com.example.proofbz.Main.MainActivity
import com.example.proofbz.R

class HelperDialog {
    var context: Context


    constructor(context: Context) {
        this.context = context
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun dialogCerrarSesion() {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
        builder.setTitle(R.string.cerrarSesion)
        builder.setMessage(R.string.seguroQueDesearCerrarSesion)
            .setPositiveButton(
                R.string.aceptar
            ) { _: DialogInterface?, _: Int ->
                (context as MainActivity?)?.logOut()
            }
            .setNegativeButton(R.string.cancelar) { _: DialogInterface?, _: Int ->
            }
        builder.create()
        var dialogo = builder.show()
        dialogo.setCanceledOnTouchOutside(false)
        dialogo.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event -> // Prevent dialog close on back press button
            keyCode == KeyEvent.KEYCODE_BACK
        })
    }

    fun dialogMensaje(titulo: String, mensaje: String) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_message)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        val textViewTitulo = dialog.findViewById<TextView>(R.id.textViewTituloMensajeDialog)
        val textViewMensaje = dialog.findViewById<TextView>(R.id.textViewMensajeDialog)
        textViewMensaje.text = mensaje
        textViewTitulo.text = titulo
        dialog.findViewById<View>(R.id.textViewAceptarDialog)
            .setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


}