
package com.example.app2

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class BookAdapterx4(private val context: Context, private var bookList: List<book>) :
    RecyclerView.Adapter<BookAdapterx4.BookViewHolder>() {
    var currentCourse: String = ""
    private val db = FirebaseDatabase.getInstance().getReference("book")
    val nodeName = MainActivity2.nodeName
    var onItemClick: ((book) -> Unit)? = null

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text1: TextView = itemView.findViewById(R.id.text1)
        val text3: TextView = itemView.findViewById(R.id.text3)

        val image: ImageView = itemView.findViewById(R.id.book_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.book_list_item, parent, false)
        return BookViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.text1.text = book.bookname
        holder.text3.text = book.bookauthor


        // Load image from Firebase Storage
        val storageRef = book.book_image_url
        Glide.with(context)
            .load(storageRef)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(book)
            showBookDetailsDialog(book)
        }
    }

    fun updateBookList(bookList: List<book>) {
        this.bookList = bookList.filter { it.bookquantity.toIntOrNull() ?: 0 > 0 }
        notifyDataSetChanged()
    }

    fun fetchBookList() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val bookList = mutableListOf<book>()
                for (postSnapshot in dataSnapshot.children) {
                    val book = postSnapshot.getValue(book::class.java)
                    if ((book?.bookquantity as? Int ?: 0) >= 0)  { // check if book quantity is greater than 0
                        bookList.add(book!!)
                    }
                }
                val sortedBookList = bookList.sortedByDescending { it.date }
                updateBookList(sortedBookList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors
            }
        })
    }


    private fun showBookDetailsDialog(book: book) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.book_details_dialog)
        val button = dialog.findViewById<Button>(R.id.button)
        val button2 = dialog.findViewById<Button>(R.id.button2)
        val textView2 = dialog.findViewById<TextView>(R.id.textView2)
        val textView8 = dialog.findViewById<TextView>(R.id.textView8)
        val textView30 = dialog.findViewById<TextView>(R.id.textView30)
        val textView23 = dialog.findViewById<TextView>(R.id.textView23)
        val textView15 = dialog.findViewById<TextView>(R.id.textView15)
        val imageView5 = dialog.findViewById<ImageView>(R.id.imageView5)

        textView2.text = book.bookname
        textView23.text = "Author: ${book.bookauthor}"
        textView15.text = book.description
        textView30.text = "Available Book${book.bookquantity}"
        textView8.text = "Average Review book ${(book.averagereview.toDouble() / book.totalreview.toDouble()).toString()}"

        // Load book image from Firebase Storage and set it to ImageView
        val storageRef = book.book_image_url
        Glide.with(context)
            .load(storageRef)
            .into(imageView5)
        val rootNode = FirebaseDatabase.getInstance().reference
        val dbRef = FirebaseDatabase.getInstance().getReference("users")
        val childRef = dbRef.child(nodeName)



        button.setOnClickListener {

            val db1 = FirebaseDatabase.getInstance().getReference("borrowbook")
            val db2 = FirebaseDatabase.getInstance().getReference("book")
            val dbRef = FirebaseDatabase.getInstance().getReference("users")
            val childRef = dbRef.child(nodeName)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val currentDate = Date()
            val calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.DATE, book.totaldays.toInt())
            val duedate = dateFormat.format(calendar.time)
            val registrationDate = dateFormat.format(currentDate)

            childRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val currentname = snapshot.child("name").value.toString()
                    val currentcourse = snapshot.child("usercourse").value.toString()
                    Log.d("TAG", "bookList1: $currentcourse")

                    var lastNodeNumber = 0
                    var newNodeNumber = 0
                    var borrownode: String? = null
                    db1.orderByKey().limitToLast(1).get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            var lastNodeNumber = -1
                            var newNodeNumber=0
                            for (child in task.result?.children ?: emptyList()) {
                                lastNodeNumber = child.key?.toInt() ?: -1

                            }
                            newNodeNumber = lastNodeNumber + 1


                            val query = db2.orderByChild("bookname").equalTo(book.bookname)

                            query.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    // Handle the query result here
                                    for (bookSnapshot in dataSnapshot.children) {
                                        // You can access the child node with the matching bookname here
                                        borrownode = bookSnapshot.key

                                        val borrowbook = BookDetails(
                                            book.bookname,
                                            nodeName,
                                            "none",
                                            "none",
                                            "none",
                                            "pending",
                                            registrationDate,
                                            currentname,
                                            newNodeNumber.toString(),
                                            borrownode, book.totaldays
                                        )

                                        val builder = AlertDialog.Builder(context)
                                        builder.setTitle("Book Borrowed")
                                        builder.setMessage("Do you want to continue?")
                                        builder.setPositiveButton("Yes") { _, _ ->
                                            // User clicked Yes button, do something
                                            Toast.makeText(context, "Book Borrowed!", Toast.LENGTH_SHORT).show()
                                        }

                                        db1.orderByKey().limitToLast(1).get().addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                var lastNodeNumber = -1
                                                var newNodeNumber = 0
                                                for (child in task.result?.children ?: emptyList()) {
                                                    lastNodeNumber = child.key?.toInt() ?: -1

                                                }

                                                newNodeNumber = lastNodeNumber + 1
                                                db1.child(newNodeNumber.toString()).setValue(borrowbook).addOnCompleteListener { task2 ->
                                                    if (task2.isSuccessful) {
                                                        // Show confirmation dialog

                                                    } else {
                                                        // Show error message
                                                        Toast.makeText(context, "Failed to save data.", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }

                                        }
                                        builder.setNegativeButton("No") { _, _ ->
                                            // User clicked No button, do something
                                        }
                                        builder.show()
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle the error here
                                    println("Query cancelled: ${databaseError.message}")
                                }
                            })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle errors here
                }
            })
            dialog.dismiss()

        }

        dialog.show()
    }



}