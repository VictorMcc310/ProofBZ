package com.example.proofbz.Main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proofbz.Adapter.AdapterEstudent
import com.example.proofbz.Adapter.SwipeHelper
import com.example.proofbz.DataBase.Entities.StudentEntity
import com.example.proofbz.DataBase.RoomModule
import com.example.proofbz.DataBase.StudentApp
import com.example.proofbz.Helper.HelperDialog
import com.example.proofbz.Models.Estudent
import com.example.proofbz.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    lateinit var name: EditText
    lateinit var secondName: EditText
    lateinit var age: EditText
    lateinit var buttonSave: Button
    lateinit var buttonCancel: Button


    var listEstudent = ArrayList<Estudent>()
    var filtros = ArrayList<Estudent>()
    lateinit var adapter: AdapterEstudent
    lateinit var rv_item: RecyclerView
    lateinit var buscar: EditText
    lateinit var functionAdd: TextView
    lateinit var linearInfo: LinearLayout
    lateinit var toolbar: Toolbar
    var pulsaAdd = false
    var filtrado = false
    var editarBool = false
    var idMod=0
    var ageMode=0

    val roomM = RoomModule

    var context : Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        secondName = findViewById(R.id.secondNameEdit)
        name = findViewById(R.id.nameEdit)
        age = findViewById(R.id.ageEdit)

        buttonSave = findViewById(R.id.buttonSave)

        rv_item = findViewById(R.id.listEstudent)
        buscar = findViewById(R.id.editTextFiltro)

        functionAdd = findViewById(R.id.textViewFunction)
        linearInfo = findViewById(R.id.infoAlumno)
        buttonCancel = findViewById(R.id.buttonCancel)
        functionAdd.setOnClickListener {
            if (!pulsaAdd)
                infoAlumno()
        }
        buttonCancel.setOnClickListener { cancel() }
        buttonSave.setOnClickListener {
        if(editarBool){
            editSave()
        }else{
            save()
        }
        }


        rv_item.layoutManager = LinearLayoutManager(baseContext)
        rv_item.hasFixedSize()
        adapter = AdapterEstudent(listEstudent)
        rv_item.adapter = adapter


        buscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                println(" filter $editable")
                filtro(editable.toString() + "")
            }
        })


        val swipeHelper: SwipeHelper = object : SwipeHelper(this@MainActivity) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: MutableList<UnderlayButton>
            ) {
                underlayButtons.add(UnderlayButton(
                    getString(R.string.delete),
                    0,
                    Color.parseColor("#FF3C30"),
                    object : UnderlayButtonClickListener {
                        override fun onClick(pos: Int) {
                            val item: Estudent = adapter.getData()!![pos]
                            adapter.removeItem(pos)
                            delete(item)
                            val snackbar = Snackbar.make(
                                rv_item,
                                "Alumno eliminado de la lista",
                                Snackbar.LENGTH_LONG
                            )
                            snackbar.setAction(getString(R.string.undo)) {
                                adapter.restoreItem(item, pos)
                                insert(item)
                                rv_item.scrollToPosition(pos)
                            }
                            snackbar.setActionTextColor(Color.YELLOW)
                            snackbar.show()
                        }
                    }
                ))
                underlayButtons.add(UnderlayButton(
                    getString(R.string.edit),
                    0,
                    Color.parseColor("#FF9502"),
                    object : UnderlayButtonClickListener {
                        override fun onClick(pos: Int) {
                            val item: Estudent = adapter.getData()!![pos]
                            if(filtrado){
                               editStudent(pos,filtros)
                            }else{
                                editStudent(pos,listEstudent)
                            }

                            adapter.notifyDataSetChanged()
                        }
                    }
                ))

            }




        }
        swipeHelper.attachToRecyclerView(rv_item)


        //val app = applicationContext as StudentApp

        GlobalScope.launch {
            getSt()
        }
}

    fun editStudent(modifica : Int, lista : ArrayList<Estudent>){

        editInfoAlumno()
        name.setText(lista[modifica].name)
        name.isEnabled =false
        secondName.setText(lista[modifica].secondName)
        secondName.isEnabled =false
        age.setText(lista[modifica].age.toString())
        idMod=modifica
        buttonSave.text = getString(R.string.edit)


    }

     fun insert(student:Estudent){
        roomM.provideRoom(this).getStudentDao().insertAll(StudentEntity(0,student.name+" "+student.secondName,student.age.toString()))
    }
    fun update(student:Estudent){
        println("mira - student   "+student.age+" "+student.id )
        roomM.provideRoom(this).getStudentDao().update(StudentEntity(student.id.toInt(),student.name.replace(" ","")+" "+student.secondName.replace(" ",""),student.age.toString()))
    }

    fun delete(student:Estudent){
        println()
        roomM.provideRoom(this).getStudentDao().delete(StudentEntity(Integer.parseInt(student.id),student.name+" "+student.secondName, student.age.toString().replace(" ","")))
    }
     fun getSt(){
       var gas= roomM.provideRoom(this).getStudentDao().getAllStudents()


        listEstudent.clear()
        for (i in gas.indices){
            var estudent= Estudent()
            estudent.id=gas[i].id.toString()
            var sp= gas[i].name.split(" ")
            estudent.name=sp[0]
            estudent.secondName=sp[1]
            estudent.age=Integer.parseInt(gas[i].age)
           listEstudent.add(estudent)

        }
        adapter.filtros(listEstudent)

    }

    fun infoAlumno (){
        pulsaAdd= true
        functionAdd.text= getString(R.string.info)
        linearInfo.visibility = View.VISIBLE
    }

    fun editInfoAlumno (){
        pulsaAdd= true
        editarBool=true
        functionAdd.text= getString(R.string.editalumno)
        linearInfo.visibility = View.VISIBLE
    }

    fun cancel(){
        cleartext()
        pulsaAdd= false
        editarBool=false
        linearInfo.visibility = View.GONE
    }

    fun save(){
      if(name.text.toString()!=""&&secondName.text.toString()!=""){
          if(Integer.parseInt(age.text.toString()) in 3..99){
              var student = Estudent()
              student.name = name.text.toString().replace(" ","")
              student.secondName = secondName.text.toString().replace(" ","")
              student.age = Integer.parseInt(age.text.toString().replace(" ",""))
              listEstudent.add(student)
              GlobalScope.launch {
                  insert(student)
              }
              getSt()
              cancel()
          }else{
              HelperDialog(this).showToast("Ingrese una edad valida")
          }
      } else{
          HelperDialog(this).showToast("Ingrese Nombre(s) y Apellidos")
      }
    }

    fun editSave(){

            if(Integer.parseInt(age.text.toString()) in 3..99){
               ageMode= Integer.parseInt(age.text.toString())
                if(filtrado){
                    filtros[idMod].age=ageMode
                    println("mira - 1 "+filtros[idMod].age)
                    update(filtros[idMod])
                }else{
                    listEstudent[idMod].age=ageMode
                    println("mira -2  "+listEstudent[idMod].age)
                    update(listEstudent[idMod])
                }
                adapter.notifyDataSetChanged()

                cancel()
            }else{
                HelperDialog(this).showToast("Ingrese una edad valida")
            }

    }

    fun cleartext(){
        functionAdd.text= getString(R.string.add)
        name.setText("")
        age.setText("")
        secondName.setText("")
    }


    fun logOut(){
        Firebase.auth.signOut()
        val gsoptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        var mGoogleSignInClient = GoogleSignIn.getClient(this, gsoptions);
        mGoogleSignInClient.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun filtro(texto: String) {
        filtros.clear()
        filtrado=true
        for (item in listEstudent) {
            var nameComplete =item.name+" "+item.secondName
            if (nameComplete.toLowerCase().contains(texto.toLowerCase())) {
                filtros.add(item)
            }else if (item.age.toString().toLowerCase().contains(texto.toLowerCase())) {
                filtros.add(item)
            }
        }
        adapter.filtros(filtros)
        rv_item.adapter = adapter
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.getItemId()) {
            R.id.clima -> {
                val intent = Intent(this, ClimaActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.logout -> {
               HelperDialog(this).dialogCerrarSesion()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}