package com.example.app2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity2 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    companion object {
        var nodeName: String = ""

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.GONE
        auth = FirebaseAuth.getInstance()
        val textView = findViewById<TextView>(R.id.textView)
        val loginuser = findViewById<EditText>(R.id.loginuser)
        val loginpass = findViewById<EditText>(R.id.loginpass)
        val loginbtn = findViewById<Button>(R.id.loginbtn)
        textView.setOnClickListener {
            val intent = Intent(this, forgotpass::class.java)
            startActivity(intent)
        }
        loginbtn.setOnClickListener {
            val progressBar = findViewById<ProgressBar>(R.id.progressBar)
            val email = loginuser.text.toString().trim()
            val pass = loginpass.text.toString().trim()
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter valid details", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // User has signed in successfully
                            val user = auth.currentUser
                            val rootNode = FirebaseDatabase.getInstance().reference
                            val query = rootNode.child("users").orderByChild("email").equalTo(email)
                            query.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (child in dataSnapshot.children) {
                                            // Retrieve the key of the child node that contains user data
                                            val nodeName = child.key
                                            nodeName?.let { MainActivity2.nodeName = it
                                            }
                                            val userStatus = child.child("status").value
                                            if (userStatus != "accepted") {
                                                // User registration is still pending
                                                Toast.makeText(
                                                    this@MainActivity2,
                                                    "User registration is still pending",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                return
                                            }

                                            Toast.makeText(
                                                this@MainActivity2,
                                                "Signed in as ${user?.email}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            // Redirect to another activity and pass the child key as extra data
                                            val intent = Intent(
                                                this@MainActivity2,
                                                MainActivity3::class.java
                                            )
                                            intent.putExtra("childKey", nodeName)
                                            startActivity(intent)
                                            return
                                        }
                                    } else {
                                        // Email not found in database
                                        Toast.makeText(
                                            this@MainActivity2,
                                            "Email not found",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle database error
                                    Toast.makeText(
                                        this@MainActivity2,
                                        "Database error: ${databaseError.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                        } else {
                            // Sign in failed. Display an error message
                            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }

    }

}
