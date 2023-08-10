package com.example.app2



import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.lang.Integer.reverse

class MainActivity3 : AppCompatActivity() {
     lateinit var toggle: ActionBarDrawerToggle
     private lateinit var recyclerView: RecyclerView
     private lateinit var recyclerView2: RecyclerView
     private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView3: RecyclerView
    private lateinit var recyclerView4: RecyclerView
     private lateinit var bookList: ArrayList<book>
    private lateinit var bookAdapter: BookAdapter
    private lateinit var bookAdapterx2: BookAdapterx2
    private lateinit var bookAdapterx3: BookAdapterx3
    private lateinit var bookAdapterx4: BookAdapterx4
    private lateinit var bookAdapterx5: BookAdapterx5

    val nodeName = MainActivity2.nodeName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

     //   val intent = intent
     //   val message = intent.getStringExtra("key")
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val recyclerView1 = findViewById<RecyclerView>(R.id.recyclerView1)
        val recyclerView2 = findViewById<RecyclerView>(R.id.recyclerView2)
        val recyclerView3 = findViewById<RecyclerView>(R.id.recyclerView3)
        val recyclerView4 = findViewById<RecyclerView>(R.id.recyclerView4)
        val bookAdapter = BookAdapter(this, emptyList())
        val bookAdapterx2 = BookAdapterx2(this, emptyList())
        val bookAdapterx3 = BookAdapterx3(this, emptyList())
        val bookAdapterx4 = BookAdapterx4(this, emptyList())
        val bookAdapterx5 = BookAdapterx5(this, emptyList())

        recyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        recyclerView.setHasFixedSize(true)
        recyclerView4.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        recyclerView4.setHasFixedSize(true)
        recyclerView3.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        recyclerView3.setHasFixedSize(true)

        recyclerView1.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        recyclerView1.setHasFixedSize(true)

        recyclerView2.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        recyclerView2.setHasFixedSize(true)
        val myVariable = intent.getStringExtra("MY_VARIABLE")

        val bookAdapter1 = BookAdapter(this, emptyList())
        val bookAdapter2 = BookAdapterx2(this, emptyList())
        val bookAdapter3 = BookAdapterx3(this, emptyList())
        val bookAdapter4 = BookAdapterx4(this, emptyList())
        val bookAdapter5 = BookAdapterx5(this, emptyList())

// Set the adapters to their respective RecyclerViews
        recyclerView.adapter = bookAdapter1
        recyclerView1.adapter = bookAdapter2
        recyclerView2.adapter = bookAdapter3
        recyclerView3.adapter = bookAdapter4
        recyclerView4.adapter = bookAdapter5

// Fetch the list of books from the Firebase database for each adapter
        bookAdapter1.fetchBookList()
        bookAdapter2.fetchBookList()
        bookAdapter3.fetchBookList()
        bookAdapter4.fetchBookList()
        bookAdapter5.fetchBookList()


        // Set the click listener for the items in the list
        bookAdapter.onItemClick = { book ->
            // Handle item click event
        }


  //      init()
        val drawerLayout : DrawerLayout = findViewById(R.id.drawer_layout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){

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
              //      intent.putExtra("key", message);
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}