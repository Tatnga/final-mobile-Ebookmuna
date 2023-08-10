package com.example.app2



import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity5 : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var recyclerView2: RecyclerView
    private lateinit var bookList: ArrayList<BookDetails>
    private lateinit var bookAdapter: BookAdapter3

    val nodeName = MainActivity2.nodeName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)

        bookList = ArrayList()
        recyclerView2 = findViewById(R.id.recyclerView2)
        bookAdapter = BookAdapter3(this, listOf())
        recyclerView2.adapter = bookAdapter
        recyclerView2.layoutManager = LinearLayoutManager(this)
        bookAdapter.fetchBookList()

        // Fetch the list of books from the Firebase database for the adapter

        // Set the click listener for the items in the list
        bookAdapter.onItemClick = { BookDetails ->
            // Handle item click event
        }
        val drawerLayout : DrawerLayout = findViewById(R.id.drawer_layout)
        val navView : NavigationView = findViewById(R.id.nav_view)

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