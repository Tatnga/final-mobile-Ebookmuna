package com.example.app2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class forgotpass : AppCompatActivity() {
    private lateinit var loginuser3: EditText
    private lateinit var button5: Button
    private lateinit var button6: Button

    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpass)

        loginuser3 = findViewById(R.id.loginuser3)
        button5 = findViewById(R.id.button5)
        button6 = findViewById(R.id.button6)

        button5.setOnClickListener{
            val pass = button5.text.toString()
          auth.sendPasswordResetEmail(pass).addOnSuccessListener {
              Toast.makeText(this,"Please Check your Email", Toast.LENGTH_SHORT).show()
          }
              .addOnFailureListener{}
            Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
        }
    }
}