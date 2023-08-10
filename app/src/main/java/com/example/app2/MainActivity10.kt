package com.example.app2



import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity10 : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var ratingBar: RatingBar
    private lateinit var editText: EditText

    val nodeName = MainActivity2.nodeName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main10)

        // Declare variables

        val dbRef = FirebaseDatabase.getInstance().getReference("users/$nodeName")

        val button = findViewById<Button>(R.id.button)
        val button2 = findViewById<Button>(R.id.button2)


        val editText = findViewById<EditText>(R.id.editText)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)

        // Query the database to check if the node has been reviewed
        val db3 = FirebaseDatabase.getInstance().getReference("review1")
        db3.orderByChild("userid").equalTo(nodeName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val reviewList = ArrayList<review1>()
                for (reviewSnapshot in dataSnapshot.children) {
                    val review = reviewSnapshot.getValue(review1::class.java)
                    review?.let {
                        reviewList.add(it)
                    }
                }

                if (reviewList.isNotEmpty()) {
                    // Node has already been reviewed, display review details
                    val existingReview = reviewList[0] // Assuming there's only one review per node
                    editText.setText(existingReview.review1)
                    ratingBar.rating = existingReview.reviewstar!!.toFloat()

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors
            }
        })

        // Set up button click listeners
        button2.setOnClickListener {

        }

        button.setOnClickListener {
            val currentDate = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
            ).format(Date())
            val db3 = FirebaseDatabase.getInstance().getReference("review1")

            db3.orderByChild("userid").equalTo(nodeName).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val reviewList = ArrayList<review1>()
                    for (reviewSnapshot in dataSnapshot.children) {
                        val review = reviewSnapshot.getValue(review1::class.java)
                        review?.let {
                            reviewList.add(it)
                        }
                    }


                    val s1 = editText.text.toString().trim()
                    val rating = ratingBar.rating
                    val s2 = rating.toString().trim()
                    val rootNode = FirebaseDatabase.getInstance().reference
                    if (reviewList.isNotEmpty()) {
                        // Node has already been reviewed, update existing review
                        val existingReview = reviewList[0] // Assuming there's only one review per node
                        val newReview = review1(
                            existingReview.reviewid,
                            nodeName,
                            s1,
                            s2,
                            currentDate
                        )
                        db3.child(existingReview.reviewid!!).setValue(newReview)
                        Toast.makeText(
                            applicationContext,
                            "Review Updated",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(
                            applicationContext,
                            MainActivity10::class.java
                        )
                        startActivity(intent)
                    } else {
                        // Node hasn't been reviewed yet, add new review
                        db3.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                var newNodeNumber = 0
                                for (child in dataSnapshot.children) {
                                    newNodeNumber = child.key?.toInt() ?: 0
                                }
                                newNodeNumber += 1
                                val review2 = review1(
                                    newNodeNumber.toString(),
                                    nodeName,
                                    s1,
                                    s2,
                                    currentDate
                                )
                                db3.child(newNodeNumber.toString()).setValue(review2)
                                Toast.makeText(
                                    applicationContext,
                                    "Review Added",
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent = Intent(
                                    applicationContext,
                                    MainActivity10::class.java
                                )
                                startActivity(intent)
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Handle any errors
                            }
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors
                }
            })
        }


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            val navView: NavigationView = findViewById(R.id.nav_view)

            // Add event listener to update user stats

            toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.home -> {
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

                    R.id.profile -> {
                        val intent = Intent(this, MainActivity6::class.java)
                        //    intent.putExtra("key", message);
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

        }
    }
