package com.example.app2

import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class MainActivity4 : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var database1: DatabaseReference
    private lateinit var database2: DatabaseReference
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var recyclerView2: RecyclerView
    private lateinit var bookList: ArrayList<book>
    private lateinit var bookAdapter: BookAdapter1
    private lateinit var searchView: SearchView
    private var filteredBookList: List<book> = listOf()
    private lateinit var s1: Spinner
    private lateinit var s2: Spinner
    private lateinit var s3: Spinner
    private lateinit var s4: Spinner
    private lateinit var s5: Spinner
    private lateinit var s6: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database2 = FirebaseDatabase.getInstance().reference
        database1 = FirebaseDatabase.getInstance().reference
        database = FirebaseDatabase.getInstance().reference
        setContentView(R.layout.activity_main4)
        s1 = findViewById(R.id.s1)
        s2 = findViewById(R.id.s2)
        s3 = findViewById(R.id.s3)
        s4 = findViewById(R.id.s4)
        s5 = findViewById(R.id.s5)
        s6 = findViewById(R.id.s6)
        bookAdapter = BookAdapter1(this, filteredBookList)
        database = FirebaseDatabase.getInstance().reference.child("course")
        database2 = FirebaseDatabase.getInstance().reference.child("subjects")

// Read the data from the Firebase database
        val courseList = ArrayList<String>()
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the list to prevent duplicates
                courseList.clear()
                courseList.add("ALL")
                // Loop through the database snapshot and add the courses to the list
                for (courseSnapshot in snapshot.children) {
                    val course = courseSnapshot.child("name1").getValue(String::class.java)
                    course?.let { courseList.add(it) }
                }
                // Set the spinner adapter with the course list
                val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, courseList)
                s2.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        val subjectList = ArrayList<String>()
        database2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the list to prevent duplicates
                subjectList.clear()
                subjectList.add("ALL")
                // Loop through the database snapshot and add the courses to the list
                for (subjectSnapshot in snapshot.children) {
                    val course = subjectSnapshot.child("name1").getValue(String::class.java)
                    course?.let { subjectList.add(it) }
                }
                // Set the spinner adapter with the course list
                val adapter1 = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, subjectList)
                s5.adapter = adapter1
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        bookList = ArrayList()
        recyclerView2 = findViewById(R.id.recyclerView2)
        searchView = findViewById(R.id.searchView)
        bookAdapter = BookAdapter1(this, listOf())

        recyclerView2.adapter = bookAdapter
        recyclerView2.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView2.setHasFixedSize(true)


        bookAdapter.onItemClick = { book ->
            // Handle item click event
        }

        database1.child("book").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val book = snapshot.getValue(book::class.java)
                    book?.let { bookList.add(it) }
                }

                // Update filteredBookList and notify adapter of data change
                updateFilteredBookList()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    bookAdapter.search(newText)
                } else {
                    bookAdapter.updateBookList(listOf())
                }
                return true
            }
        })
        s1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateFilteredBookList()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        s2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateFilteredBookList()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        s3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateFilteredBookList()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        s4.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateFilteredBookList()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        s5.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateFilteredBookList()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        s6.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateFilteredBookList()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        bookAdapter.fetchBookList()
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

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
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun filterSearchResults(query: String, originalList: List<book>): List<book> {

        val normalizedQuery = query.lowercase(Locale.getDefault()).trim()
        return originalList.filter { book ->
            val bookname = book.bookname.lowercase(Locale.getDefault())
            val bookauthor = book.bookauthor.lowercase(Locale.getDefault())
            bookname.contains(normalizedQuery) || bookauthor.contains(normalizedQuery)
        }
    }

    private fun updateFilteredBookList() {


        if (bookList.isEmpty()) {

            bookAdapter.fetchBookList()

        }

        val s1SelectedOption = s1.selectedItem.toString()
        val s2SelectedOption = s2.selectedItem.toString()
        val s3SelectedOption = s3.selectedItem.toString()
        val s4SelectedOption = s4.selectedItem.toString()
        val s5SelectedOption = s5.selectedItem.toString()
        val s6SelectedOption = s6.selectedItem.toString()
        Log.d("TAG", "Selected options: $s1SelectedOption, $s2SelectedOption, $s3SelectedOption")

        filteredBookList =
            if (s1SelectedOption == "ALL" && s2SelectedOption == "ALL" && s3SelectedOption == "ALL" && s4SelectedOption == "ALL" && s5SelectedOption == "ALL") {
                bookList
            } else {
                val filteredList = bookList.filter { book ->
                    val bookCatalog = book.bookcatalog.lowercase(Locale.getDefault())
                    val bookCourse = book.bookcourse.lowercase(Locale.getDefault())
                    val bookYear = book.bookyear.lowercase(Locale.getDefault())
                    val bookSubjects = book.subjects.joinToString(separator = ",").lowercase(Locale.getDefault())


                    val matchCatalog =
                        s1SelectedOption.trim() == "ALL" || bookCatalog.trim() == s1SelectedOption.trim()
                            .toLowerCase(Locale.getDefault())
                    val matchCourse =
                        s2SelectedOption.trim() == "ALL" || bookCourse.trim() == s2SelectedOption.trim()
                            .toLowerCase(Locale.getDefault())
                    val matchYear =
                        s3SelectedOption.trim() == "ALL" || bookYear.trim() == s3SelectedOption.trim()
                            .toLowerCase(Locale.getDefault())
                    val matchSubjects = s5SelectedOption.trim() == "ALL" || bookSubjects.contains(s5SelectedOption.trim().toLowerCase(Locale.getDefault()))


                    matchCatalog && matchCourse && matchYear && matchSubjects
                }
                if (s3SelectedOption == "") {
                    Log.d("TAG", "Filtered Book List is Empty")
                    // Handle the case where no books match the selected options.
                    // You could display a message to the user or update the UI in some way.
                }

                filteredList
            }

        if (s6SelectedOption == "Name") {
            if (s4SelectedOption == "Ascending Order") {
                bookAdapter.updateBookList(filteredBookList)
            } else {
                bookAdapter.updateBookList1(filteredBookList)
            }
        }
        else if (s6SelectedOption == "Author") {
            if (s4SelectedOption == "Ascending Order") {
                bookAdapter.updateBookList2(filteredBookList)
            } else {
                bookAdapter.updateBookList3(filteredBookList)
            }
        }
        else  {
            if (s4SelectedOption == "Ascending Order") {
                bookAdapter.updateBookList4(filteredBookList)
            } else {
                bookAdapter.updateBookList5(filteredBookList)
            }
        }
    }
}