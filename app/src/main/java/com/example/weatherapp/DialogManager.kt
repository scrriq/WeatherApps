package com.example.weatherapp

import android.content.Context
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

object DialogManager {
    fun locationSettingsDialog(context: Context, listner: Listner){
        val builder = AlertDialog.Builder(context)
        val dialog =  builder.create()
        dialog.setTitle("Enable location?")
        dialog.setMessage("Location disabled, do you want enable location?")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok"){
            _, _ ->
            listner.onClick(null)
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel"){
            _, _ -> dialog.dismiss()
        }
        dialog.show()
    }
//    fun searchByNameDialog(context: Context, listner: Listner){
//        val builder = AlertDialog.Builder(context)
//        val edName = EditText(context)
//        builder.setView(edName)
//        val dialog =  builder.create()
//        dialog.setTitle("City name:")
//        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok"){ _, _ ->
//            listner.onClick(edName.text.toString())
//            dialog.dismiss()
//        }
//        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel"){
//                _, _ -> dialog.dismiss()
//        }
//        dialog.show()
//    }
    interface Listner{
        fun onClick(name: String?)
    }
}