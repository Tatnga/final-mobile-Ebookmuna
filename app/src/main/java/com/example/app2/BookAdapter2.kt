package com.example.app2

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
class BookAdapter2(private val context: Context, var bookList: List<BookDetails>) :
    RecyclerView.Adapter<BookAdapter2.BookViewHolder>() {

    private var filteredBookList: List<BookDetails> = bookList
    private val db = FirebaseDatabase.getInstance().getReference("borrowbook")
    val nodeName = MainActivity2.nodeName
    var onItemClick: ((BookDetails) -> Unit)? = null

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text1: TextView = itemView.findViewById(R.id.text1)
        val text3: TextView = itemView.findViewById(R.id.text3)
        val text4: TextView = itemView.findViewById(R.id.text4)
        val text5: TextView = itemView.findViewById(R.id.text5)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_list_item2, parent, false)
        return BookViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredBookList.size
    }

    fun search(query: String) {
        filteredBookList = if (query.isEmpty()) {
            bookList.filter { it.nodeName == nodeName } // filter based on nodeName
        } else {
            bookList.filter { BookDetails ->
                BookDetails.nodeName == nodeName && (BookDetails.bookname?.lowercase(Locale.ROOT)?.contains(query.lowercase(Locale.ROOT)) == true
                        ||    BookDetails.borrowername?.lowercase(Locale.ROOT)?.contains(query.lowercase(Locale.ROOT)) == true)
            }
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val BookDetails = filteredBookList[position]
        holder.text1.text = BookDetails.bookname
        holder.text3.text = BookDetails.date
        holder.text4.text = BookDetails.currentid
        holder.text5.text = BookDetails.status
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(BookDetails)
            showBookDetailsDialog(BookDetails)
        }
    }

    fun setData(bookList: List<BookDetails>) {
        this.bookList = bookList
        filteredBookList = bookList.filter { it.nodeName == nodeName } // filter based on nodeName
        notifyDataSetChanged()
    }
    private fun showBookDetailsDialog(BookDetails: BookDetails) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.book_details_dialog2)
        val button = dialog.findViewById<Button>(R.id.button)
        val button2 = dialog.findViewById<Button>(R.id.button2)
        val textView = dialog.findViewById<TextView>(R.id.textView)
        val textView8 = dialog.findViewById<TextView>(R.id.textView8)
        val textView15 = dialog.findViewById<TextView>(R.id.textView15)
        val imageView5 = dialog.findViewById<ImageView>(R.id.imageView5)
        val editText = dialog.findViewById<EditText>(R.id.editText)
        val ratingBar = dialog.findViewById<RatingBar>(R.id.ratingBar)
        textView.text = BookDetails.bookname


        val s1 = editText.text.toString().trim()
        val rating = ratingBar.rating
        val s2 = rating.toString().trim()
        val rootNode = FirebaseDatabase.getInstance().reference
        button2.setOnClickListener {

            dialog.dismiss()
        }

        button.setOnClickListener {
            val db2 = FirebaseDatabase.getInstance().getReference("book")
            db2.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val bookList = ArrayList<book>()
                    for (bookSnapshot in dataSnapshot.children) {
                        val book = bookSnapshot.getValue(book::class.java)
                        book?.let {
                            bookList.add(it)
                        }
                    }
                    // Pass the bookList to the whole activity
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors
                }
            })
            val db1 = FirebaseDatabase.getInstance().getReference("borrowbook")
            val dbRef = FirebaseDatabase.getInstance().getReference("users")
            val db3 = FirebaseDatabase.getInstance().getReference("review")
            val childRef = dbRef.child(nodeName)
            val editText = dialog.findViewById<EditText>(R.id.editText)
            val ratingBar = dialog.findViewById<RatingBar>(R.id.ratingBar)
            val s1 = editText.text.toString().trim()
            val rating = ratingBar.rating
            val s2 = rating.toString().trim()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val currentDate = Date()
            val calendar = Calendar.getInstance()
            calendar.time = currentDate

            val registrationDate = dateFormat.format(currentDate)

            childRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val currentid = snapshot.child("childno").value.toString()
                    val currentcourse = snapshot.child("usercourse").value.toString()
                    var lastNodeNumber = 0
                    var borrownode: String? = null

                    db3.orderByKey().limitToLast(1).get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            var lastNodeNumber = -1
                            var newNodeNumber = 0

                            for (child in task.result?.children ?: emptyList()) {
                                lastNodeNumber = child.key?.toInt() ?: -1
                            }

                            newNodeNumber = lastNodeNumber + 1

                            val query = db1.orderByChild("currentid").equalTo(BookDetails.currentid)
                            query.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    // Handle the query result here
                                    for (bookSnapshot in dataSnapshot.children) {
                                        // You can access the child node with the matching bookname here
                                        borrownode = bookSnapshot.key

                                        if (s1.isNullOrEmpty() || s2.isNullOrEmpty()) {
                                            Toast.makeText(
                                                context,
                                                "Please fill all fields.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return
                                        }


                                        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                                        val review1 = review(
                                            newNodeNumber.toString(),
                                            BookDetails.bookid,
                                            currentid,
                                            s1,
                                            s2,
                                           currentDate
                                        )

                                        val builder = AlertDialog.Builder(context)
                                        builder.setTitle("Book Review")
                                        builder.setMessage("Do you want to continue?")
                                        builder.setPositiveButton("Yes") { _, _ ->
                                            // User clicked Yes button, do something
                                            Toast.makeText(
                                                context,
                                                "Book Reviewed!",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            db3.orderByKey().limitToLast(1).get()
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        var lastNodeNumber = -1
                                                        var newNodeNumber = 0

                                                        for (child in task.result?.children ?: emptyList()) {
                                                            lastNodeNumber = child.key?.toInt() ?: -1
                                                        }

                                                        newNodeNumber = lastNodeNumber + 1

                                                        db3.child(newNodeNumber.toString())
                                                            .setValue(review1)
                                                            .addOnCompleteListener { task2 ->
                                                                if (task2.isSuccessful) {
                                                                    val databaseReference = FirebaseDatabase.getInstance().getReference("borrowbook").child(
                                                                        BookDetails.currentid!!
                                                                    )
                                                                    databaseReference.child("status").setValue("reviewed")


                                                                    val databaseReference3 = FirebaseDatabase.getInstance().getReference("book").child(BookDetails.bookid!!)

                                                                    databaseReference3.child("totalreview").addListenerForSingleValueEvent(object : ValueEventListener {
                                                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                                            val totalReview = dataSnapshot.value?.toString()?.toLong() ?: 0
                                                                            println("current total review: $totalReview")
                                                                            databaseReference3.child("totalreview").setValue((totalReview + 1).toString())

                                                                            databaseReference3.child("averagereview").addListenerForSingleValueEvent(object : ValueEventListener {
                                                                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                                                    val averageReview = dataSnapshot.value?.toString()?.toDouble() ?: 0.0

                                                                                    println("current average review: $averageReview")
                                                                                    val newAverageReview = (averageReview + s2.toDouble())
                                                                                    databaseReference3.child("averagereview").setValue(newAverageReview.toString())
                                                                                    println("new average review: $newAverageReview")
                                                                                }

                                                                                override fun onCancelled(databaseError: DatabaseError) {
                                                                                    // Handle any errors
                                                                                }
                                                                            })
                                                                        }

                                                                        override fun onCancelled(databaseError: DatabaseError) {
                                                                            // Handle any errors
                                                                        }
                                                                    })


                                                                    val databaseReference1 = FirebaseDatabase.getInstance().getReference("users").child(nodeName)
                                                                    databaseReference1.child("totalreview").addListenerForSingleValueEvent(object : ValueEventListener {
                                                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                                            val totalReview = dataSnapshot.value?.toString()?.toLong() ?: 0
                                                                            databaseReference1.child("totalreview").setValue((totalReview.toInt() + 1).toString())


                                                                        }

                                                                        override fun onCancelled(databaseError: DatabaseError) {
                                                                            // Handle any errors
                                                                        }
                                                                    })
                                                                }

                                                 else {
                                                                    // Show error message
                                                                    Toast.makeText(
                                                                        context,
                                                                        "Failed to save data.",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }
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
                                    // Handle any errors
                                }
                            })
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors
                }
            })

            dialog.dismiss()

        }

        dialog.show()
    }
    fun updateBookList(bookList: List<BookDetails>) {
        this.bookList = bookList
        this.filteredBookList = bookList
        notifyDataSetChanged()
    }

    fun fetchBookList() {
        db.orderByChild("nodeName").equalTo(nodeName)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val bookList = mutableListOf<BookDetails>()
                    for (postSnapshot in dataSnapshot.children) {
                        val bookDetails = postSnapshot.getValue(BookDetails::class.java)
                        if (bookDetails?.status in listOf("returned", "payed") && bookDetails?.nodeName == nodeName) {
                            bookList.add(bookDetails!!)
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
    fun sortByAuthor() {
        bookList = bookList.sortedBy { it.bookname }
        notifyDataSetChanged()
    }

    fun sortByName() {
        bookList = bookList.sortedBy { it.borrowername }
        notifyDataSetChanged()
    }

    fun sortByDate() {
        bookList = bookList.sortedBy { it.dateborrow }
        notifyDataSetChanged()
    }
    fun sortByQuantity() {
        bookList = bookList.sortedByDescending { it.date }
        notifyDataSetChanged()
    }

}