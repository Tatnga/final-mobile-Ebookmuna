package com.example.app2

import android.app.Dialog
import android.content.Context
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
class BookAdapter1(private val context: Context, var bookList: List<book>) :
    RecyclerView.Adapter<BookAdapter1.BookViewHolder>() {

    private var filteredBookList: List<book> = bookList.filter { it.bookquantity.toIntOrNull() ?: 0 > 0 }


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
            LayoutInflater.from(parent.context).inflate(R.layout.book_list_item1, parent, false)
        return BookViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredBookList.size
    }

    fun search(query: String) {
        filteredBookList = if (query.isEmpty()) {
            bookList.filter { it.bookquantity.toIntOrNull() ?: 0 > 0}
        } else {
            bookList.filter { book ->
                book.bookname.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
                        || book.bookauthor.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
            }.filter { it.bookquantity.toIntOrNull() ?: 0 > 0}
        }
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = filteredBookList[position]
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

    fun setData(bookList: List<book>) {
        this.bookList = bookList
        filteredBookList = bookList
        notifyDataSetChanged()
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
        textView30.text = "Available Book ${book.bookquantity}"
        textView8.text = "Average Review book ${(book.averagereview.toDouble() / book.totalreview.toDouble()).toString()}"

        // Load book image from Firebase Storage and set it to ImageView
        val storageRef = book.book_image_url
        Glide.with(context)
            .load(storageRef)
            .into(imageView5)
        val rootNode = FirebaseDatabase.getInstance().reference


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
                                            borrownode,book.totaldays
                                        )
                                        val builder = android.app.AlertDialog.Builder(context)
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
                                                        Toast.makeText(context, "Book Borrowed!", Toast.LENGTH_SHORT).show()
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


    fun updateBookList(bookList: List<book>) {
        this.bookList = bookList
        this.filteredBookList = bookList.filter { it.bookquantity.toIntOrNull() ?: 0 > 0 }
            .sortedBy { it.bookname }
        println("Updated book list: $bookList")
        notifyDataSetChanged()
    }
    fun updateBookList1(bookList: List<book>) {
        this.bookList = bookList
        this.filteredBookList = bookList.filter { it.bookquantity.toIntOrNull() ?: 0 > 0}
            .sortedByDescending { it.bookname }
        println("Updated book list: $bookList")
        notifyDataSetChanged()
    }
    fun updateBookList2(bookList: List<book>) {
        this.bookList = bookList
        this.filteredBookList = bookList.filter { it.bookquantity.toIntOrNull() ?: 0 > 0 }
            .sortedBy { it.bookauthor }
        println("Updated book list: $bookList")
        notifyDataSetChanged()
    }
    fun updateBookList3(bookList: List<book>) {
        this.bookList = bookList
        this.filteredBookList = bookList.filter { it.bookquantity.toIntOrNull() ?: 0 > 0}
            .sortedByDescending { it.bookauthor }
        println("Updated book list: $bookList")
        notifyDataSetChanged()
    }
    fun updateBookList4(bookList: List<book>) {
        this.bookList = bookList
        this.filteredBookList = bookList.filter { it.bookquantity.toIntOrNull() ?: 0 > 0 }
            .sortedBy { it.date }
        println("Updated book list: $bookList")
        notifyDataSetChanged()
    }
    fun updateBookList5(bookList: List<book>) {
        this.bookList = bookList
        this.filteredBookList = bookList.filter { it.bookquantity.toIntOrNull() ?: 0 > 0}
            .sortedByDescending { it.date }
        println("Updated book list: $bookList")
        notifyDataSetChanged()
    }
    fun fetchBookList() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val bookList = mutableListOf<book>()
                if (dataSnapshot.hasChildren()) {
                    for (postSnapshot in dataSnapshot.children) {
                        val book = postSnapshot.getValue(book::class.java)
                        if ((book?.bookquantity?.toIntOrNull() ?: 0) > 0) { // Only include books with positive quantity
                            bookList.add(book!!)
                        }
                    }
                    val sortedBookList = bookList.sortedBy { it.totalbookborrowed }
                    updateBookList(sortedBookList)
                } else {
                    println("No data found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors
            }
        })
    }
    fun fetchBookList1() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val bookList = mutableListOf<book>()
                if (dataSnapshot.hasChildren()) {
                    for (postSnapshot in dataSnapshot.children) {
                        val book = postSnapshot.getValue(book::class.java)
                        println("Retrieved book: $book")
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
    fun sortByAuthor() {
        bookList = bookList.sortedBy { it.bookname }
        notifyDataSetChanged()
    }

    fun sortByName() {
        bookList = bookList.sortedBy { it.bookauthor }
        notifyDataSetChanged()
    }

    fun sortByDate() {
        bookList = bookList.sortedBy { it.bookcatalog }
        notifyDataSetChanged()
    }
    fun sortByQuantity() {
        bookList = bookList.sortedByDescending { it.bookquantity }
        notifyDataSetChanged()
    }

}