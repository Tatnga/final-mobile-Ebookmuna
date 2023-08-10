package com.example.app2

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class  MainActivity6 : AppCompatActivity() {

    private lateinit var editn: EditText
    private lateinit var editnu: EditText
    private lateinit var editem: EditText
    private lateinit var editco: EditText
    private lateinit var editad: EditText
    private lateinit var editpa: EditText
    private lateinit var editpa1: EditText
    private lateinit var btnup: Button
    private lateinit var vtnca: Button
    private lateinit var sp1: Spinner
    private lateinit var sp2: Spinner
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var selectedItem: String = "None"
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookList: ArrayList<book>
    private lateinit var bookAdapter: BookAdapter
    private var selectedItem1: String = "None"
    private val nodeName = MainActivity2.nodeName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main6)
        editn = findViewById(R.id.editn)
        editnu = findViewById(R.id.editnu)
        editem = findViewById(R.id.editem)
        editco = findViewById(R.id.editco)
        editad = findViewById(R.id.editad)
        btnup = findViewById(R.id.btnup)
        editpa = findViewById(R.id.editpa)
        editpa1 = findViewById(R.id.editpa1)
        vtnca = findViewById(R.id.vtnca)
        sp1 = findViewById(R.id.sp1)
        sp2 = findViewById(R.id.sp2)
        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("users")


        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            // User is not authenticated, redirect to login screen
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
        val drawerLayout : DrawerLayout = findViewById(R.id.drawer_layout1)
        val navView : NavigationView = findViewById(R.id.nav_view1)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home ->   {
                    val intent = Intent(this, MainActivity3::class.java)
                    startActivity(intent)
                }
                R.id.library -> {
                    val intent = Intent(this, MainActivity4::class.java)
                    startActivity(intent)
                }
                R.id.borrowedbook -> {
                    val intent = Intent(this, MainActivity5::class.java)
                    startActivity(intent)
                }


                R.id.review -> {
                    val intent = Intent(this, MainActivity7::class.java)
                    startActivity(intent)
                }
                R.id.review1 -> {
                val intent = Intent(this, MainActivity8::class.java)
                startActivity(intent)
            }
                R.id.report -> {
                    val intent = Intent(this, MainActivity9::class.java)
                    startActivity(intent)
                }
                R.id.rateus -> {
                    val intent = Intent(this, MainActivity10::class.java)
                    startActivity(intent)
                }
                R.id.logoutnav -> {
                    val alertDialog = AlertDialog.Builder(this)
                    alertDialog.setTitle("Logout")
                    alertDialog.setMessage("Are you sure you want to log out?")
                    alertDialog.setPositiveButton("Yes") { _, _ ->
                        FirebaseAuth.getInstance().signOut()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    alertDialog.setNegativeButton("No", null)
                    alertDialog.show()
                }
            }
            true
        }
        val data = arrayOf("BEEd", "BSA", "BSBA","BSCpE","BSEE","BSECE","BSIE","BSME","BS Crim","BSCA","BSIT","BSCS ","BSHM","BSTM","BSN","BSMT","BS Mar Engg")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

// Set the ArrayAdapter to the Spinner
        sp2.adapter = adapter

// Add an OnItemSelectedListener to the Spinner
        sp2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        sp1.adapter = adapter1

// Add an OnItemSelectedListener to the Spinner
        sp1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        dbRef.child(nodeName).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(user::class.java)
                if (user != null) {
                    // Set the values of the EditText fields using the user object
                    editn.setText(user.name)
                    editnu.setText(user.useridno)
                    editem.setText(user.email)
                    editco.setText(user.contact)
                    editad.setText(user.address)
                    sp2.setSelection(adapter.getPosition(user.usercourse))
                    sp1.setSelection(adapter1.getPosition(user.useryear))
                    editpa.setText(user.password)
                    editpa.setText(user.password)

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        this.vtnca.setOnClickListener {
            val intent = Intent(this, MainActivity6::class.java)
            startActivity(intent)
        }
        this.btnup.setOnClickListener {

        updateUser(nodeName)

        }
    }
    private fun updateUser(userId: String) {
        val pattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val name = editn.text.toString().trim()
        val idno = editnu.text.toString().trim()
        val email = editem.text.toString().trim()
        val contact = editco.text.toString().trim()
        val address = editad.text.toString().trim()
        val password = editpa.text.toString().trim()
        val password1 = editpa1.text.toString().trim()
        val s1 = selectedItem1.trim()
        val s2 = selectedItem.trim().replace(" ", "").toLowerCase()
        var hasError = false

        if (name.isEmpty()) {
            editn.error = "Please enter your Name"
            hasError = true
        }
        if (idno.isEmpty()) {
            editnu.error = "Please enter ID Number"
            hasError = true
        }
        if (email.isEmpty()) {
            editem.error = "Please enter Email"
            hasError = true
        }
        if (contact.isEmpty()) {
            editco.error = "Please enter Contact"
            hasError = true
        }
        if (address.isEmpty()) {
            editad.error = "Please enter Address"
            hasError = true
        }
        if (password.isEmpty()) {
            editpa.error = "Please enter Password"
            hasError = true
        }
        if (password1.isEmpty()) {
            editpa1.error = "Please enter Password"
            hasError = true
        }
        if (selectedItem == "") {
            Toast.makeText(applicationContext, "Please select a Course", Toast.LENGTH_SHORT)
                .show()
            hasError = true
        }
        if (selectedItem1 == "") {
            Toast.makeText(applicationContext, "Please select a Year", Toast.LENGTH_SHORT)
                .show()
            hasError = true
        }
        if (password != password1) {
            editpa.error = "Password Don't Match"
            hasError = true
        }
        if (password.length < 6) {
            editpa.error = "Password is too short"
            hasError = true
        }

        if (!password.matches(".*[a-zA-Z].*".toRegex())) {
            editpa.error = "Password must contain letters"
            hasError = true
        }
        if (!pattern.matches(email)) {
            editem.error = "Please enter a valid email address"
            hasError = true
        }

        if (hasError) {
            Toast.makeText(this, "Please correct the errors", Toast.LENGTH_LONG).show()
            return
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = Date()
        val registrationDate = dateFormat.format(currentDate)
        var lastNodeNumber = -1
        var newNodeNumber = 0
        dbRef.orderByKey().limitToLast(1).get()

            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    for (child in task.result?.children
                        ?: emptyList()) {
                        lastNodeNumber =
                            child.key?.toInt() ?: -1

                    }
                }

                val users = user(
                    name,
                    idno,
                    email,
                    contact,
                    address,
                    password,
                   s1,s2,
                    "none",
                    "borrower",
                    "pending",
                    registrationDate,
                    newNodeNumber.toString()
                )

                // Create an AlertDialog.Builder object
                val builder = AlertDialog.Builder(this)

// Set the title and message of the dialog
                builder.setTitle("Confirm")
                    .setMessage("Are you sure you want to update your profile?")

// Set the positive button action to update the user profile
                builder.setPositiveButton("Yes") { dialog, which ->
                    // Update the user profile
                    val userRef = dbRef.child(nodeName)
                    userRef.setValue(users).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Update email and password in Firebase Authentication
                            val user = auth.currentUser
                            user?.updateEmail(email)?.addOnCompleteListener { emailTask ->
                                if (emailTask.isSuccessful) {
                                    user.updatePassword(password)?.addOnCompleteListener { passwordTask ->
                                        if (passwordTask.isSuccessful) {
                                            Toast.makeText(
                                                this,
                                                "User profile updated successfully",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            FirebaseAuth.getInstance().signOut()
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "Failed to update password",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(this, "Failed to update email", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "Failed to update user profile", Toast.LENGTH_LONG).show()
                        }
                    }
                }

// Set the negative button action to cancel the update
                builder.setNegativeButton("No") { dialog, which ->
                    // Do nothing and dismiss the dialog
                    dialog.dismiss()
                }

// Create the AlertDialog object and show it
                val dialog = builder.create()
                dialog.show()

                }

            }
            }



      private fun getIndex(spinner: Spinner, value: String?): Int {
            for (i in 0 until spinner.count) {
                if (spinner.getItemAtPosition(i).toString() == value) {
                    return i
                }
            }
            return 0
        }









