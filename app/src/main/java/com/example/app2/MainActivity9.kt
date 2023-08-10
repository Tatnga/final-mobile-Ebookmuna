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
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity9 : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle



    val nodeName = MainActivity2.nodeName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main9)

        // Declare variables

        val dbRef = FirebaseDatabase.getInstance().getReference("users/$nodeName")

        val text2 = findViewById<TextView>(R.id.text2)
        val text1 = findViewById<TextView>(R.id.text1)
        val text3 = findViewById<TextView>(R.id.text3)
        val text4 = findViewById<TextView>(R.id.text4)
        val drawerLayout : DrawerLayout = findViewById(R.id.drawer_layout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        // Add event listener to update user stats
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(user::class.java)
                if (user != null) {
                    text2.text = user.totalbook
                    text1.text = user.totalreview
                    text3.text = user.totalpending
                    text4.text = user.totalreturn

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Failed to read value.", databaseError.toException())
            }
        })

        // Add event listener to update chart

        b1()
        b2()
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
    private fun b2(){
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("borrowbook")
        val chart: LineChart = findViewById(R.id.chart1)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val monthCounts = mutableMapOf<Int, MutableMap<String, Int>>()
                val allMonths = (1..12).toList()

                for (data in snapshot.children) {
                    val borrowDate = data.child("date").value as? String
                    val userid = data.child("nodeName").value as? String
                    val stat = data.child("status").value as? String
                    if (borrowDate != null && userid != null && userid == nodeName  ) {
                        if (stat == "reviewed") {
                            val borrowMonth = getMonthFromDate(borrowDate)
                            val borrowYear = borrowDate.substring(0, 4)

                            if (!monthCounts.containsKey(borrowYear.toInt())) {
                                monthCounts[borrowYear.toInt()] = mutableMapOf()
                            }

                            val yearMap = monthCounts[borrowYear.toInt()]!!
                            if (!yearMap.containsKey(borrowMonth.toString())) {
                                yearMap[borrowMonth.toString()] = 1
                            } else {
                                yearMap[borrowMonth.toString()] =
                                    yearMap[borrowMonth.toString()]!! + 1
                            }
                        }
                    }
                }

                val entries = mutableListOf<Entry>()
                for (month in allMonths) {
                    for (year in monthCounts.keys) {
                        val count = monthCounts[year]?.getOrDefault(month.toString(), 0) ?: 0
                        entries.add(Entry(month.toFloat(), count.toFloat()))
                    }
                }
                val dataSet = LineDataSet(entries, "Reviewed Book Count")
                dataSet.color = Color.BLUE
                dataSet.lineWidth = 2f
                dataSet.setCircleColor(Color.RED)
                chart.data = LineData(dataSet)
                chart.xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return DateFormatSymbols().months[value.toInt() - 1]
                    }
                }
                chart.invalidate()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Failed to read value.", error.toException())
            }
        })

    }
    private fun b1(){
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("borrowbook")
        val chart: LineChart = findViewById(R.id.chart)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val monthCounts = mutableMapOf<Int, MutableMap<String, Int>>()
                val allMonths = (1..12).toList()

                for (data in snapshot.children) {
                    val borrowDate = data.child("date").value as? String
                    val userid = data.child("nodeName").value as? String
                    val stat = data.child("status").value as? String
                    if (borrowDate != null && userid != null && userid == nodeName  ) {
                        if (stat == "returned" || stat == "payed" || stat == "reviewed") {
                            val borrowMonth = getMonthFromDate(borrowDate)
                            val borrowYear = borrowDate.substring(0, 4)

                            if (!monthCounts.containsKey(borrowYear.toInt())) {
                                monthCounts[borrowYear.toInt()] = mutableMapOf()
                            }

                            val yearMap = monthCounts[borrowYear.toInt()]!!
                            if (!yearMap.containsKey(borrowMonth.toString())) {
                                yearMap[borrowMonth.toString()] = 1
                            } else {
                                yearMap[borrowMonth.toString()] =
                                    yearMap[borrowMonth.toString()]!! + 1
                            }
                        }
                    }
                }

                val entries = mutableListOf<Entry>()
                for (month in allMonths) {
                    for (year in monthCounts.keys) {
                        val count = monthCounts[year]?.getOrDefault(month.toString(), 0) ?: 0
                        entries.add(Entry(month.toFloat(), count.toFloat()))
                    }
                }
                val dataSet = LineDataSet(entries, "Borrow Book Count")
                dataSet.color = Color.BLUE
                dataSet.lineWidth = 2f
                dataSet.setCircleColor(Color.RED)
                chart.data = LineData(dataSet)
                chart.xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return DateFormatSymbols().months[value.toInt() - 1]
                    }
                }
                chart.invalidate()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Failed to read value.", error.toException())
            }
        })

    }
    private fun getMonthFromDate(dateString: String): Int {
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.time = date ?: Date()
        return calendar.get(Calendar.MONTH) + 1
    }

}