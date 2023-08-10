 package com.example.app2

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.NonCancellable.key
import java.text.SimpleDateFormat
import java.util.*

 class  MainActivity : AppCompatActivity() {


     private lateinit var editname: EditText
     private lateinit var editidno: EditText
     private lateinit var editemail: EditText
     private lateinit var editcontact: EditText
     private lateinit var editaddress: EditText
     private lateinit var editpassword: EditText
     private lateinit var editpassword2: EditText
     private lateinit var btnsave: Button
     private lateinit var btnlogin: Button
     private lateinit var spinner: Spinner
     private lateinit var spinner2: Spinner
     private lateinit var dbRef: DatabaseReference
     private lateinit var auth: FirebaseAuth
     private var selectedItem: String = "None"


     private var selectedItem1: String = "None"


     @SuppressLint("MissingInflatedId")
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)


         editname = findViewById(R.id.editname)
         editidno = findViewById(R.id.editidno)
         editemail = findViewById(R.id.editemail)
         editcontact = findViewById(R.id.editcontact)
         editaddress = findViewById(R.id.editaddress)
         editpassword = findViewById(R.id.editpassword)
         editpassword2 = findViewById(R.id.editpassword2)
         spinner = findViewById(R.id.spinner)
         spinner2 = findViewById(R.id.spinner2)
         btnsave = findViewById(R.id.btnsave)
         btnlogin = findViewById(R.id.btnlogin)

         auth = FirebaseAuth.getInstance()
         dbRef = FirebaseDatabase.getInstance().getReference("users")

// Obtain a reference to the Spinner using findViewById()

// Create an ArrayAdapter with the data to populate the Spinner
         val data = arrayOf("BEEd", "BSA", "BSBA","BSCpE","BSEE","BSECE","BSIE","BSME","BS Crim","BSCA","BSIT","BSCS ","BSHM","BSTM","BSN","BSMT","BS Mar Engg")
         val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, data)
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

// Set the ArrayAdapter to the Spinner
         spinner.adapter = adapter

// Add an OnItemSelectedListener to the Spinner
         spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
             override fun onItemSelected(
                 parent: AdapterView<*>?,
                 view: View?,
                 position: Int,
                 id: Long
             ) {
                 selectedItem = parent?.getItemAtPosition(position).toString()
                 // Do something with the selected item
             }

             override fun onNothingSelected(parent: AdapterView<*>?) {

             }
         }
// Obtain a reference to the Spinner using findViewById()


// Create an ArrayAdapter with the data to populate the Spinner

         val data1 = arrayOf("1", "2", "3", "4", "irregular")
         val adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, data1)
         adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

// Set the ArrayAdapter to the Spinner
         spinner2.adapter = adapter1

// Add an OnItemSelectedListener to the Spinner
         spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
             override fun onItemSelected(
                 parent: AdapterView<*>?,
                 view: View?,
                 position: Int,
                 id: Long
             ) {
                 selectedItem1 = parent?.getItemAtPosition(position).toString()
                 // Do something with the selected item
             }

             override fun onNothingSelected(parent: AdapterView<*>?) {

             }
         }

         this.btnlogin.setOnClickListener {
             val intent = Intent(this, MainActivity2::class.java)
             startActivity(intent)
         }
         this.btnsave.setOnClickListener {
             val pattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
             val name = editname.text.toString().trim()
             val idno = editidno.text.toString().trim()
             val email = editemail.text.toString().trim()
             val contact = editcontact.text.toString().trim()
             val address = editaddress.text.toString().trim()
             val password = editpassword.text.toString().trim()
             val password1 = editpassword2.text.toString().trim()
             val s1 = selectedItem1.trim()
             val s2 = selectedItem.trim().replace(" ", "").toLowerCase()
             if (name.isEmpty()) {
                 editname.error = "Please enter your Name"
             }
             else if (idno.isEmpty()) {
                 editidno.error = "Please enter ID Number"
             }
             else if (email.isEmpty()) {
                 editemail.error = "Please enter Email"
             }
             else if (contact.isEmpty()) {
                 editcontact.error = "Please enter Contact"
             }
             else if (address.isEmpty()) {
                 editaddress.error = "Please enter Address"
             }
             else if (password.isEmpty()) {
                 editpassword.error = "Please enter Password"
             }
             else if (password1.isEmpty()) {
                 editpassword2.error = "Please enter Password"
             }
             else if (selectedItem == "") {
                 Toast.makeText(applicationContext, "Please select a Course", Toast.LENGTH_SHORT)
                     .show()
             }
             else if (selectedItem1 == "") {
                 Toast.makeText(applicationContext, "Please select a Year", Toast.LENGTH_SHORT)
                     .show()
             }
             else if (password != password1) {
                 editpassword2.error = "Password Don't Match"
             }
             else if (password.length < 6) {
                 editpassword2.error = "Password is to short"
             }
             else if (!name.matches("^[a-zA-Z ]+\$".toRegex())) {
                 editname.error = "Name must contain only letters"
             }

             else if (!password.matches(".*[a-zA-Z].*".toRegex())) {
                 editpassword2.error = "Password must contain letters"
             }
             else if (!pattern.matches(email)) {
                 editemail.error = "Please enter a valid email address"
             }
             else if (name.isNullOrEmpty() || idno.isNullOrEmpty() || email.isNullOrEmpty() || contact.isNullOrEmpty() || address.isNullOrEmpty() || password.isNullOrEmpty() || password1.isNullOrEmpty()) {
                 Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
             }
             else if (!pattern.matches(email)) {
                 editemail.error = "Please enter a valid email address"
             }
             else if (password.length < 6 || !password.matches(
                     ".*[a-zA-Z].*".toRegex()
                 ) || !pattern.matches(email)
             ) {
                 Toast.makeText(this, "You have error", Toast.LENGTH_LONG).show()
             } else {
                 val auth = Firebase.auth
                 auth.fetchSignInMethodsForEmail(email)
                     .addOnCompleteListener { task ->
                         if (task.isSuccessful) {
                             val signInMethods = task.result?.signInMethods
                             if (signInMethods.isNullOrEmpty()) {
                                 auth.createUserWithEmailAndPassword(email, password)
                                     .addOnCompleteListener { createUserTask ->
                                         if (createUserTask.isSuccessful) {
                                             val dateFormat =
                                                 SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                             val currentDate = Date()
                                             val registrationDate = dateFormat.format(currentDate)
                                             var lastNodeNumber = 0

                                             dbRef.orderByKey().limitToLast(1).get()
                                                 .addOnCompleteListener { task ->
                                                     if (task.isSuccessful) {
                                                         var lastNodeNumber = -1
                                                         var newNodeNumber = 0
                                                         for (child in task.result?.children
                                                             ?: emptyList()) {
                                                             lastNodeNumber =
                                                                 child.key?.toInt() ?: -1

                                                         }
                                                         val currentDateTime: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                                                         newNodeNumber = lastNodeNumber + 1
                                                         val users = user(
                                                             name,
                                                             idno,
                                                             email,
                                                             contact,
                                                             address,
                                                             password,
                                                             s1,
                                                             s2,
                                                             "none",
                                                             "borrower",
                                                             "pending",
                                                             registrationDate,
                                                             newNodeNumber.toString(), // Add the user ID to the user object
                                                             "0", "0", "0", "0"
                                                         )
                                                         dbRef.orderByKey().limitToLast(1).get()
                                                             .addOnCompleteListener { task ->
                                                                 if (task.isSuccessful) {
                                                                     var lastNodeNumber = -1
                                                                     var newNodeNumber = 0
                                                                     for (child in task.result?.children
                                                                         ?: emptyList()) {
                                                                         lastNodeNumber =
                                                                             child.key?.toInt()
                                                                                 ?: -1

                                                                     }

                                                                     newNodeNumber =
                                                                         lastNodeNumber + 1
                                                                     dbRef.child(newNodeNumber.toString())
                                                                         .setValue(users)
                                                                     Toast.makeText(
                                                                         this,
                                                                         "Data Inserted",
                                                                         Toast.LENGTH_LONG
                                                                     ).show()
                                                                     val intent = Intent(
                                                                         this,
                                                                         MainActivity2::class.java
                                                                     )
                                                                     startActivity(intent)
                                                                 }
                                                             }
                                                     }
                                                 }
                                         }

                                     }

                             }
                             else {
                                 Toast.makeText(
                                     this,
                                     "Email already exist",
                                     Toast.LENGTH_LONG
                                 ).show()
                             }
                         }
                     }
             }

         }
     }
 }





